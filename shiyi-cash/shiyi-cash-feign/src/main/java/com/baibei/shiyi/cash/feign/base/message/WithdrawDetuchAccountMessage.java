package com.baibei.shiyi.cash.feign.base.message;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Classname WithdrawDetuchAccountMessage
 * @Description 出金，扣减用户余额消息体或回调消息体
 * @Date 2019/11/1 18:03
 * @Created by Longer
 */
@Data
@ToString
public class WithdrawDetuchAccountMessage {
    private String customerNo;
    /**
     * 出金订单号
     */
    private String orderNo;

    /**
     * 出金金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 订单类型（3=调账）
     */
    private String orderType;

    /**
     * success=扣款成功；fail=扣款失败
     */
    String result;

}
