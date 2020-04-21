package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.product.feign.base.admin.IAdminBrandBase;
import com.baibei.shiyi.product.feign.client.hystrix.AdminBrandHystrix;
import com.baibei.shiyi.product.feign.client.hystrix.AdminParameterHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author: hyc
 * @date: 2019/9/12 10:03
 * @description:
 */
@FeignClient(value = "shiyi-product", fallbackFactory = AdminBrandHystrix.class)
public interface AdminBrandFeign extends IAdminBrandBase {
}
