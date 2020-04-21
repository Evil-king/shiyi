package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname AdmProductShelfSkuDto
 * @Description 商品上下架商品规格dto
 * @Date 2019/9/2 18:09
 * @Created by Longer
 */
@Data
public class AdmProductShelfSkuVo {

    /**
     * 上架属性规格id
     */
    private Long skuId;
    /**
     * 上架价格
     */
    private BigDecimal shelfPrice;

    private String skuProperty;

    private List<BaseKeyValueVo> skuPropertyList = new ArrayList<>();

    /**
     * 属性规格编码
     */
    private String skuNo;

    /**
     * 库存
     */
    private BigDecimal stock;
}
