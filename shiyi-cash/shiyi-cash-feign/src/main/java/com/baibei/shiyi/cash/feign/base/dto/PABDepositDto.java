package com.baibei.shiyi.cash.feign.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class PABDepositDto {

    /**
     * 入金金额
     */
    @NotNull
    @Range(min = 0, max = 999999999)
    private BigDecimal tranAmount;

    /**
     * 入金账号
     */
    @NotNull
    @Size(max = 32)
    private String inAcctId;


    /**
     * 入金账号名称
     */
    @NotNull
    @Size(max = 120)
    private String inAcctIdName;

    /**
     * 币种
     */
    @NotNull
    @Size(max = 3, min = 3)
    private String ccyCode;

    /**
     * 会计日期
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date acctDate;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 会员子账号
     */
    @NotNull
    @Size(max = 32)
    private String custAcctId;


    /**
     * 银行外部流水号
     */
    @NotNull(message = "银行外部流水号不能为空")
    private String externalNo;

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 资金汇总账号
     */
    private String supAcctId;
}
