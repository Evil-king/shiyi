package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/12 14:51
 * @description:
 */
@Data
public class OperationDto {
    @NotEmpty(message = "请选择操作的订单")
    private List<Long> ids;
    @NotNull(message = "状态不能为空")
    private String status;
    //操作人用户名
    private String operatorName;
}
