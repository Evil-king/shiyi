package com.baibei.shiyi.cash.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Apply1010Dto {

    /**
     * 用户编码
     */
    private String customerNo;

    @NotNull(message = "查询标志不能为空")
    private String selectFlag;
}



