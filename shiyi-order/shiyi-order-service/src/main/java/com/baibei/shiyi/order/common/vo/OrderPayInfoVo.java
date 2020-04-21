package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 14:45
 * @description:
 */
@Data
public class OrderPayInfoVo {
    /**
     * 地址ID
     */
    private Long addressId;

    /**
     * 收货人姓名
     */
    private String receivingName;

    /**
     * 收货手机号
     */
    private String receivingMobile;

    /**
     * 详细地址
     */
    private String detailsAddress;

    /**
     * 支付方式以及支付金额
     */
    private List<KeyValue> payList;

    // 运费
    private BigDecimal freightAmount;

    /**
     * 商城账户余额
     */
    private BigDecimal mallBalance;

    /**
     * 是否显示（show=显示；hide=隐藏）
     */
    private String showMallBalance="hide";
}