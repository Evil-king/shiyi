package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.model.ShelfCategoryRef;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShelfCategoryRefMapper extends MyMapper<ShelfCategoryRef> {

    List<CategoryVo> findByCategory(@Param("shelfId") Long shelfId);
}