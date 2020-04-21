package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.model.ProductStock;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface ProductStockMapper extends MyMapper<ProductStock> {
    int detuchStock(ChangeStockDto changeStockDto);

    Long countHotProduct();

    void softDelete(Long productId);

    /**
     * 修改库存
     * @param productId
     * @param skuId
     * @param changeCount 更改数量（可为负数）
     * @return
     */
    int changeStock(@Param("productId") Long productId,@Param("skuId") Long skuId,
                    @Param("changeCount") BigDecimal changeCount,@Param("sellCount") BigDecimal sellCount
                    );

    int changeSellCount(@Param("productId") Long productId,@Param("skuId") Long skuId,
                      @Param("changeAmount") BigDecimal changeAmount);
}