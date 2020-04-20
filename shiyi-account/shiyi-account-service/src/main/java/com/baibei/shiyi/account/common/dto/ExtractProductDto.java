package com.baibei.shiyi.account.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/11/11 20:10
 * @description:
 */
@Data
public class ExtractProductDto extends CustomerNoDto {
    @NotNull(message = "提取数量不能为空")
    private String number;
}
