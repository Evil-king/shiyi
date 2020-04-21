package com.baibei.shiyi.user.feign.client;

import com.baibei.shiyi.user.feign.base.shiyi.ICustomerBase;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerDetailBase;
import com.baibei.shiyi.user.feign.client.hystrix.CustomerDetailHystrix;
import com.baibei.shiyi.user.feign.client.hystrix.CustomerHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/5/24 1:51 PM
 * @description:
 */
@FeignClient(value = "shiyi-user", fallbackFactory = CustomerDetailHystrix.class)
public interface CustomerDetailFeign extends ICustomerDetailBase {


}
