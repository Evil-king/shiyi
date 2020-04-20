package com.baibei.shiyi.cash.feign.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 1312接口dto
 */
@Data
public class WithdrawForBank1312Dto {
    /**
     * 完整报文
     */
    @NotBlank(message = "报文不能为空")
   private String message;

}
