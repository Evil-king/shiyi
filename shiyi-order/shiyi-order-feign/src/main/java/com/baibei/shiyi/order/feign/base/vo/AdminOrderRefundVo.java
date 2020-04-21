package com.baibei.shiyi.order.feign.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AdminOrderRefundVo {

    /**
     * 退款订单Id
     */
    private Long refundId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 子订单编号
     */
    private String orderItemNo;

    /**
     * 商品详情
     */
    private String productName;

    /**
     * 商品单价
     */
    private BigDecimal amount;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 积分抵扣金额
     */
    private BigDecimal integrationAmount;


    /**
     * 实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;

    /**
     * 退款处理状态，doing=退款处理中，success=退款处理成功，fail=退款处理失败
     */
    private String refundStatus;

    /**
     * 退款文本
     */
    private String refundStatusText;

    /**
     * 审核状态 ，wait=待审核，pass=审核通过，reject=审核拒绝',
     */
    private String reviewStatus;
}
