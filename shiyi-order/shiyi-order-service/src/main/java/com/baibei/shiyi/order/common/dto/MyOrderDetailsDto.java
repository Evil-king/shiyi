package com.baibei.shiyi.order.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 16:33
 * @description:
 */
@Data
public class MyOrderDetailsDto {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
   /* @NotBlank(message = "订单状态不能为空")
    private String status;*/
}