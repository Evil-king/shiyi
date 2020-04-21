package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.product.feign.base.admin.IAdminProductProTypeBase;
import com.baibei.shiyi.product.feign.client.hystrix.AdminProductProTypeHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/9/6 16:23
 * @description:
 */
@FeignClient(value = "shiyi-product", fallbackFactory = AdminProductProTypeHystrix.class)
public interface AdminProductProTypeFeign extends IAdminProductProTypeBase {
}
