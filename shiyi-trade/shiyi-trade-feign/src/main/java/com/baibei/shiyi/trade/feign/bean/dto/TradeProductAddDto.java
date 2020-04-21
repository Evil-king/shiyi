package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TradeProductAddDto {

    private long id;

    private List<TradeProductAddDto.ImageDta> icon;

    private List<TradeProductAddDto.ImageDta> slideUrl;

    private List<TradeProductAddDto.ImageDta> detailsUrl;

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

    private String creator;//创建人

    private String modifier;//执行人


    @Data
    public static class ImageDta{
        private String url;
    }
}
