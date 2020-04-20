package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductShelfSkuVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmToAddProductShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductShelfFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/product/shelf")
public class ProductShelfController {

    @Autowired
    private IAdmProductShelfFeign admProductShelfFeign;

    /**
     * 商品列表
     * @param admSelectProductShelfDto
     * @return
     */
    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_MANAGE_ON_SELL'))")
    public ApiResult<MyPageInfo<BaseShelfVo>> pageList(@Validated @RequestBody AdmSelectProductShelfDto admSelectProductShelfDto){
        return admProductShelfFeign.pageList(admSelectProductShelfDto);
    }

    /**
     * 上架新商品
     * @param admProductShelfDto
     * @return
     */
    @PostMapping("/addShelfProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult addShelfProduct(@Validated @RequestBody AdmProductShelfDto admProductShelfDto){
        return admProductShelfFeign.shelfProduct(admProductShelfDto);
    }

    /**
     * 编辑上架商品
     * @param admProductShelfDto
     * @return
     */
    @PostMapping("/editShelfProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult editShelfProduct(@Validated @RequestBody AdmProductShelfDto admProductShelfDto){
        return admProductShelfFeign.shelfProduct(admProductShelfDto);
    }

    /**
     * 跳转到编辑上架商品页面（数据回显）
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/editShelfProductPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult<AdmEditProductShelfVo> editShelfProductPage(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto){
        return admProductShelfFeign.editShelfProductPage(admEditProductShelfPageDto);
    }

    /**
     * 跳转到新增上架商品页面（数据回显）
     * @return
     */
    @PostMapping("/toAddShelfProductPage")
    ApiResult<AdmToAddProductShelfVo> toAddShelfProductPage(){
        return admProductShelfFeign.toAddShelfProductPage();
    }

    /**
     * 获取上架商品的属性列表数据
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/getAdmShelfSkuInfoList")
    ApiResult<List<AdmProductShelfSkuVo>> getAdmShelfSkuInfoList(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto){
        return admProductShelfFeign.getAdmShelfSkuInfoList(admEditProductShelfPageDto);
    }

    /**
     * 删除上架商品
     * @param admEditProductShelfPageDto
     * @return
     */
    @PostMapping("/deleteProductShelf")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult deleteProductShelf(@Validated @RequestBody AdmEditProductShelfPageDto admEditProductShelfPageDto){
        return admProductShelfFeign.deleteProductShelf(admEditProductShelfPageDto);
    }


    /**
     * 批量删除上架商品
     * @param batchDeleteShelfDtoList
     * @return
     */
    @PostMapping("/batchDeleletProductShelf")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult batchDeleletProductShelf(@Validated @RequestBody List<BatchDeleteShelfDto> batchDeleteShelfDtoList){
        return admProductShelfFeign.batchDeleletProductShelf(batchDeleteShelfDtoList);
    }

    /**
     * 商品上下架
     * @param shelfProductDto
     * @return
     */
    @PostMapping("/shelf")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult shelf(@Validated @RequestBody ShelfProductDto shelfProductDto){
        return admProductShelfFeign.shelf(shelfProductDto);
    }

    /**
     * 批量上下架
     * @param shelfProductDtoList
     * @return
     */
    @PostMapping("/batchShelf")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_MANAGE_ON_SELL'))")
    ApiResult batchShelf(@Validated @RequestBody List<ShelfProductDto> shelfProductDtoList){
        return admProductShelfFeign.batchShelf(shelfProductDtoList);
    }
}
