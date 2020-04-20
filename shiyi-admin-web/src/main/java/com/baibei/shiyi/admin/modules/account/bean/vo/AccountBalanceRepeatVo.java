package com.baibei.shiyi.admin.modules.account.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceRepeatVo extends BaseRowModel {

    private Long id;

    @ExcelProperty(index = 0, value = "批次号")
    private String batchNo;

    @ExcelProperty(index = 1, value = "用户编号")
    private String customerNo;

    @ExcelProperty(index = 2, value = "手机号码")
    private String phone;

    @ExcelProperty(index = 3, value = "创建人")
    private String createBy;

    @ExcelProperty(index = 4, value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String createTime;

    private String balanceType;

    @ExcelProperty(index = 5, value = "类型")
    private String balanceTypeText;

    private String status;

    @ExcelProperty(index = 6, value = "状态")
    private String statusText;

    @ExcelProperty(index = 7, value = "余额")
    private BigDecimal balance;


    @ExcelProperty(index = 8, value = "执行时间", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String executionTime;

    @ExcelProperty(index = 9, value = "执行人")
    private String executionBy;


}
