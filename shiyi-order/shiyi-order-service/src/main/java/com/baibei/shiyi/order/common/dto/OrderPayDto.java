package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/7 15:05
 * @description:
 */
@Data
public class OrderPayDto extends CustomerBaseDto {
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
}