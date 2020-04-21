package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/2 10:30
 * @description:
 */
@Data
public class TradeInfoDto extends CustomerBaseDto {
    @NotBlank(message = "商品编码不能为空")
    private String productTradeNo;
    // buy=挂牌买入；sell=挂牌卖出
    @NotBlank(message = "方向不能为空")
    private String direction;
}