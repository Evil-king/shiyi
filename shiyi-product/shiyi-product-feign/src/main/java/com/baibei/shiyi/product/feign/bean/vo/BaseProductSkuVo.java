package com.baibei.shiyi.product.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Classname BaseProductSkuVo
 * @Description 商品规格列表dto
 * @Date 2019/9/2 18:09
 * @Created by Longer
 */
@Data
public class BaseProductSkuVo {

    /**
     * 商品Id
     */
    private Long productId;
    /**
     * 商品货号
     */
    private String spuNo;
    /**
     * 上架属性规格id
     */
    private Long skuId;

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
