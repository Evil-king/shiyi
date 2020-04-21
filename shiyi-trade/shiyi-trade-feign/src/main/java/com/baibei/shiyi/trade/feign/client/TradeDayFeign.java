package com.baibei.shiyi.trade.feign.client;

import com.baibei.shiyi.trade.feign.base.ITradeDayBase;
import com.baibei.shiyi.trade.feign.client.hystrix.TradeDayHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shiyi-trade", fallbackFactory = TradeDayHystrix.class)
public interface TradeDayFeign extends ITradeDayBase {
}
