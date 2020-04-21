package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname RealnameVerificationDto
 * @Description 储蓄卡验证dto
 * @Date 2019/11/28 14:24
 * @Created by Longer
 */
@Data
public class BankCardVerificationDto{
    /**
     * 银行卡号
     */
    @NotBlank(message = "银行卡号不能为空")
    private String cardNo;
}
