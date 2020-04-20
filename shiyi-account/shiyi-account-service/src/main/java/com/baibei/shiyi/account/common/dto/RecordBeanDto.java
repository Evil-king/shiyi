package com.baibei.shiyi.account.common.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/10/30 19:23
 * @description:
 */
@Data
public class RecordBeanDto extends RecordDto {
    //consumption:消费 exchange:兑换，shiyi:屹家无忧
    @NotNull(message = "积分类型不能为空")
    private String beanType;
}
