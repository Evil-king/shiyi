package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductSkuVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseProductSkuVo;
import com.baibei.shiyi.product.model.ProductSku;

import java.util.List;

public interface ProductSkuMapper extends MyMapper<ProductSku> {
    void softDelete(Long productId);

    List<AdmEditProductSkuVo> selectSkuStockList(Long productId);

    List<BaseProductSkuVo> selectProductSkuList(String spuNo);
}