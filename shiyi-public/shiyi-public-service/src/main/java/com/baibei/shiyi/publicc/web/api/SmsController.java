package com.baibei.shiyi.publicc.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.publicc.bean.dto.OperatorSmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.SmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import com.baibei.shiyi.publicc.service.ISmsService;
import com.baibei.shiyi.publicc.sms.util.RandomUtils;
import com.baibei.shiyi.publicc.util.SmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hwq
 * @date 2019/05/25
 */
@RestController
@RequestMapping("/api/sms")
public class SmsController{

    @Autowired
    private ISmsService smsService;
    @Autowired
    private SmsUtil smsUtil;

    @RequestMapping("/getSms")
    public ApiResult<String> getSms(@RequestBody @Validated SmsDto smsDto) {
        ApiResult apiResult = smsService.getSms(smsDto.getPhone(), smsDto.getMsg());
        return apiResult;
    }


    @RequestMapping("/validateCode")
    public ApiResult validateCode(@RequestBody @Validated ValidateCodeDto validateCodeDto) {
        ApiResult apiResult = smsService.validateCode(validateCodeDto.getPhone(),
                validateCodeDto.getCode(),validateCodeDto.getType());
        return apiResult;
    }

    @RequestMapping("/operatorSms")
    public ApiResult validateCode(@RequestBody @Validated OperatorSmsDto operatorSmsDto) {
        String code = RandomUtils.getRandomNumber(6);
        String message = smsUtil.operatorSms(operatorSmsDto.getSmsType(), operatorSmsDto.getContentNo(),
                code, operatorSmsDto.getPhone());
        return ApiResult.success(message);
    }
}
