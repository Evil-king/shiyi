package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.client.hystrix.ProductStockHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@FeignClient(value = "${shiyi-product:shiyi-product}", path = "/admin/product/stock", fallbackFactory = ProductStockHystrix.class)
public interface IProductStockFeign {

    /**
     * 后台更改库存
     * @param changeStockDto
     * @return
     */
    @PostMapping("/changeStock")
    ApiResult changeStock(ChangeStockDto changeStockDto);
}
