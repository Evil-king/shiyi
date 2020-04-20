package com.baibei.shiyi.account.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/1 17:50
 * @description:
 */
@Data
public class AdminRecordVo extends BaseRowModel {
    @ExcelProperty(index = 1, value = "流水号")
    private String recordNo;
    @ExcelProperty(index = 2, value = "手机号")
    private String mobile;
    @ExcelProperty(index = 3, value = "用户编号")
    private String customerNo;
    @ExcelProperty(index = 4, value = "直接推荐")
    private String recommenderId;
    @ExcelProperty(index = 5, value = "收支类型")
    private String retype;
    @ExcelProperty(index = 6, value = "交易类型")
    private String tradeType;
    @ExcelProperty(index = 7, value = "收支金额")
    private BigDecimal changeAmount;
    @ExcelProperty(index = 8, value = "余额")
    private BigDecimal balance;
    @ExcelProperty(index = 9, value = "处理时间")
    private String createTime;
    @ExcelProperty(index = 10, value = "关联订单")
    private String orderNo;
    @ExcelProperty(index = 11, value = "备注")
    private String remark;
}
