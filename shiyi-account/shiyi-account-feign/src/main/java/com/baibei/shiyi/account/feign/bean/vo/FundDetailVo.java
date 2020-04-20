package com.baibei.shiyi.account.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/30 10:23
 * @description:
 */
@Data
public class FundDetailVo extends FundInformationVo{
    /**
     * 交易商编号
     */
    private String customerNo;
    /**
     * 期初资金（上一个交易日的日结资金，日结时等于可提现金额）
     */
    private BigDecimal initFund;
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
