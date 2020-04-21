package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.RiskTradeDto;
import com.baibei.shiyi.trade.feign.client.admin.IAdminRiskTradeFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdminRiskTradeHystrix implements FallbackFactory<IAdminRiskTradeFeign> {

    @Override
    public IAdminRiskTradeFeign create(Throwable throwable) {
        return new IAdminRiskTradeFeign() {
            @Override
            public ApiResult save(RiskTradeDto riskTradeDto) {
                log.info("current save is fallbackFactory,params is {},error is {}", riskTradeDto,throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<RiskTradeDto> get() {
                log.info("current get is fallbackFactory,error is {}",throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
