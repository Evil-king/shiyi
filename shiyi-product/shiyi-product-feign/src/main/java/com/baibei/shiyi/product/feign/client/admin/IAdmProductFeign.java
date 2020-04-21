package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseProductSkuVo;
import com.baibei.shiyi.product.feign.client.hystrix.AdmProductHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${shiyi-product:shiyi-product}", path = "/admin/product/product", fallbackFactory = AdmProductHystrix.class)
public interface IAdmProductFeign {

    @PostMapping("/pageList")
    ApiResult<MyPageInfo<AdmProductVo>> pageList(@Validated @RequestBody AdmProductDto admProductDto);

    @PostMapping("/addOrEditProduct")
    ApiResult addOrEditProduct(@Validated @RequestBody AddProductDto addProductDto);

    @PostMapping("/editProductPage")
    ApiResult<AdmEditProductVo> editProductPage(@Validated @RequestBody AdmEditProductPageDto admEditProductPageDto);

    /***
     * 删除商品(软删除)
     * @param AdmDeleteProductDto
     * @return
     */
    @PostMapping("/deleteProduct")
    ApiResult deleteProduct(@Validated @RequestBody AdmDeleteProductDto AdmDeleteProductDto);

    /***
     * 批量删除商品(软删除)
     * @param AdmDeleteProductDto
     * @return
     */
    @PostMapping("/batchDeleteProduct")
    ApiResult batchDeleteProduct(@Validated @RequestBody List<AdmDeleteProductDto> AdmDeleteProductDto);


    /***
     * 获取商品sku列表
     * @param spuDto
     * @return
     */
    @PostMapping("/getProductSkuList")
    ApiResult<List<BaseProductSkuVo>> getProductSkuList(@Validated @RequestBody SpuDto spuDto);
}
