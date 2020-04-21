package com.baibei.shiyi.trade.feign.client.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.client.hystrix.ShiyiSchedulerHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 16:03
 * @description: 交易-定时任务
 */
@FeignClient(value = "shiyi-trade", fallbackFactory = ShiyiSchedulerHystrix.class)
public interface ShiyiSchedulerFeign {
    /**
     * 交易日相关数据处理
     *
     * @return
     */
    @GetMapping("/shiyi/trade/tradeDay")
    ApiResult tradeDay();

    /**
     * T+N解锁持仓
     *
     * @return
     */
    @GetMapping("/shiyi/trade/unlockHoldPosition")
    ApiResult<Boolean> unlockHoldPosition();
}