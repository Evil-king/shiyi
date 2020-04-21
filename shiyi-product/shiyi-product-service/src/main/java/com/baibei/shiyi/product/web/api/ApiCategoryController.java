package com.baibei.shiyi.product.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.CategoryDto;
import com.baibei.shiyi.product.feign.bean.vo.CategoryProductVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname ApiGroupProductController
 * @Description 商品组相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/api/product/category")
public class ApiCategoryController {
    @Autowired
    private ICategoryService categoryService;

    /**
     * 顶级类目集合
     * @return
     */
    @PostMapping("/firstCategorys")
    public ApiResult<List<CategoryVo>> firstCategorys() {
        return ApiResult.success(categoryService.getTheFirstIndexCategorys());
    }

    /**
     * 类目分页列表
     * @param categoryDto
     * @return
     */
    @PostMapping("/nextCategorys")
    public ApiResult<MyPageInfo<List<CategoryVo>>> pageList(@Validated @RequestBody CategoryDto categoryDto) {
        return ApiResult.success(categoryService.getNextCategory(categoryDto));
    }

    /**
     * 类目下的商品集合
     * @param categoryDto
     * @return
     */
    @PostMapping("/indexCategoryProducts")
    public ApiResult<MyPageInfo<List<CategoryProductVo>>> indexCategoryProducts(@Validated @RequestBody CategoryDto categoryDto) {
        return ApiResult.success(categoryService.getIndexCategoryProducts(categoryDto));
    }
}
