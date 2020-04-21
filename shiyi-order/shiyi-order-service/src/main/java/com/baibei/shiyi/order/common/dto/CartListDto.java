package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 10:24
 * @description:
 */
@Data
public class CartListDto extends CustomerBaseDto {
    /**
     * 上下架（shelf=上架，unshelf=下架）
     */
    @NotBlank(message = "状态不能为空")
    private String status;
}