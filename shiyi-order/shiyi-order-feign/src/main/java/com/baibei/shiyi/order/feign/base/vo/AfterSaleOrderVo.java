package com.baibei.shiyi.order.feign.base.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AfterSaleOrderVo extends BaseRowModel {
    @ExcelProperty(index = 1, value = "主订单编号")
    private String orderNo;

    @ExcelProperty(index = 2, value = "子订单编号")
    private String orderItemNo;

    @ExcelProperty(index = 3, value = "服务单编号")
    private String serverNo;

    @ExcelProperty(index = 4, value = "下单时间")
    private String createTime;

    @ExcelProperty(index = 5, value = "完成时间")
    private String finishTime;

    @ExcelProperty(index = 6, value = "用户编号")
    private String customerNo;

    @ExcelProperty(index = 7, value = "订单金额")
    private BigDecimal totalAmount;

    @ExcelProperty(index = 8, value = "售后类型")
    private String type;

    @ExcelProperty(index = 9, value = "售后状态")
    private String status;

    @ExcelProperty(index = 10, value = "处理时间")
    private String modifyTime;
}
