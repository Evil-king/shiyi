package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.AdmCategoryProductDto;
import com.baibei.shiyi.product.feign.bean.dto.AdmProductNotInCategroyDto;
import com.baibei.shiyi.product.feign.bean.dto.CategoryDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseIndexProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryProductVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.model.Category;

import java.util.List;
import java.util.Map;

public interface CategoryMapper extends MyMapper<Category> {
    List<CategoryVo> selectFirstCategorys();

    List<CategoryVo> selectFirstIndexCategorys(Map parm);

    List<CategoryVo> selectIndexCategorysByPid(CategoryDto categoryDto);

    List<CategoryProductVo> selectIndexCategoryProducts(CategoryDto categoryDto);

    List<BaseShelfVo> selectAdmCategoryProductList(Long categoryId);

    List<BaseShelfVo> selectProductListNotInCategory(AdmProductNotInCategroyDto admProductNotInCategroyDto);
}