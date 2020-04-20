package com.baibei.shiyi.account.common.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/8/1 13:52
 * @description:
 */
@Data
public class RecordDto extends PageParam {
    @NotBlank(message = "交易账号不能为空")
    private String customerNo;
    /**
     * 收入或是支出 out 支出 in 收入
     */
    private String retype;

    private String startTime;
    private String endTime;
}
