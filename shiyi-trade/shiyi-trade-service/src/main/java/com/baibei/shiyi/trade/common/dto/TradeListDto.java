package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/23 5:55 PM
 * @description:
 */
@Data
public class
TradeListDto extends CustomerBaseDto {
    // 商品交易商编码
    @NotBlank(message = "商品交易编码不能为空")
    private String productTradeNo;

    // 价格
    @NotBlank(message = "价格不能为空")
    private String price;

    // 数量
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer count;

    // 方向,buy=买入，sell=卖出
    @NotBlank(message = "方向不能为空")
    private String direction;

    // 是否匿名，1=匿名，0=不匿名
    private Integer anonymousFlag;
}
