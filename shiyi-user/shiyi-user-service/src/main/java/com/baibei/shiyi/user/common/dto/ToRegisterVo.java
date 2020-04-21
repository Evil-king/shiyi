package com.baibei.shiyi.user.common.dto;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/29 11:37
 * @description:
 */
@Data
public class ToRegisterVo {
    /**
     * 邀请码是否必填 1:必填 0：选填
     */
    private String isMust;
}
