package com.baibei.shiyi.user.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/12/25 10:38
 * @description:
 */
@Data
public class ExternalRegisterDto {
    /**
     * 注册手机号
     */
    @NotBlank(message = "注册手机号不能为空")
    private String mobile;
    /**
     * 推荐人手机号
     */
    @NotBlank(message = "推荐人手机号不能为空")
    private String recommenderMobile;

    /**
     * 推荐人手机号
     */
    @NotBlank(message = "注册来源不能为空")
    private String registerSource;
}
