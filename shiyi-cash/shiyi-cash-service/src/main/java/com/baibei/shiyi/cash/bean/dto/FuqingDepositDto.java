package com.baibei.shiyi.cash.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuqingDepositDto {

    // 银行流水号
    private String serialNo;

    // 交易所代码
    private String exchangeId;

    // 交易网所资金账号
    private String exchangeFundAccount;


    // 银行业务类型
    private String bisinType;

    /**
     * 发生金额
     */
    private BigDecimal occurAmount;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 证件类型
     */
    private String idKind;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 发生日期
     */
    private String initDate;


    private String fuqingTransferReq;

}

