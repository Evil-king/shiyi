package com.baibei.shiyi.common.tool.constants;

/**
 * excel导出数据最大数量
 */
public class ExcelConstant {

    /**
     * excel 导出一个sheet数据量最大
     */
    public static final Integer MAX_ROW_NUMBER = 500000;


    /**
     * 获取Sheet页数
     * 以0为页
     *
     * @param size 总量
     * @return
     */
    public static Integer countSheetCount(Integer size) {
        return (size + MAX_ROW_NUMBER - 1) / MAX_ROW_NUMBER;
    }
}
