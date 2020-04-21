package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/14 13:59
 * @description:
 */
@Data
public class CustomerHoldPageListVo extends BaseRowModel {
    @ExcelProperty(index = 1, value = "客户编号")
    private String customerNo;

    @ExcelProperty(index = 2,value = "客户名称")
    private String customerName;

    @ExcelProperty(index = 3, value = "持有时间")
    private String createTime;

    @ExcelProperty(index = 4, value = "交易流水号")
    private String holdNo;

    @ExcelProperty(index = 5, value = "商品编号")
    private String productTradeNo;

    @ExcelProperty(index = 6, value = "商品名称")
    private String productName;

    @ExcelProperty(index = 7, value = "商品期初数量")
    private BigDecimal originalCount;

    private String scanner;

    @ExcelProperty(index = 8, value = "状态")
    private String scannerText;

    @ExcelProperty(index = 9, value = "买入价")
    private BigDecimal buyPrice;

    private String resource;

    @ExcelProperty(index = 10,value = "来源")
    private String resourceText;
}
