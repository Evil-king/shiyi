package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TradeProductListVo {

    private long id;

    private String productTradeNo;

    private String productName;

    private BigDecimal issuePrice;//发行价

    private BigDecimal exchangePrice;//兑换价

    private String marketTime;//上市日期

    private String tradeStatus;//交易状态

    private String creator;//创建人

    private String modifier;//执行人

    private String createTime;

    private String modifyTime;
}
