package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.AdmProductDto;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.model.Product;

import java.util.List;

public interface ProductMapper extends MyMapper<Product> {
    List<AdmProductVo> selectList(AdmProductDto admProductDto);

    AdmEditProductVo selectEditProductInfo(Long productId);
}