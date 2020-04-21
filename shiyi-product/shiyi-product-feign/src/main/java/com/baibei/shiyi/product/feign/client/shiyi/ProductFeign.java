package com.baibei.shiyi.product.feign.client.shiyi;

import com.baibei.shiyi.product.feign.base.shiyi.IProductBase;
import com.baibei.shiyi.product.feign.client.hystrix.ProductHystrix;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Longer
 * @date 2019/08/1
 */
@FeignClient(value = "shiyi-product", fallbackFactory = ProductHystrix.class)
public interface ProductFeign extends IProductBase {
}


