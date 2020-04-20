package com.baibei.shiyi.admin.modules.account.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceErrorVo extends BaseRowModel {

    @ExcelProperty(index = 0, value = "批次号")
    private String batchNO;

    @ExcelProperty(index = 1, value = "用户编码")
    private String customerNo;

    @ExcelProperty(index = 2, value = "手机号码")
    private String phone;

    private String balanceType;

    @ExcelProperty(index = 3, value = "类型")
    private String balanceTypeText;

    @ExcelProperty(index = 4, value = "余额")
    private BigDecimal balance;

    @ExcelProperty(index = 5, value = "原因")
    private String errorMsg;

    private Long id;
}
