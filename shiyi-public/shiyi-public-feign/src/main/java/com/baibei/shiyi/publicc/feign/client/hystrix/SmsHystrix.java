package com.baibei.shiyi.publicc.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.publicc.feign.bean.dto.OperatorSmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.SmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import com.baibei.shiyi.publicc.feign.client.SmsFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author hwq
 * @date 2019/05/24
 */
@Component
@Slf4j
public class SmsHystrix implements FallbackFactory<SmsFeign> {


    @Override
    public SmsFeign create(Throwable throwable) {
        return new SmsFeign() {
            @Override
            public ApiResult<String> getSms(@RequestBody SmsDto smsDto) {
                log.info("current getSms hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> validateCode(@RequestBody ValidateCodeDto validateCodeDto) {
                log.info("current validateCode hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<String> operatorSms(@RequestBody OperatorSmsDto operatorSmsDto) {
                log.info("current @RequestBody hystrix is error,exception is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
