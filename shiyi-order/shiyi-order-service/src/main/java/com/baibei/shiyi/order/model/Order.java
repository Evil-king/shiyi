package com.baibei.shiyi.order.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_ord_order")
@Data
public class Order {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 关联客户编码
     */
    @Column(name = "customer_no")
    private String customerNo;


    /**
     * 运费金额
     */
    @Column(name = "freight_amount")
    private BigDecimal freightAmount;

    /**
     * 订单状态，init=订单初始化；wait=待支付；undelivery=待发货（已支付）；pay_fail=支付失败；deliveryed=待收货；cancel=已取消；completed=已完成
     */
    private String status;


    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone")
    private String receiverPhone;

    /**
     * 省份/直辖市
     */
    @Column(name = "receiver_province")
    private String receiverProvince;

    /**
     * 城市
     */
    @Column(name = "receiver_city")
    private String receiverCity;

    /**
     * 区
     */
    @Column(name = "receiver_region")
    private String receiverRegion;

    /**
     * 详细地址
     */
    @Column(name = "receiver_detail_address")
    private String receiverDetailAddress;

    /**
     * 订单自动超时时间
     */
    @Column(name = "auto_cancel_time")
    private Date autoCancelTime;


    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单取消时间
     */
    @Column(name = "cancel_time")
    private Date cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason")
    private String cancelReason;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

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