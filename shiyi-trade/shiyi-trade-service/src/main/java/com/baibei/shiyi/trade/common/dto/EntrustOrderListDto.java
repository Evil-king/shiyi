package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:35 PM
 * @description:
 */
@Data
public class EntrustOrderListDto extends CustomerBaseAndPageDto {
    @NotBlank(message = "方向不能为空")
    private String direction;
    @NotBlank(message = "交易商品编码不能为空")
    private String productTradeNo;
}
