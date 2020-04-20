package com.baibei.shiyi.account.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/10/31 11:23
 * @description:
 */
@Data
public class InsertFundPasswordDto extends CustomerNoDto {
    @NotNull(message = "资金密码不能为空")
    private String password;
}
