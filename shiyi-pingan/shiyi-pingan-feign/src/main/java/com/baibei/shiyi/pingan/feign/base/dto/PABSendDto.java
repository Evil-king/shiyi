package com.baibei.shiyi.pingan.feign.base.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class PABSendDto {

    /**
     * 交易类型
     */
    @NotNull(message = "交易码不能为空")
    private Integer tranFunc;

    /**
     * 请求消息体
     */
    @NotNull(message = "消息体不能为空")
    private String message;

    /**
     * 交易流水号不能为空,保持唯一性
     */
    @NotNull(message = "交易流水号不能为空")
    @Length(max = 20)
    private String thirdLogNo;

    /***
     * 请求码，预留字段
     */
    private String requestCode;

    /**
     * 请求描述,预留字段
     */
    private String requestMsg;
}
