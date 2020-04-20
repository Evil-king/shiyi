package com.baibei.shiyi.account.feign.bean.vo;

import lombok.Data;

/**
 * @Classname SumWithdrawAndDepositVo
 * @Description TODO
 * @Date 2019/11/14 21:18
 * @Created by Longer
 */
@Data
public class SumWithdrawAndDepositVo {
    private String customerNo;

    private String amount;

    /**
     * 交易类型（出金、入金）
     */
    private String tradeType;
}
