package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TradeProductDto extends PageParam {

    private String productName;

    private String productTradeNo;

    private String tradeStatus;
}
