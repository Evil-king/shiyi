package com.baibei.shiyi.trade.feign.client;

import com.baibei.shiyi.trade.feign.base.IAdminDealOrderBase;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminDealOrderHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:25 AM
 * @description:
 */
@FeignClient(value = "shiyi-trade", fallbackFactory = AdminDealOrderHystrix.class)
public interface AdminDealOrderFeign extends IAdminDealOrderBase {

}
