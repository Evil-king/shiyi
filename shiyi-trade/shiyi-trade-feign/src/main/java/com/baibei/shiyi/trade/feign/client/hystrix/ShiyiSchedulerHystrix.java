package com.baibei.shiyi.trade.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.client.shiyi.ShiyiSchedulerFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 16:05
 * @description:
 */
@Component
@Slf4j
public class ShiyiSchedulerHystrix implements FallbackFactory<ShiyiSchedulerFeign> {
    @Override
    public ShiyiSchedulerFeign create(Throwable throwable) {
        return new ShiyiSchedulerFeign() {
            @Override
            public ApiResult tradeDay() {
                log.info("tradeDay fallback，reason was {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<Boolean> unlockHoldPosition() {
                log.info("unlockHoldPosition fallback，reason was {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}