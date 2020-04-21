package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductShelfFeign;
import com.baibei.shiyi.product.service.IProductService;
import com.baibei.shiyi.product.service.IProductShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Classname AdmProductShelfController
 * @Description 后台管理商品上下架
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/admin/product/shelf")
public class AdmProductShelfController implements IAdmProductShelfFeign {

    @Autowired
    private IProductShelfService productShelfService;


    @Override
    public ApiResult<MyPageInfo<BaseShelfVo>> pageList(@Validated @RequestBody AdmSelectProductShelfDto admSelectProductShelfDto) {
        MyPageInfo<BaseShelfVo> baseShelfVoMyPageInfo =productShelfService.getProductShelfListForPage(admSelectProductShelfDto);
        return ApiResult.success(baseShelfVoMyPageInfo);
    }

    @Override
    public ApiResult shelfProduct(@Validated @RequestBody AdmProductShelfDto admProductShelfDto) {
        productShelfService.shelfProduct(admProductShelfDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<AdmEditProductShelfVo> editShelfProductPage(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto) {
        AdmEditProductShelfVo admEditProductShelfVo = productShelfService.admEditShelfProductPage(admEditProductShelfPageDto.getShelfId());
        return ApiResult.success(admEditProductShelfVo);
    }

    @Override
    public ApiResult<AdmToAddProductShelfVo> toAddShelfProductPage() {
        AdmToAddProductShelfVo admToAddProductShelfVo = productShelfService.admToAddProductShelfPage();
        return ApiResult.success(admToAddProductShelfVo);
    }

    @Override
    public ApiResult<List<AdmProductShelfSkuVo>> getAdmShelfSkuInfoList(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto) {
        List<AdmProductShelfSkuVo> admProductShelfSkuVoList = productShelfService.getAdmShelfSkuInfoList(admEditProductShelfPageDto.getShelfId());
        return ApiResult.success(admProductShelfSkuVoList);
    }

    @Override
    public ApiResult deleteProductShelf(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto) {
        productShelfService.softDeleteById(admEditProductShelfPageDto.getShelfId());
        return ApiResult.success();
    }

    @Override
    public ApiResult batchDeleletProductShelf(@Validated @RequestBody List<BatchDeleteShelfDto> batchDeleteShelfDtoList) {
        return productShelfService.batchSoftDelete(batchDeleteShelfDtoList);
    }

    @Override
    public ApiResult shelf(@Validated @RequestBody ShelfProductDto shelfProductDto) {
        return productShelfService.shelf(shelfProductDto);
    }

    @Override
    public ApiResult batchShelf(@Validated @RequestBody List<ShelfProductDto> shelfProductDtoList) {
        return productShelfService.batchShelf(shelfProductDtoList);
    }
}
