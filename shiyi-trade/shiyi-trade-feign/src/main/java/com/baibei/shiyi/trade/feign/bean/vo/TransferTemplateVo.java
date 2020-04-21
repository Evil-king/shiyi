package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class TransferTemplateVo extends BaseRowModel {

    @ExcelProperty(index = 0, value = "商品编码")
    private String productTradeNo;

    @ExcelProperty(index = 1, value = "转入客户编码")
    private String inCustomerNo;

    @ExcelProperty(index = 2, value = "转出客户编码")
    private String outCustomerNo;

    @ExcelProperty(index = 3, value = "成交单价")
    private String price;

    @ExcelProperty(index = 4, value = "数量")
    private String num;

    @ExcelProperty(index = 5, value = "成本价")
    private String costPrice;

    @ExcelProperty(index = 6, value = "解锁日期")
    private String tradeTime;

    @ExcelProperty(index = 7, value = "备注")
    private String remark;

    @ExcelProperty(index = 8, value = "是否收手续费")
    private String isFee;

    private String creator;//创建人

    private String modifier;//执行人

    private String fileName;

}
