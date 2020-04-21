package com.baibei.shiyi.product.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname InitStockDto
 * @Description 初始化库存dto
 * @Date 2019/9/18 11:17
 * @Created by Longer
 */
@Data
public class InitStockDto {

    /**
     * 关联商品sku
     */
    private Long skuId;

    /**
     * 关联商品id
     */
    private Long productId;

    /**
     * 商品货号
     */
    private String spuNo;

    /**
     * 库存
     */
    private BigDecimal stock;

    /**
     * 库存单位
     */
    private String unit;

    /**
     * 销量
     */
    private BigDecimal sellCount;

    /**
     * 执行者编码
     */
    private String operatorNo;

    /**
     * 备注
     */
    private String remark;
}
