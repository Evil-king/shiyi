package com.baibei.shiyi.product.feign.client.hystrix;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.feign.client.admin.IAdmCategoryFeign;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Longer
 * @date 2019/09/11
 */
@Component
@Slf4j
public class AdmCategoryHystrix implements FallbackFactory<IAdmCategoryFeign> {

    @Override
    public IAdmCategoryFeign create(Throwable throwable) {
        return new IAdmCategoryFeign() {

            @Override
            public ApiResult addOrEditCategory(AddCategoryDto addCategoryDto) {
                log.info("addOrEditCategory current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteCategory(DeleteCategoryDto deleteCategoryDto) {
                log.info("deleteCategory current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<BaseShelfVo>> categoryProductList(AdmCategoryProductDto admCategoryProductDto) {
                log.info("categoryProductList current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<MyPageInfo<BaseShelfVo>> productListNotInCategory(AdmProductNotInCategroyDto admProductNotInCategroyDto) {
                log.info("productListNotInCategory current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult clearCategoryProduct(DeleteCategoryDto deleteCategoryDto) {
                log.info("clearCategoryProduct current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult deleteOneCategoryProduct(DeleteOneCategoryProductDto deleteOneCategoryProductDto) {
                log.info("deleteOneCategoryProduct current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchDeleteCategoryProduct(List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList) {
                log.info("batchDeleteCategoryProduct current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult batchAddCategoryProduct(List<AddCategoryProduct> addCategoryProductList) {
                log.info("batchAddCategoryProduct current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<CategoryVo> editCategoryPage(AdmEditCategoryPageDto admEditCategoryPageDto) {
                log.info("editCategoryPage current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CategoryVo>> admTheFirstCategroy() {
                log.info("admTheFirstCategroy current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<CategoryVo>> admTheNextCategroy(AdmEditCategoryPageDto admEditCategoryPageDto) {
                log.info("admTheNextCategroy current exception is {}", throwable);
                return ApiResult.serviceFail();
            }

            @Override
            public ApiResult<List<Map<String, Object>>> getCategoryTree() {
                log.info("getCategoryTree current exception is {}", throwable);
                return ApiResult.serviceFail();
            }
        };
    }
}
