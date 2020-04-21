package com.baibei.shiyi.user.feign.client.admin;

import com.baibei.shiyi.user.feign.base.admin.IAdminCustomerBase;
import com.baibei.shiyi.user.feign.client.hystrix.AdminCustomerHystrix;
import com.baibei.shiyi.user.feign.client.hystrix.CustomerHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/11/1 14:56
 * @description:
 */
@FeignClient(value = "shiyi-user", fallbackFactory = AdminCustomerHystrix.class)
public interface AdminCustomerFeign extends IAdminCustomerBase {
}
