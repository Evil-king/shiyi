package com.baibei.shiyi.user.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/12/4 20:09
 * @description:
 */
@Data
public class BankNodeDto extends CustomerNoDto {
    @NotBlank(message = "银行卡不能为空")
    private String bankCard;
    private String city;
    private String province;
    private String key;
    //当前页数
    @NotBlank(message = "当前页不能为空")
    private String currentPage;
}
