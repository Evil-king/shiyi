package com.baibei.shiyi.order.rocketmq.comsumer;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.HttpClientUtils;
import com.baibei.shiyi.order.feign.base.dto.OrderPay;
import com.baibei.shiyi.order.feign.base.dto.OrderReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Longer
 * @date: 2019/10/11 14:53
 * @description: 请求福清，确认支付
 */
@Component
@Slf4j
public class OrderPayConsumerImpl implements IConsumer<OrderPay> {
    @Value("${fuqing.request.url}")
    private String fuqingReqUrl;//福清域名
    @Override
    public ApiResult execute(OrderPay orderPay) {
        ApiResult apiResult;
        try{
            String url = fuqingReqUrl+"/orderPay/orderPay.json";
            log.info("OrderPayConsumerImpl,请求福清地址为：{},参数为：{}",url,orderPay);
            String s = HttpClientUtils.doPostJson(url, JSONObject.toJSONString(orderPay));
            log.info("请求福清，确认支付：{}",s);
            apiResult=ApiResult.success();
        }catch (Exception e){
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}