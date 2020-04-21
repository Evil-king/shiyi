package com.baibei.shiyi.order.common.vo;

import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 16:24
 * @description:
 */
@Data
public class MyOrderDetailsVo {
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 关联客户编码
     */
    private String customerNo;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单实付金额
     */
    private BigDecimal actualAmount;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 订单状态，init=订单初始化；wait=待支付；undelivery=待发货（已支付）；pay_fail=支付失败；deliveryed=已发货；cancel=已取消；completed=已完成
     */
    private String status;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 退款状态中文表达
     */
    private String refundStatusText;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 详细地址
     */
    private String detailsAddress;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单取消时间
     */
    private Date cancelTime;

    /**
     * 状态描述
     */
    private String statusText;

    /**
     * 物流公司(配送方式)
     */
    private String deliveryCompany;

    /**
     * 物流单号
     */
    private String deliveryNo;

    /**
     * 订单完成时间
     */
    private Date completedTime;

    /**
     * 售后单状态
     */
    private String afterSaleOrderStatus;

    /**
     * 售后订单描述
     */
    private String afterSaleOrderStatusRemark;

    private List<MyOrderProductDetailsVo> productList;

    private Long autoCancelTime;

    private List<KeyValue> payList;

    /**
     * 支付方式。money=现金余额；mallAccount=商城账户；consumption=消费积分
     */
    private String payWay;
    /**
     * 支付方式描述。money=现金余额；mallAccount=商城账户；consumption=消费积分
     */
    private String payWayText;

    public String getPayWayText() {
        String result = "";
        if (Constants.BeanType.MONEY.equals(getPayWay())) {
            result = "现金余额";
        }
        if (Constants.BeanType.MALLACCOUNT.equals(getPayWay())) {
            result = "商城账户";
        }
        if (Constants.BeanType.CONSUMPTION.equals(getPayWay())) {
            result = "消费积分";
        }
        return result;
    }
}