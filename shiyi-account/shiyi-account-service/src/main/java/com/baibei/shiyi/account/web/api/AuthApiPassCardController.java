package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.ExtractProductDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.service.IPassCardService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.content.feign.bean.vo.ExtractProductVo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import com.baibei.shiyi.trade.feign.client.TradeDayFeign;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/11/11 20:07
 * @description:
 */
@RestController
@RequestMapping("/auth/api/account/passCard")
public class AuthApiPassCardController {
    @Autowired
    private IPassCardService passCardService;

    @Autowired
    private CustomerFeign customerFeign;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private TradeDayFeign tradeDayFeign;



    @RequestMapping("/extractProduct")
    public ApiResult extractProduct(@RequestBody @Validated ExtractProductDto extractProductDto){
        if(!NumberUtil.isNumber(extractProductDto.getNumber())){
            return ApiResult.badParam("不支持输入小数或负数，请输入整数");
        }
        if(Integer.valueOf(extractProductDto.getNumber())<1){
            return ApiResult.badParam("提取数量必须大于0");
        }
        ApiResult<ExtractProductVo> apiResult = contentFeign.extractProduct();
        if(apiResult.hasFail()){
            return apiResult;
        }
        ApiResult tradeDay = tradeDayFeign.isTradeDay(new Date());
        if(!tradeDay.hasSuccess() || "false".equals(tradeDay.getData())){
            return ApiResult.badParam("非提取时间");
        }
        Date startTime=DateUtil.getNowDate(apiResult.getData().getStartTime());
        Date endTime=DateUtil.getNowDate(apiResult.getData().getEndTime());
        if(!DateUtil.isEffectiveDate(new Date(), startTime, endTime)){
            return ApiResult.badParam("非提取时间");
        }
        CustomerNoDto customerNoDto=new CustomerNoDto();
        customerNoDto.setCustomerNo(extractProductDto.getCustomerNo());
        ApiResult<CustomerVo> result = customerFeign.findUserByCustomerNo(customerNoDto);
        if(!result.hasSuccess()){
            return result;
        }
        if(result.getData().getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_EXTRACT.getCode())!=-1){
            return ApiResult.badParam("提取失败，请联系客服");
        }
        return passCardService.extractProduct(extractProductDto,apiResult.getData());
    }

}
