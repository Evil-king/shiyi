package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;

@Data
public class SignInBackVo {

    /**
     * 前置流水号
     */
    private String frontLogNo;

    /**
     * 保留域
     */
    private String reserve;

}
