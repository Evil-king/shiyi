package com.baibei.shiyi.order.rocketmq.comsumer;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.HttpClientUtils;
import com.baibei.shiyi.order.feign.base.dto.OrderReport;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Longer
 * @date: 2019/10/11 14:53
 * @description: 向清算所推算订单信息
 */
@Component
@Slf4j
public class OrderReportConsumerImpl implements IConsumer<OrderReport> {

    @Value("${fuqing.request.url}")
    private String fuqingReqUrl;//福清域名
    @Autowired
    private ICustomerFeign customerFeign;
    private SimpleDateFormat yyyyMMddNoLine = (SimpleDateFormat)DateUtil.yyyyMMddNoLine.get();
    @Override
    public ApiResult execute(OrderReport orderReport) {
        ApiResult apiResult;
        try{
            String url = fuqingReqUrl+"/orderReport/orderReport.json";
            //获取卖方用户实名信息
            CustomerBaseDto sellerDto = new CustomerBaseDto();
            sellerDto.setCustomerNo(orderReport.getSellerMemCode());
            ApiResult<RealnameInfoVo> sellerRealnameInfoVoApiResult = customerFeign.realnameInfo(sellerDto);
            if (sellerRealnameInfoVoApiResult.hasFail()||StringUtils.isEmpty(sellerRealnameInfoVoApiResult.getData())) {
                log.info("OrderReportConsumerImpl查询买方用户实名信息失败：",sellerRealnameInfoVoApiResult);
                return ApiResult.success();//return success不再重试
            }
            RealnameInfoVo sellerRealnameInfoVo = sellerRealnameInfoVoApiResult.getData();
            orderReport.setSellerAccountName(sellerRealnameInfoVo.getRealname());
            //获取买方用户实名信息
            CustomerBaseDto buyerDto = new CustomerBaseDto();
            buyerDto.setCustomerNo(orderReport.getBuyerMemCode());
            ApiResult<RealnameInfoVo> realnameInfoVoApiResult = customerFeign.realnameInfo(buyerDto);
            if (realnameInfoVoApiResult.hasFail()||StringUtils.isEmpty(realnameInfoVoApiResult.getData())) {
                log.info("OrderReportConsumerImpl查询买方用户实名信息失败：",realnameInfoVoApiResult);
                return ApiResult.success();//return success不再重试
            }
            RealnameInfoVo buyerRealnameInfoVo = realnameInfoVoApiResult.getData();
            orderReport.setBuyerAccountName(buyerRealnameInfoVo.getRealname());

            log.info("OrderReportConsumerImpl,请求福清地址为：{},参数为：{}",url,orderReport);
            String s = HttpClientUtils.doPostJson(url, JSONObject.toJSONString(orderReport));
            log.info("向清算所推算订单信息结果：{}",s);
            apiResult=ApiResult.success();
        }catch (Exception e){
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}