package com.baibei.shiyi.account.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ConsignmentDto extends CustomerBaseDto {
    @NotNull(message = "价格不能为空")
    private BigDecimal price;
    @NotNull(message = "手续费不能为空")
    private BigDecimal fee;
}
