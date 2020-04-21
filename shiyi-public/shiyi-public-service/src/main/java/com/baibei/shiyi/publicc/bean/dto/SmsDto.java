package com.baibei.shiyi.publicc.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author hwq
 * @date 2019/05/24
 */
@Data
public class SmsDto implements Serializable {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "短信类型不能为空")
    private String type;

    private String customerNo;
}
