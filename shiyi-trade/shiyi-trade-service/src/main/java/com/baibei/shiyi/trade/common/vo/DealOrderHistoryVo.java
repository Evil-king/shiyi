package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DealOrderHistoryVo{

    // 成交单号
//    private String dealNo;
    // 商品名称
//    private String productName;

    // 商品编码
    private String productTradeNo;

    // 交易方向
//    private String type;

    // 价格
    private BigDecimal price;

    // 手续费
//    private BigDecimal fee;

    // 成交数量
    private Integer count;


    // 成交时间
    private Date createTime;

    //成交总额
//    private BigDecimal countPrice;
}
