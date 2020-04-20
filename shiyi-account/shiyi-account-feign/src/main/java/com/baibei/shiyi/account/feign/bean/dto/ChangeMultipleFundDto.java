package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/4 13:38
 * @description:
 */
@Data
public class ChangeMultipleFundDto {
    /**
     * 订单号
     */
    @NotNull(message = "订单号不能为空")
    private String orderNo;
    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private String customerNo;
    @NotEmpty(message = "多个操作资金类型不能为空")
    private List<ChangeMultipleFundType> changeMultipleFundTypeList;
}
