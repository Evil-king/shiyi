package com.baibei.shiyi.order.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_ord_order_product")
@Data
public class OrderProduct {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联订单表主键
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联商品上架ID
     */
    @Column(name = "shelf_id")
    private Long shelfId;

    /**
     * 关联商品规格ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 商品主图
     */
    @Column(name = "product_img")
    private String productImg;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品）
     */
    @Column(name = "shelf_type")
    private String shelfType;


    /**
     * 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    @Column(name = "sku_property")
    private String skuProperty;

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总价
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;


    /**
     * 支付方式，money=现金支付；consumption=消费积分支付
     */
    @Column(name = "pay_way")
    private String payWay;

    /**
     * 赠送积分
     */
    @Column(name = "send_integral_json")
    private String sendIntegralJson;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 状态(1:正常，0:删除)
     */
    private Byte flag;

}