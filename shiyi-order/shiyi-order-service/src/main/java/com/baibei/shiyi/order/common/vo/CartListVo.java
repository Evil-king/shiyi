package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/1 10:32
 * @description:
 */
@Data
public class CartListVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 关联客户编码
     */
    private String customerNo;

    /**
     * 关联商品上架ID
     */
    private Long shelfId;

    /**
     * 关联商品规格ID
     */
    private Long skuId;

    /**
     * 商品主图
     */
    private String productImg;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品sku规格(json格式。如：{"颜色":"红色","尺码":"25"}
     */
    private String skuProperty;

    /**
     * 加入购物车时的价格
     */
    private BigDecimal amount;

    // 库存
    private BigDecimal stock;

    // 库存告急，默认为false
    private Boolean stockNotify = false;

    // 上下架状态
    private String status;

    private BigDecimal compareAmount=BigDecimal.ZERO;

    private String compareText;

    /**
     * 赠送积分数
     */
    private BigDecimal sendIntegral;

    /**
     * 商品类型（send_integral=赠送积分商品，支付类型：余额；consume_ingtegral=消费积分商品，支付类型：金豆；transfer_product=交割商品，支付类型：红豆）
     */
    private String shelfType;

}