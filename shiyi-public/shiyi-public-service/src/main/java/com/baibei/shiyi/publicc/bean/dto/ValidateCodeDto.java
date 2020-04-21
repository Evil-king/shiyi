package com.baibei.shiyi.publicc.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hwq
 * @date 2019/05/28
 */
@Data
public class ValidateCodeDto {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;
}
