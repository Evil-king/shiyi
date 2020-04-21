package com.baibei.shiyi.user.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/5 9:54
 * @description:
 */
@Data
public class ChangeStatusDto {
    @NotEmpty(message = "用户集合不能为空")
    private List<String> customerNos;
    @NotEmpty(message = "状态不能为空")
    private List<String> customerStatus;
}
