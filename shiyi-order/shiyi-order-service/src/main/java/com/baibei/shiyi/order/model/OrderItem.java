package com.baibei.shiyi.order.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ord_order_item")
@Data
public class OrderItem {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 子订单编号
     */
    @Column(name = "order_item_no")
    private String orderItemNo;

    /**
     * 关联订单表主键
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联客户编码
     */
    @Column(name = "customer_no")
    private String customerNo;

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
     * 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    @Column(name = "sku_property")
    private String skuProperty;

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
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    @Column(name = "shelf_type")
    private String shelfType;

    /**
     * 单价
     */
    private BigDecimal amount;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 订单实付金额
     */
    @Column(name = "actual_amount")
    private BigDecimal actualAmount;

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
     * 订单状态，cancel=已取消；undelivery=待发货；deliveryed=待收货；completed=已完成；apply_refund=退款申请；refunded=已退款
     */
    private String status;

    /**
     * 订单退款状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     */
    private String refundStatus;

    /**
     * 物流公司(配送方式)
     */
    @Column(name = "delivery_company")
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @Column(name = "delivery_no")
    private String deliveryNo;

    /**
     * 发货时间
     */
    @Column(name = "delivery_time")
    private Date deliveryTime;

    /**
     * 订单完成时间
     */
    @Column(name = "completed_time")
    private Date completedTime;

    /**
     * 订单取消时间
     */
    @Column(name = "cancel_time")
    private Date cancelTime;

    /**
     * 订单自动收货时间
     */
    @Column(name = "auto_delivery_time")
    private Date autoDeliveryTime;

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