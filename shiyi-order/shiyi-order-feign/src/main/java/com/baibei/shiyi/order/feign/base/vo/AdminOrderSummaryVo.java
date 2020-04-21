package com.baibei.shiyi.order.feign.base.vo;

import lombok.Data;

/**
 * 订单汇总
 */
@Data
public class AdminOrderSummaryVo {

    /**
     * 订单总数
     */
    private Integer orderCount;

    /**
     * 等待支付订单总数
     */
    private Integer orderWait;

    /**
     * 待发货订单总数
     */
    private Integer orderUndelivery;

    /**
     * 待收货订单总数
     */
    private Integer orderDeliveryed;

    /**
     * 已完成订单总数
     */
    private Integer orderCompleted;

    /**
     * 订单已取消
     */
    private Integer orderCancel;
}
