package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.model.ProductImg;

public interface ProductImgMapper extends MyMapper<ProductImg> {
    void softDeleteByProductId(Long productId);
}