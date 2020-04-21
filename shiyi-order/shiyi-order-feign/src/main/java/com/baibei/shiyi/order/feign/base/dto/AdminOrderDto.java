package com.baibei.shiyi.order.feign.base.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AdminOrderDto extends PageParam {


    private Long orderId;

    private Long orderItemId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户编号
     */
    private String customerNo;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 物流公司
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliveryNo;

    /**
     * 子订单号编号
     */
    private String orderItemNo;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款Id
     */
    private Long refundId;
}
