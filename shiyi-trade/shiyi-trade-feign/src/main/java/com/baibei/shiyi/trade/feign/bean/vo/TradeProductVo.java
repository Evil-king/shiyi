package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class TradeProductVo {

    private long id;

    private List<TradeProductVo.ImageDta> icon;

    private List<TradeProductVo.ImageDta> slideUrl;

    private List<TradeProductVo.ImageDta> detailsUrl;

    private String productTradeNo;

    private String productName;

    private BigDecimal issuePrice;

    private BigDecimal exchangePrice;

    private int minTrade;

    private int maxTrade;

    private BigDecimal highestQuotedPrice;

    private BigDecimal lowestQuotedPrice;

    private String unit;

    private String marketTime;

    private String status;

    private String tradeStatus;

    @Data
    public static class ImageDta{
        private String url;
    }
}
