package com.baibei.shiyi.account.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/11/14 19:54
 * @description:
 */
@Data
public class CustomerNoPageDto extends PageParam {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
}
