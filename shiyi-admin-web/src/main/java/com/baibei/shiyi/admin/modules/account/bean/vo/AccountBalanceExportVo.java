package com.baibei.shiyi.admin.modules.account.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalanceExportVo extends BaseRowModel {


    /**
     * 客户编号
     */
    @ExcelProperty(value = "用户编号", index = 0)
    private String customerNo;

    @ExcelProperty(value = "手机号码", index = 1)
    private String phone;

    @ExcelProperty(value = "类型", index = 2)
    private String balanceTypeText;


    @ExcelProperty(value = "余额", index = 3)
    private BigDecimal balance;

    private String batchNo;

    private String createBy;

    private String balanceType;

    private String executeBy;

    private String remark;

}
