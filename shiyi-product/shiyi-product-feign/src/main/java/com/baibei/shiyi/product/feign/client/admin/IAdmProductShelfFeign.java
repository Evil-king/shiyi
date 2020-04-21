package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.feign.client.hystrix.AdmProductHystrix;
import com.baibei.shiyi.product.feign.client.hystrix.AdmProductShelfHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(value = "${shiyi-product:shiyi-product}", path = "/admin/product/shelf", fallbackFactory = AdmProductShelfHystrix.class)
public interface IAdmProductShelfFeign {

    /**
     * 商品上下架列表
     * @param admSelectProductShelfDto
     * @return
     */
    @PostMapping("/pageList")
    ApiResult<MyPageInfo<BaseShelfVo>> pageList(@Validated @RequestBody AdmSelectProductShelfDto admSelectProductShelfDto);

    /**
     * 上架商品
     * @param admProductShelfDto
     * @return
     */
    @PostMapping("/shelfProduct")
    ApiResult shelfProduct(@Validated @RequestBody AdmProductShelfDto admProductShelfDto);

    /**
     * 跳转到编辑上架商品页面（数据回显）
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/editShelfProductPage")
    ApiResult<AdmEditProductShelfVo> editShelfProductPage(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto);

    /**
     * 跳转到新增上架商品页面（数据回显）
     * @return
     */
    @PostMapping("/toAddShelfProductPage")
    ApiResult<AdmToAddProductShelfVo> toAddShelfProductPage();

    /**
     * 获取上架商品的属性列表数据
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/getAdmShelfSkuInfoList")
    ApiResult<List<AdmProductShelfSkuVo>> getAdmShelfSkuInfoList(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto);

    /**
     * 删除上架商品
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/deleteProductShelf")
    ApiResult deleteProductShelf(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto);

    /**
     * 批量删除上架商品
     * @param batchDeleteShelfDtoList
     * @return
     */
    @PostMapping("/batchDeleletProductShelf")
    ApiResult batchDeleletProductShelf(@Validated @RequestBody List<BatchDeleteShelfDto> batchDeleteShelfDtoList);

    /**
     * 商品上下架
     * @param shelfProductDto
     * @return
     */
    @PostMapping("/shelf")
    ApiResult shelf(@Validated @RequestBody ShelfProductDto shelfProductDto);

    /**
     * 批量上下架
     * @param shelfProductDtoList
     * @return
     */
    @PostMapping("/batchShelf")
    ApiResult batchShelf(@Validated @RequestBody List<ShelfProductDto> shelfProductDtoList);
}
