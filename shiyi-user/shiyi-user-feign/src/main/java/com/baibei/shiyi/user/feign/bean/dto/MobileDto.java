package com.baibei.shiyi.user.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author: hyc
 * @date: 2019/5/29 9:52
 * @description:
 */
@Data
public class MobileDto implements Serializable {
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    /**
     * 1：注册
     */
    @NotBlank(message = "短信类型不能为空")
    private String type;

    private String smsType="1";//短信模板
}
