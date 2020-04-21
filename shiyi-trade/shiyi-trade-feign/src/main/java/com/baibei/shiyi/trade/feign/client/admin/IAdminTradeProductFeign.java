package com.baibei.shiyi.trade.feign.client.admin;


import com.baibei.shiyi.trade.feign.base.IAdminTradeProductBase;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminTradeProductHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shiyi-trade", fallbackFactory = AdminTradeProductHystrix.class)
public interface IAdminTradeProductFeign extends IAdminTradeProductBase {
}
