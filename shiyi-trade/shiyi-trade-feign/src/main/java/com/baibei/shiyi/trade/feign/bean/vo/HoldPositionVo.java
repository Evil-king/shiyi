package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import jdk.nashorn.internal.ir.IdentNode;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldPositionVo extends BaseRowModel {

    @ExcelProperty(index = 1,value = "客户编号")
    private String customerNo;

    @ExcelProperty(index = 2,value ="实名名称")
    private String customerName;

    @ExcelProperty(index = 3,value = "商品编码")
    private String productTradeNo;

    @ExcelProperty(index = 4,value = "商品名称")
    private String productName;

    @ExcelProperty(index = 5,value = "持有总数")
    private Integer remaindCount;

    @ExcelProperty(index = 5,value = "  可卖数量")
    private Integer canSellCount;

    @ExcelProperty(index = 6,value = "冻结数量")
    private Integer frozenCount;

    @ExcelProperty(index = 7,value = "锁定数量")
    private Integer lockCount;

    @ExcelProperty(index = 8,value = "成本价")
    private BigDecimal costPrice;

    @ExcelProperty(index = 9,value = "商品最新价")
    private BigDecimal lastPrice;

    /**
     * 持仓市值
     */
    @ExcelProperty(index = 10,value = "持仓市值")
    private BigDecimal holdMarketValue;


    /**
     * 盈亏比例
     */
    @ExcelProperty(index = 11,value = "盈亏比例")
    private BigDecimal profitLossRatio;

    /**
     * 盈亏资金
     */
    @ExcelProperty(index = 12,value = "盈亏资金")
    private BigDecimal profitAndLossPrice;


}