package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ListBuyVo {

    private long id;

    private BigDecimal price;//价格

    private int num;//数量

    private String customerNo;//挂牌方
}
