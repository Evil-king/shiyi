package com.baibei.shiyi.user.feign.bean.dto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
/**
 * @author: hyc
 * @date: 2019/5/27 14:36
 * @description:
 */
@Data
public class RegisterDto implements Serializable {
    //手机号
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    //验证码
    @NotBlank(message = "验证码不能为空")
    private String mobileVerificationCode;
    //邀请码
    private String invitationCode;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
    //重复密码
    @NotBlank(message = "重复密码不能为空")
    private String repeatPassword;
    /**
     * 注册来源，默认是自己方注册
     */
    private String registerSource="register";

}
