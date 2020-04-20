package com.baibei.shiyi.account.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/29 17:22
 * @description:
 */
@Data
public class FundInformationVo {
    /**
     * 总资产(客户资金+持仓市值)
     */
    private BigDecimal totalAssets;
    /**
     * 客户资金（可用资金+冻结资金）
     */
    private BigDecimal totalBalance;
    /**
     * 可用资金
     */
    private BigDecimal balance;
    /**
     * 可提现金额
     */
    private BigDecimal withdrawableCash;
    /**
     * 持仓市值
     */
    private BigDecimal holdMarketValue;
    /**
     * 持仓盈亏
     */
    private BigDecimal floatProfitAndLoss;
}
