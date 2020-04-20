package com.baibei.shiyi.admin.modules.account.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户余额管理
 */
@Data
public class AccountBalanceVo extends BaseRowModel {

    private Long id;

    /**
     * 客户编号
     */
    @ExcelProperty(value = "用户编号", index = 0)
    private String customerNo;

    @ExcelProperty(value = "手机号码", index = 1)
    private String phone;

    /**
     * 创建人
     */
    @ExcelProperty(value = "创建人", index = 2)
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 3, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


    /**
     * 余额类型
     */
    private String balanceType;

    @ExcelProperty(value = "余额类型", index = 4)
    private String balanceTypeText;

    /**
     * 状态
     */
    private String status;

    @ExcelProperty(value = "状态", index = 5)
    private String statusText;

    /**
     * 余额
     */
    @ExcelProperty(value = "余额",index = 6)
    private BigDecimal balance;

    /**
     * 执行时间
     */
    @ExcelProperty(value = "执行时间", index = 7, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date executionTime;

    /**
     * 执行人
     */
    @ExcelProperty(value = "执行人", index = 8)
    private String executeBy;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 9)
    private String remark;


    /**
     * 批次号
     */
    private String batchNo;
}
