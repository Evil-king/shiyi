package com.baibei.shiyi.cash.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname CashChangeAmountDto
 * @Description TODO
 * @Date 2019/11/6 18:20
 * @Created by Longer
 */
@Data
public class CashChangeAmountDto {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
    @NotNull(message = "变动资金不能为空")
    private BigDecimal changeAmount;
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    @NotNull(message = "交易类型不能为空")
    private String tradeType;
    //类型：out：解冻(支出)，in：冻结（收入）
    @NotNull(message = "类型不能为空")
    private String reType;

    @Override
    public String toString() {
        return "ChangeAmountDto{" +
                "customerNo='" + customerNo + '\'' +
                ", changeAmount=" + changeAmount +
                ", orderNo='" + orderNo + '\'' +
                ", tradeType=" + tradeType +
                ", reType=" + reType +
                '}';
    }
}
