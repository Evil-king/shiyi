package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/10/17 13:58
 * @description:
 */
@Data
public class ChangePasswordDto {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
    /**
     * 重复密码
     */
    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;
}
