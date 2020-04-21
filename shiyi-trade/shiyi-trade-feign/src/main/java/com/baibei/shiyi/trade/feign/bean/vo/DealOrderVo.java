package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/18 10:28
 * @description:
 */
@Data
public class DealOrderVo extends BaseRowModel {
    @ExcelProperty(index = 1, value = "成交单号")
    private String dealNo;

    @ExcelProperty(index = 2, value = "成交时间")
    private String createTime;

    @ExcelProperty(index = 3,value = "客户编码")
    private String delister;

    private String direction;

    @ExcelProperty(index = 4, value = "成交类型")
    private String directionText;

    @ExcelProperty(index = 5,value = "对方客户编码")
    private String beDelister;


    @ExcelProperty(index = 6, value = "商品交易编码")
    private String productTradeNo;

    @ExcelProperty(index = 7, value = "商品名称")
    private String productName;

    @ExcelProperty(index = 8, value = "成交价")
    private BigDecimal price;

    @ExcelProperty(index = 9, value = "成交数量")
    private BigDecimal count;

    @ExcelProperty(index = 10, value = "成交总额")
    private BigDecimal totalPrice;

    @ExcelProperty(index = 11, value = "摘牌方手续费")
    private BigDecimal delisterFee;

    @ExcelProperty(index = 12, value = "被摘牌方手续费")
    private BigDecimal beDelisterfee;

    @ExcelProperty(index = 13, value = "委托单号")
    private String entrustNo;
}
