package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 提货订单展示对象
 */
@Data
@ToString(callSuper = true)
public class PickOrderDto extends CustomerBaseDto {


    @NotNull(message = "商品单号不能为空")
    private String spuNo;

    @Min(value = 1, message = "提货数量不能为0")
    @NotNull(message = "提货数量不能为空")
    private Integer pickNumber;

    @NotNull(message = "提货地址不能为空")
    private Long addressId;

}
