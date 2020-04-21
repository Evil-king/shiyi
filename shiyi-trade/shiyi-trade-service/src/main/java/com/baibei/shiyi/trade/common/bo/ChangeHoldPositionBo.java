package com.baibei.shiyi.trade.common.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 15:07
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeHoldPositionBo {
    // 客户编码
    private String customerNo;
    // 交易商品编码
    private String productTradeNo;
    // 变动数量
    private Integer count;
    // 价格
    private BigDecimal price;
    // 来源，HoldResourceEnum枚举
    private String resource;
    // 来源相关的订单号
    private String resourceNo;
    // 备注
    private String remark;
    // 收支，in=收入；out=支出
    private String reType;

    private String customerName;

    private Date  tradeTime;
}