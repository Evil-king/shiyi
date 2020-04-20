package com.baibei.shiyi.cash.feign.base.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawListVo extends BaseRowModel {
    @ExcelProperty(value = "提现流水号", index = 1)
    private String orderNo;
    @ExcelProperty(value = "银行流水号", index = 2)
    private String externalNo;
    @ExcelProperty(value = "平台会员代码", index = 3)
    private String customerNo;
    @ExcelProperty(value = "用户名", index = 4)
    private String userName;
    @ExcelProperty(value = "提现金额", index = 5)
    private BigDecimal orderamt;
    @ExcelProperty(value = "手续费", index = 6)
    private BigDecimal handelFee;
    @ExcelProperty(value = "总金额", index = 7)
    private BigDecimal totalAmount;
    @ExcelProperty(value = "状态", index = 8)
    private String status;
    @ExcelProperty(value = "申请时间", index = 9)
    private String startTime;
    @ExcelProperty(value = "审核时间", index = 10)
    private String reviewTime;
    private String endTime;
    @ExcelProperty(value = "审核人", index = 11)
    private String reviewer;

    public BigDecimal getTotalAmount(){
        return getOrderamt().add(getHandelFee());
    }

}
