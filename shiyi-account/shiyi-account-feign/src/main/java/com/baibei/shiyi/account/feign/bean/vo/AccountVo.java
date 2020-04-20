package com.baibei.shiyi.account.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/28 19:43
 * @description:
 */
@Data
public class AccountVo {
    private String customerNo;
    //余额
    private BigDecimal balance=BigDecimal.ZERO;
    //冻结资金
    private BigDecimal freezingAmount=BigDecimal.ZERO;
    //可提现金额
    private BigDecimal withdrawableCash=BigDecimal.ZERO;
    //总资金
    private BigDecimal totalBalance=BigDecimal.ZERO;
    private String fundPassword;
}
