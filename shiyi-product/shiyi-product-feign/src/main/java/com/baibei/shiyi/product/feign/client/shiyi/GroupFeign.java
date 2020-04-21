package com.baibei.shiyi.product.feign.client.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.client.hystrix.ShiyiGroupHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;



@FeignClient(value = "shiyi-product", fallbackFactory = ShiyiGroupHystrix.class)
public interface GroupFeign {

    /**
     * 获取最小单位小的商品信息（根据shelfNo 和 skuNo获取指定一条商品信息）
     * @param sumGroupProductDto
     * @return
     */
    @PostMapping("/shiyi/product/group/sumGroupProduct")
    @ResponseBody
    ApiResult<Integer> sumGroupProduct(@RequestBody @Validated SumGroupProductDto sumGroupProductDto);
}
