package com.baibei.shiyi.order.feign.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 订单详情
 */
@Data
public class AdminOrderDetailsVo {

    /**
     * 子订单编号
     */
    private String orderItemNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品单价
     */
    private BigDecimal amount;


    /**
     * 积分抵扣类型
     */
    private String integralType;
    /**
     * 积分抵扣金额
     */
    private BigDecimal integrationAmount;

    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 订单状态
     */
    private String status;


    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deliveryTime;

    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date completedTime;

    /**
     * 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    private String shelfType;

    /**
     * 支付方式，cash=现金支付；redbean=红豆支付；kingbean=金豆支付
     */
    private String payWay;

}
