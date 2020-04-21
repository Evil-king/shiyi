package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiAfterSaleDetailsDto extends CustomerBaseDto {
    @NotNull(message = "子订单编号不能为空")
    private String orderItemNo;
    @NotNull(message = "售后类型不能为空")
    private String type;
    private String signer;//details
}
