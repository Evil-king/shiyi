package com.baibei.shiyi.publicc.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.publicc.feign.bean.dto.OperatorSmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.SmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author hwq
 * @date 2019/05/24
 */
public interface IPublicBase {

    /**
     * 短信接口
     *
     * @param smsDto
     * @return
     */
    @RequestMapping(value = "/api/sms/getSms", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<String> getSms(@RequestBody SmsDto smsDto);

    /**
     * 验证短信
     *
     * @param validateCodeDto
     * @return
     */
    @RequestMapping(value = "/api/sms/validateCode", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<String> validateCode(@RequestBody ValidateCodeDto validateCodeDto);


    /**
     * 短信模板操作
     * @param operatorSmsDto
     * @return
     */
    @RequestMapping(value = "/api/sms/operatorSms", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<String> operatorSms(@RequestBody OperatorSmsDto operatorSmsDto);

}
