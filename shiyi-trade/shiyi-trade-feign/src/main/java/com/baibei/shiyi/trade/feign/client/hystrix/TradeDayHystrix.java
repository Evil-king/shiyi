package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.client.TradeDayFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TradeDayHystrix implements FallbackFactory<TradeDayFeign> {

    @Override
    public TradeDayFeign create(Throwable throwable) {
        return day -> {
            log.info("isTradeDay hystrix is error,exception is {}", throwable);
            return ApiResult.serviceFail();
        };
    }
}
