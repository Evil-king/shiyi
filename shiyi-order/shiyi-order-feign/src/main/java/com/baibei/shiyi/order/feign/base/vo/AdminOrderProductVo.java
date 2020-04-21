package com.baibei.shiyi.order.feign.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品信息
 */
@Data
public class AdminOrderProductVo {

    /**
     * 商品上架Id
     */
    private Long shelfId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品主图
     */
    private String productImage;

    /**
     * 商品仓
     */
    private String module;

    /**
     * 商品仓文本
     */
    private String moduleText;

    /**
     * 商品sku 属性[{"颜色":"红色"},{"尺码":"25"}]
     */
    private String skuProperty;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 总价
     */
    private BigDecimal totalAmount;

    /**
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    private String shelfType;

    /**
     * 支付方式，cash=现金支付；redbean=红豆支付；kingbean=金豆支付
     */
    private String payWay;
}
