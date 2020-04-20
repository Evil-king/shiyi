package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/10/29 18:13
 * @description:
 */
@Data
public class ChangeCustomerBeanDto extends ChangeAmountDto{
    @NotBlank(message = "豆类型不能为空")
    private String customerBeanType;
}
