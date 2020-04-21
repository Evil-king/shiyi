package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 10:11
 * @description:
 */
@Data
public class OrderCancelDto extends CustomerBaseDto {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    private String reason;
}