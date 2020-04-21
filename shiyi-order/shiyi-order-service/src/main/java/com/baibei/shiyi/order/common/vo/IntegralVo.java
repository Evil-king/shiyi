package com.baibei.shiyi.order.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/6 11:03
 * @description:
 */
@Data
public class IntegralVo {
    // 积分类型
    private Long integralType;
    // 订单服务这边的积分类型
    private String orderIntegralType;
    // 积分余额
    private BigDecimal integralBalance = BigDecimal.ZERO;
    // 积分抵扣值
    private BigDecimal integralDeduct = BigDecimal.ZERO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegralVo that = (IntegralVo) o;
        return Objects.equals(integralType, that.integralType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(integralType);
    }
}