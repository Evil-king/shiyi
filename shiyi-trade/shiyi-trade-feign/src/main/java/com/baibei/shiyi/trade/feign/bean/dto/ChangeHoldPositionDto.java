package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 15:07
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeHoldPositionDto {
    // 客户编码
    @NotBlank(message = "customerNo不能为空")
    private String customerNo;
    // 交易商品编码
    @NotBlank(message = "productTradeNo不能为空")
    private String productTradeNo;
    // 变动数量
    @NotNull(message = "count不能为空")
    private Integer count;
    // 价格
    @NotNull(message = "price不能为空")
    private BigDecimal price;
    // 来源，HoldResourceEnum枚举
    @NotBlank(message = "resource不能为空")
    private String resource;
    // 来源相关的订单号
    @NotBlank(message = "resourceNo不能为空")
    private String resourceNo;
    // 收支，in=收入；out=支出
    @NotBlank(message = "reType不能为空")
    private String reType;
    // 客户名称
    @NotBlank(message = "客户名称不能为空")
    private String customerName;
    // 备注
    private String remark;
}