package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.feign.client.hystrix.AdmCategoryHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;


@FeignClient(value = "${shiyi-product:shiyi-product}", path = "/admin/product/category", fallbackFactory = AdmCategoryHystrix.class)
public interface IAdmCategoryFeign {

    /**
     * 新增或编辑类目
     * @param addCategoryDto
     * @return
     */
    @PostMapping("/addOrEditCategory")
    ApiResult addOrEditCategory(@Validated @RequestBody AddCategoryDto addCategoryDto);


    /**
     * 删除类目
     * @param deleteCategoryDto
     * @return
     */
    @PostMapping("/deleteCategory")
    ApiResult deleteCategory(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto);

    /**
     * 获取类目下的商品列表
     * @param admCategoryProductDto
     * @return
     */
    @PostMapping("/categoryProductList")
    ApiResult<MyPageInfo<BaseShelfVo>> categoryProductList(@Validated @RequestBody AdmCategoryProductDto admCategoryProductDto);

    /**
     * 获取不在指定类目下的商品列表
     * @param admProductNotInCategroyDto
     * @return
     */
    @PostMapping("/productListNotInCategory")
    ApiResult<MyPageInfo<BaseShelfVo>> productListNotInCategory(@Validated @RequestBody AdmProductNotInCategroyDto admProductNotInCategroyDto);

    /**
     * 清空类目下的所有商品
     * @param deleteCategoryDto
     * @return
     */
    @PostMapping("/clearCategoryProduct")
    ApiResult clearCategoryProduct(@Validated @RequestBody DeleteCategoryDto deleteCategoryDto);

    /**
     * 删除类目下的某个商品
     * @param deleteOneCategoryProductDto
     * @return
     */
    @PostMapping("/deleteOneCategoryProduct")
    ApiResult deleteOneCategoryProduct(@Validated @RequestBody DeleteOneCategoryProductDto deleteOneCategoryProductDto);

    /**
     * 批量删除类目下的商品
     * @param deleteOneCategoryProductDtoList
     * @return
     */
    @PostMapping("/batchDeleteCategoryProduct")
    ApiResult batchDeleteCategoryProduct(@Validated @RequestBody List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList);


    /**
     * 批量添加类目商品
     * @param addCategoryProductList
     * @return
     */
    @PostMapping("/batchAddCategoryProduct")
    ApiResult batchAddCategoryProduct(@Validated @RequestBody List<AddCategoryProduct> addCategoryProductList);

    /**
     * 跳转到编辑类目页面
     * @param admEditCategoryPageDto
     * @return
     */
    @PostMapping("/editCategoryPage")
    ApiResult<CategoryVo> editCategoryPage(AdmEditCategoryPageDto admEditCategoryPageDto);

    /**
     * 后台，获取顶级类目
     * @return
     */
    @PostMapping("/admTheFirstCategroy")
    ApiResult<List<CategoryVo>> admTheFirstCategroy();

    /**
     * 后台，获取下级类目
     * @param admEditCategoryPageDto
     * @return
     */
    @PostMapping("/admTheNextCategroy")
    ApiResult<List<CategoryVo>> admTheNextCategroy(AdmEditCategoryPageDto admEditCategoryPageDto);

    /**
     * 获取前台类目树
     * @return
     */
    @PostMapping("/getCategoryTree")
    ApiResult<List<Map<String,Object>>> getCategoryTree();
}
