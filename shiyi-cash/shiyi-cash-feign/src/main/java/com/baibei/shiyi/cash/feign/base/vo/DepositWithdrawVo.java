package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositWithdrawVo {

    /**
     * 发生日期
     */
    private String initDate;

    /**
     * 银行流水号
     */
    private String orderNo;

    /**
     * 出入金类型
     */
    private String type;

    /**
     * 发生金额
     */
    private BigDecimal occurAmount;

    /**
     * 资金账号
     */
    private String exchangeFundAccount;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 备注
     */
    private String remark;

}
