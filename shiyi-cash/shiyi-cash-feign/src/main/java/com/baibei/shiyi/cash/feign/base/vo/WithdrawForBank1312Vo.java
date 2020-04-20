package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;


@Data
public class WithdrawForBank1312Vo {
    // 交易码
    private String TranFunc;

    // 银行流水号
    private String bankExternalNo;

    // 交易网业务报文体
    private String message;
}
