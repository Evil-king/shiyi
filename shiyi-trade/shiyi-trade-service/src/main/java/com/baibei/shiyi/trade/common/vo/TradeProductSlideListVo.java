package com.baibei.shiyi.trade.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class TradeProductSlideListVo {
    private List<TradeProductSlideVo> productSlideVoList;

    private String productName;

}
