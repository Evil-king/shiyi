package com.baibei.shiyi.order.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 10:20
 * @description:
 */
@Data
public class CartDeleteDto {
    @NotBlank(message = "ID不能为空")
    private String ids;
}