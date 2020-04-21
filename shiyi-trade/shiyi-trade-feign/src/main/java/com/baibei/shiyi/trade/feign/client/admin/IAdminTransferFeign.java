package com.baibei.shiyi.trade.feign.client.admin;

import com.baibei.shiyi.trade.feign.base.IAdminTransferBase;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminTransferHystrix;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "shiyi-trade", fallbackFactory = AdminTransferHystrix.class)
public interface IAdminTransferFeign extends IAdminTransferBase {
}
