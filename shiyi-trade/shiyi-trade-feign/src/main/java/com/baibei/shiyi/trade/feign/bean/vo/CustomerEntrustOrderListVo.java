package com.baibei.shiyi.trade.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/14 16:10
 * @description:
 */
@Data
public class CustomerEntrustOrderListVo extends BaseRowModel {
    @ExcelProperty(index = 1, value = "用户编号")
    private String customerNo;
    @ExcelProperty(index = 2, value = "委托时间")
    private String createTime;
    @ExcelProperty(index = 3, value = "委托方向")
    private String direction;
    @ExcelProperty(index = 4, value = "商品编码")
    private String productTradeNo;
    @ExcelProperty(index = 5, value = "商品名称")
    private String productName;
    @ExcelProperty(index = 6, value = "委托价格")
    private BigDecimal price;
    @ExcelProperty(index = 7, value = "委托数量")
    private BigDecimal entrustCount;

    /**
     * 总价值（数量*价格）
     */
    @ExcelProperty(index = 8, value = "总价值")
    private BigDecimal totalPrice;
    @ExcelProperty(index = 9, value = "已成交数量")
    private BigDecimal dealCount;
    @ExcelProperty(index = 10, value = "撤单数量")
    private BigDecimal revokeCount;
    @ExcelProperty(index = 11, value = "委托单号")
    private String entrustNo;
    @ExcelProperty(index = 12, value = "处理结果")
    private String result;
}
