package com.baibei.shiyi.trade.feign.client;

import com.baibei.shiyi.trade.feign.base.IAdminCustomerHoldBase;
import com.baibei.shiyi.trade.feign.base.ICustomerHoldBase;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminCustomerHoldHystrix;
import com.baibei.shiyi.trade.feign.client.hystrix.CustomerHoldHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:25 AM
 * @description:
 */
@FeignClient(value = "shiyi-trade", fallbackFactory = AdminCustomerHoldHystrix.class)
public interface AdminCustomerHoldFeign extends IAdminCustomerHoldBase {

}
