package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class
RevokeByDirectionDto extends CustomerBaseDto {
    @NotBlank(message = "方向不能为空")
    private String direction;

    private String productTradeNo;
}
