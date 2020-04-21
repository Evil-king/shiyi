package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/12 17:49
 * @description:
 */
@Data
public class OrderNoDto extends CustomerBaseDto {
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
}