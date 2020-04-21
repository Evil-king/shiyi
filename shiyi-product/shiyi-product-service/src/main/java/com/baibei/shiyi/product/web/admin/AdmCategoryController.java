package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.BaseIndexProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmCategoryFeign;
import com.baibei.shiyi.product.model.Category;
import com.baibei.shiyi.product.service.ICategoryService;
import com.baibei.shiyi.product.service.IShelfCategoryRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @Classname AdmProductController
 * @Description 后台管理商品相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/admin/product/category")
public class AdmCategoryController implements IAdmCategoryFeign {

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IShelfCategoryRefService shelfCategoryRefService;


    @Override
    public ApiResult addOrEditCategory(@Validated @RequestBody AddCategoryDto addCategoryDto) {
        categoryService.addOrEditCategory(addCategoryDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult deleteCategory(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto) {
        categoryService.deleteCategory(deleteCategoryDto.getCategoryId());
        return ApiResult.success();
    }

    @Override
    public ApiResult<MyPageInfo<BaseShelfVo>> categoryProductList(@Validated @RequestBody AdmCategoryProductDto admCategoryProductDto) {
        MyPageInfo<BaseShelfVo> admCategoryProductList = categoryService.getAdmCategoryProductList(admCategoryProductDto);
        return ApiResult.success(admCategoryProductList);
    }

    @Override
    public ApiResult<MyPageInfo<BaseShelfVo>> productListNotInCategory(@Validated @RequestBody AdmProductNotInCategroyDto admProductNotInCategroyDto) {
        MyPageInfo<BaseShelfVo> productListNotInCategory = categoryService.getProductListNotInCategory(admProductNotInCategroyDto);
        return ApiResult.success(productListNotInCategory);
    }

    @Override
    public ApiResult clearCategoryProduct(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto) {
        shelfCategoryRefService.clearCategroyProduct(deleteCategoryDto.getCategoryId());
        return ApiResult.success();
    }

    @Override
    public ApiResult deleteOneCategoryProduct(@Validated @RequestBody DeleteOneCategoryProductDto deleteOneCategoryProductDto) {
        shelfCategoryRefService.deleteCategoryProduct(deleteOneCategoryProductDto.getCategoryId(),deleteOneCategoryProductDto.getShelfId());
        return ApiResult.success();
    }

    @Override
    public ApiResult batchDeleteCategoryProduct(@Validated @RequestBody List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList) {
        shelfCategoryRefService.batchDeleteCategoryProduct(deleteOneCategoryProductDtoList);
        return ApiResult.success();
    }

    @Override
    public ApiResult batchAddCategoryProduct(@Validated @RequestBody List<AddCategoryProduct> addCategoryProductList) {
        shelfCategoryRefService.batchAddCategoryProduct(addCategoryProductList);
        return ApiResult.success();
    }

    @Override
    public ApiResult<CategoryVo> editCategoryPage(@Validated @RequestBody AdmEditCategoryPageDto admEditCategoryPageDto) {
        CategoryVo categoryVo = categoryService.getCategoryById(admEditCategoryPageDto.getCategoryId());
        return ApiResult.success(categoryVo);
    }

    @Override
    public ApiResult<List<CategoryVo>> admTheFirstCategroy() {
        List<CategoryVo> categoryVoList = categoryService.getAdmTheFirstCategroy();
        return ApiResult.success(categoryVoList);
    }

    @Override
    public ApiResult<List<CategoryVo>> admTheNextCategroy(@Validated @RequestBody AdmEditCategoryPageDto admEditCategoryPageDto) {
        List<CategoryVo> categoryVoList = categoryService.getAdmTheNextCategroy(admEditCategoryPageDto.getCategoryId());
        return ApiResult.success(categoryVoList);
    }

    @Override
    public ApiResult<List<Map<String, Object>>> getCategoryTree() {
        List<Map<String, Object>> categoryTree = categoryService.getCategoryTree();
        return ApiResult.success(categoryTree);
    }
}
