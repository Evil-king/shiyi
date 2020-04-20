package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 11:12
 * @description:
 */
@Data
public class CheckByFundTypes {
    @NotNull(message = "用户编号不能为空")
    private String customerNo;
    @NotEmpty(message = "类型以及判断金额不能为空")
    private List<CheckFundType> checkFundTypes;
}
