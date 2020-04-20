package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/11/1 19:39
 * @description:
 */
@Data
public class AdminRecordBeanDto extends AdminRecordDto{
    @NotNull(message = "积分类型不能为空")
    private String beanType;
}
