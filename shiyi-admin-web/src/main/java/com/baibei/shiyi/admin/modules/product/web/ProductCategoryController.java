package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmCategoryFeign;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/product/category")
public class ProductCategoryController {

    @Autowired
    private IAdmCategoryFeign admCategoryFeign;

    /**
     * 获取顶级类目
     * @param admProductDto
     * @return
     */
    @PostMapping("/admTheFirstCategroy")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult<List<CategoryVo>> admTheFirstCategroy() {
        return admCategoryFeign.admTheFirstCategroy();
    }

    /**
     * 获取下级类目
     * @param admProductDto
     * @return
     */
    @PostMapping("/admTheNextCategroy")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult<List<CategoryVo>> admTheNextCategroy(@Validated @RequestBody AdmEditCategoryPageDto admEditCategoryPageDto) {
        return admCategoryFeign.admTheNextCategroy(admEditCategoryPageDto);
    }


    /**
     * 新增前台类目
     * @param addCategoryDto
     * @return
     */
    @PostMapping("/addCategory")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult addCategory(@Validated @RequestBody AddCategoryDto addCategoryDto) {
        return admCategoryFeign.addOrEditCategory(addCategoryDto);
    }

    /**
     * 编辑前台类目
     * @param addCategoryDto
     * @return
     */
    @PostMapping("/editCategory")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult editCategory(@Validated @RequestBody AddCategoryDto addCategoryDto) {
        return admCategoryFeign.addOrEditCategory(addCategoryDto);
    }

    /**
     * 跳转到编辑商品的页面，数据回显
     * @param admEditCategoryPageDto
     * @return
     */
    @PostMapping("/editCategoryPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult<CategoryVo> editCategoryPage(@Validated @RequestBody AdmEditCategoryPageDto admEditCategoryPageDto) {
        return admCategoryFeign.editCategoryPage(admEditCategoryPageDto);
    }

    /**
     * 删除类目
     * @param deleteCategoryDto
     * @return
     */
    @PostMapping("/deleteCategory")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult deleteCategory(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto) {
        return  admCategoryFeign.deleteCategory(deleteCategoryDto);
    }

    /**
     * 获取类目下的商品列表
     * @param admCategoryProductDto
     * @return
     */
    @PostMapping("/categoryProductList")
    public  ApiResult<MyPageInfo<BaseShelfVo>> categoryProductList(@Validated @RequestBody AdmCategoryProductDto admCategoryProductDto) {
        ApiResult<MyPageInfo<BaseShelfVo>> myPageInfoApiResult = admCategoryFeign.categoryProductList(admCategoryProductDto);
        return myPageInfoApiResult;
    }

    /**
     * 清空类目下的所有商品
     * @param deleteCategoryDto
     * @return
     */
    @PostMapping("/clearCategoryProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult clearCategoryProduct(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto) {
        return admCategoryFeign.clearCategoryProduct(deleteCategoryDto);
    }

    /**
     * 删除前台类目下的某个商品
     * @param deleteOneCategoryProductDto
     * @return
     */
    @PostMapping("/deleteOneCategoryProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult deleteOneCategoryProduct(@Validated @RequestBody DeleteOneCategoryProductDto deleteOneCategoryProductDto) {
        return admCategoryFeign.deleteOneCategoryProduct(deleteOneCategoryProductDto);
    }


    /**
     * 获取不在指定类目下的商品集合
     * @param admProductNotInCategroyDto
     * @return
     */
    @PostMapping("/productListNotInCategory")
    public ApiResult<MyPageInfo<BaseShelfVo>> productListNotInCategory(@Validated @RequestBody AdmProductNotInCategroyDto admProductNotInCategroyDto) {
        return admCategoryFeign.productListNotInCategory(admProductNotInCategroyDto);
    }

    /**
     * 批量添加前台类目商品
     * @param addCategoryProductList
     * @return
     */
    @PostMapping("/batchAddCategoryProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_FRONT'))")
    public ApiResult batchAddCategoryProduct(@Validated @RequestBody List<AddCategoryProduct> addCategoryProductList) {
        return admCategoryFeign.batchAddCategoryProduct(addCategoryProductList);
    }

    /**
     * 批量删除类目下的商品
     * @param deleteOneCategoryProductDtoList
     * @return
     */
    @PostMapping("/batchDeleteCategoryProduct")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_FRONT'))")
    ApiResult batchDeleteCategoryProduct(@Validated @RequestBody List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList){
        return admCategoryFeign.batchDeleteCategoryProduct(deleteOneCategoryProductDtoList);
    }

    /**
     * 获取前台类目树
     * @return
     */
    @PostMapping("/getCategoryTree")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_CATEGORY_FRONT'))")
    ApiResult<List<Map<String,Object>>> getCategoryTree(){
        return admCategoryFeign.getCategoryTree();
    }

}
