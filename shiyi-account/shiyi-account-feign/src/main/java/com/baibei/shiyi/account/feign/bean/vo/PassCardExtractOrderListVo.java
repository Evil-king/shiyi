package com.baibei.shiyi.account.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/12 14:56
 * @description:
 */
@Data
public class PassCardExtractOrderListVo extends BaseRowModel{

    private Long id;
    @ExcelProperty(index = 1, value = "兑换单号")
    private String orderNo;
    @ExcelProperty(index = 2, value = "兑换时间")
    private String createTime;
    @ExcelProperty(index = 3, value = "用户编码")
    private String customerNo;
    @ExcelProperty(index = 4, value = "兑换账户")
    private String extractCustomerNo;
    @ExcelProperty(index = 5, value = "商品交易编码")
    private String productTradeNo;
    @ExcelProperty(index = 6, value = "商品名称")
    private String productName;
    @ExcelProperty(index = 7, value = "兑换单价")
    private BigDecimal price;
    @ExcelProperty(index = 8, value = "兑换数量")
    private Integer amount;
    @ExcelProperty(index = 9, value = "兑换总额")
    private BigDecimal totalPrice;
    @ExcelProperty(index = 10, value = "手续费")
    private BigDecimal serviceCharge;
    @ExcelProperty(index = 11, value = "状态")
    private String status;
    @ExcelProperty(index = 12, value = "操作人")
    private String operatorName;
}
