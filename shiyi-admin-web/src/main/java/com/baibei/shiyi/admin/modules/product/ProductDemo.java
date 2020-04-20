package com.baibei.shiyi.admin.modules.product;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 18:27
 * @description:
 */
@Data
public class ProductDemo extends BaseRowModel {
    @ExcelProperty(value = "姓名", index = 0)
    private String xm;
    @ExcelProperty(value = "微信号", index = 1)
    private String wxh;
    @ExcelProperty(value = "手机号", index = 2)
    private String sjh;
}