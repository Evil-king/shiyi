package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname RealnameVerificationDto
 * @Description 实名认证dto
 * @Date 2019/11/28 14:24
 * @Created by Longer
 */
@Data
public class RealnameVerificationDto extends CustomerBaseDto {
    /**
     * 真实姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String realname;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idcard;

    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    private String bankCard;

    /**
     * 银行卡预留手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String bankMobile;
}
