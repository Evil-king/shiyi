package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class TransferPageListVo extends BaseRowModel {

    @ExcelProperty(index = 1, value = "过户时间")
    private String createTime;

    @ExcelProperty(index = 2, value = "流水号")
    private String serialNumber;

    @ExcelProperty(index = 3, value = "转入客户编码")
    private String inCustomerNo;

    @ExcelProperty(index = 4, value = "转出客户编码")
    private String outCustomerNo;

    @ExcelProperty(index = 5, value = "商品编码")
    private String productTradeNo;

    @ExcelProperty(index = 6, value = "过户数量")
    private String transferNum;

    @ExcelProperty(index = 7, value = "单价")
    private BigDecimal price;

    @ExcelProperty(index = 8, value = "商品总价")
    private BigDecimal totalPrice;

    @ExcelProperty(index = 9, value = "买方手续费")
    private BigDecimal buyFee;

    @ExcelProperty(index = 10, value = "卖方手续费")
    private BigDecimal sellFee;

    @ExcelProperty(index = 11, value = "成本价")
    private BigDecimal costPrice;

    @ExcelProperty(index = 12, value = "执行状态")
    private String status;

    @ExcelProperty(index = 13, value = "备注")
    private String remark;

    private String tradeTime;

    private String isFee;
}
