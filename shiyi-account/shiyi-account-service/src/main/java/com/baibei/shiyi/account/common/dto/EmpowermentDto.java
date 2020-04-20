package com.baibei.shiyi.account.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/11/11 11:33
 * @description:
 */
@Data
public class EmpowermentDto extends CustomerNoDto {
    @NotNull(message = "赋能数量不能为空")
    private String number;
}
