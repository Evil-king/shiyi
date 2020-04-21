package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PcEntrustListAndCustomerDto extends PageParam {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;

    private String startTime;

    private String endTime;
}
