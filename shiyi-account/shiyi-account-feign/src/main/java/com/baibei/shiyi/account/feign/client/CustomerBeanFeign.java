package com.baibei.shiyi.account.feign.client;

import com.baibei.shiyi.account.feign.base.shiyi.ICustomerBeanBase;
import com.baibei.shiyi.account.feign.client.hystrix.CustomerBeanHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/10/29 17:32
 * @description:
 */
@FeignClient(value = "shiyi-account", fallbackFactory = CustomerBeanHystrix.class)
public interface CustomerBeanFeign extends ICustomerBeanBase {
}
