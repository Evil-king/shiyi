package com.baibei.shiyi.cash.feign.base.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 出金
 */
@Data
@ToString(callSuper=true)
public class OrderWithdrawDto extends CustomerBaseDto {

    @NotNull(message = "转出金额不能为空")
    private BigDecimal orderAmt;

    @NotNull(message = "收款账号不能为空")
    private String receiveAccount;

    @NotNull(message = "收款银行不能为空")
    private String bankName;

    @NotNull(message = "收款账户不能为空")
    private String accountName;

    private String password;//资金密码

    private String type;

    private String status;

    private String externalNo;

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
}
