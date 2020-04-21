package com.baibei.shiyi.user.feign.bean.dto;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/28 10:52
 * @description:
 */
@Data
public class ChangeUserDto {
    private String mobile;
    private String orgCode;
}
