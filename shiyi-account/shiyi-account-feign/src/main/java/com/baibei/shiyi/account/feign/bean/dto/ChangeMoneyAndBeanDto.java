package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/10/31 19:18
 * @description:
 */
@Data
public class ChangeMoneyAndBeanDto {
    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private String customerNo;
    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderNo;
    /**
     * 资金修改金额
     */
    @NotNull(message = "资金变动金额不能为空")
    private BigDecimal moneyChangeAmount;
    /**
     * 资金交易类型
     */
    @NotNull(message = "资金交易类型不能为空")
    private String moneyTradeType;
    /**
     * 资金收支类型（in:收入 out:支出）
     */
    @NotNull(message = "资金收支类型不能为空")
    private String moneyRetype;
    /**
     * 修改多个积分类型
     */
    private List<ChangeBeanDto> changeBeanDtos;


}
