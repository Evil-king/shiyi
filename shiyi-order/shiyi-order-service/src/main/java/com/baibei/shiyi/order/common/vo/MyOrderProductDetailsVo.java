package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 16:28
 * @description:
 */
@Data
public class MyOrderProductDetailsVo {

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    private String skuProperty;

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 数量
     */
    private Integer quantity;


    private Long shelfId;

    private Long skuId;

    private BigDecimal sendIntegral;

    private String shelfType;

    private String shelfTypeDesc;

}