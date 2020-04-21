package com.baibei.shiyi.order.feign.base.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 积分类型
 */
@Data
public class AdminIntegralTypeVo {

    /**
     * 提货积分
     */
    private BigDecimal deliveryIntegral;

    /**
     * 消费积分
     */
    private BigDecimal consumeIntegral;

    /**
     * 屹家报积分
     */
    private BigDecimal yijiabaoIntegral;
}
