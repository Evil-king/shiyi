package com.baibei.shiyi.account.common.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2020/1/15 13:41
 * @description:
 */
@Data
public class AccountDetailVo {
    /**
     * 可用资金
     */
    private BigDecimal balance;
    /**
     * 可提现金额
     */
    private BigDecimal withdrawableCash;
    /**
     * 总收入
     */
    private BigDecimal totalIn;
    /**
     * 总支出
     */
    private BigDecimal totalOut;
    /**
     * 总入金
     */
    private BigDecimal totalRecharge;
    /**
     * 总出金
     */
    private BigDecimal totalWithdraw;
    /**
     * 冻结金额
     */
    private BigDecimal freezingAmount;
}

