package com.baibei.shiyi.pingan.feign.client.hystrix;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;
import com.baibei.shiyi.pingan.feign.client.api.IApiPABFilePlannedSpeedFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PABFilePlannedHystrix implements FallbackFactory<IApiPABFilePlannedSpeedFeign> {

    @Override
    public IApiPABFilePlannedSpeedFeign create(Throwable throwable) {
        return new IApiPABFilePlannedSpeedFeign() {
            @Override
            public ApiResult<FilePlannedSpeedVo> filePlannedSpeed(FilePlannedSpeedDto filePlannedSpeedDto) {
                log.info("filePlannedSpeed is hystrix params is {} , error is {}", JSON.toJSONString(filePlannedSpeedDto),throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
