package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiAfterSalePageListDto extends PageParam {
    @NotNull(message = "用户编号不能为空")
    private String customerNo;
}
