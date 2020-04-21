package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * api寄货入参
 */
@Data
public class ShippingDto extends CustomerBaseDto {
    @NotNull(message = "子订单编号不能为空")
    private String orderItemNo;

    @NotNull(message = "物流单号不能为空")
    private String sendLogisticsNo;

    @NotNull(message = "物流公司号不能为空")
    private String sendLogisticsName;
}
