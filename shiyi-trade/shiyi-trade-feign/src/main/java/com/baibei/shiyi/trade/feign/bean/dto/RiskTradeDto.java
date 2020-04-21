package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RiskTradeDto {

    /**
     * 开启开关
     */
    @NotNull(message = "交易开关参数不能为空")
    private String openFlag;

    /**
     * 创建人
     */
    private String creator;

}
