package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseProductSkuVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.feign.client.admin.IGroupFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/product/product")
public class ProductController {

    @Autowired
    private IAdmProductFeign admProductFeign;

    /**
     * 商品列表
     * @param admProductDto
     * @return
     */
    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCTS_LIST'))")
    public ApiResult<MyPageInfo<AdmProductVo>> pageList(@RequestBody AdmProductDto admProductDto) {
        return admProductFeign.pageList(admProductDto);
    }


    /**
     * 新增商品
     * @param addProductDto
     * @return
     */
    @PostMapping("/addProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('PRODUCTS_LIST'))")
    public ApiResult addProduct(@RequestBody @Validated AddProductDto addProductDto) {
        String userId = SecurityUtils.getUsername();
        addProductDto.setUserId(userId);
        return admProductFeign.addOrEditProduct(addProductDto);
    }

    /**
     * 新增编辑商品
     * @param addProductDto
     * @return
     */
    @PostMapping("/editProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCTS_LIST'))")
    public ApiResult editProduct(@RequestBody @Validated AddProductDto addProductDto) {
        String userId = SecurityUtils.getUsername();
        addProductDto.setUserId(userId);
        return admProductFeign.addOrEditProduct(addProductDto);
    }

    /**
     * 跳转到编辑商品的页面，数据回显
     * @param admEditProductPageDto
     * @return
     */
    @PostMapping("/editProductPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCTS_LIST'))")
    public ApiResult editProductPage(@Validated @RequestBody AdmEditProductPageDto admEditProductPageDto) {
        return admProductFeign.editProductPage(admEditProductPageDto);
    }

    /**
     * 删除商品（软删除）
     * @param AdmDeleteProductDto
     * @return
     */
    @PostMapping("/deleteProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCTS_LIST'))")
    public ApiResult deleteProduct(@Validated @RequestBody AdmDeleteProductDto AdmDeleteProductDto) {
        return admProductFeign.deleteProduct(AdmDeleteProductDto);
    }

    /**
     * 批量删除商品（软删除）
     * @param AdmDeleteProductDto
     * @return
     */
    @PostMapping("/batchDeleteProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCTS_LIST'))")
    public ApiResult batchDeleteProduct(@Validated @RequestBody List<AdmDeleteProductDto> AdmDeleteProductDtoList) {
        return admProductFeign.batchDeleteProduct(AdmDeleteProductDtoList);
    }

    /***
     * 获取商品sku列表
     * @param spuDto
     * @return
     */
    @PostMapping("/getProductSkuList")
    public ApiResult<List<BaseProductSkuVo>> getProductSkuList(@Validated @RequestBody SpuDto spuDto){
        return admProductFeign.getProductSkuList(spuDto);
    }
}
