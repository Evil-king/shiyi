package com.baibei.shiyi.user.feign.client;

import com.baibei.shiyi.user.feign.base.shiyi.ICustomerAddressBase;
import com.baibei.shiyi.user.feign.client.hystrix.CustomerAddressHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/5/24 1:51 PM
 * @description:
 */
@FeignClient(value = "shiyi-user", fallbackFactory = CustomerAddressHystrix.class)
public interface CustomerAddressFeign extends ICustomerAddressBase {

}
