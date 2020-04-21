package com.baibei.shiyi.user.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/7/31 9:22
 * @description:
 */
@Data
public class MobileLoginDto {
    /**
     * 手机号
     */
    @NotBlank(message = "手机号码不能为空")
    private String mobile;
    /**
     * 手机验证码
     */
    @NotBlank(message = "手机验证码不能为空")
    private String verificationCode;
}
