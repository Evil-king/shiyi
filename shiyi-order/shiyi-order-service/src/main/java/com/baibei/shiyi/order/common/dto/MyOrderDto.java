package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 11:33
 * @description:
 */
@Data
public class MyOrderDto extends CustomerBaseAndPageDto {
    @NotBlank(message = "状态不能为空")
    private String status;
}