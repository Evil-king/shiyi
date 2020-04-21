package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.vo.ProductCollectVo;
import com.baibei.shiyi.product.model.ProductCollect;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCollectMapper extends MyMapper<ProductCollect> {

    List<ProductCollectVo> findByCustomerNo(@Param("customerNo") String customerNo);
}