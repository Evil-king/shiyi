package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/29 15:32
 * @description:
 */
@Data
public class IntegralDetailsVo {
    private Long shelfId;

    private Long skuId;

    private String orderIntegralType;

    private BigDecimal integralBalance;

    private BigDecimal integralDeduct = BigDecimal.ZERO;

}