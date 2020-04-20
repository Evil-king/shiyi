package com.baibei.shiyi.common.tool.enumeration;

/**
 * @Classname PeriodType
 * @Description TODO
 * @Date 2019/5/30 16:57
 * @Created by Longer
 */
public enum PeriodType {
    /** 分时 */
    ONE_MINUTE("1"),

    /** 5分钟 */
    FIVE_MINUTE("5"),

    /** 15分钟 */
    FIFTEEN_MINUTE("15"),

    /** 30分钟 */
    THIRTY_MINUTE("30"),

    /** 60分钟 */
    SIXTY_MINUTE("60"),

    /** 天 */
    DAY("day"),

    /** 4小时*/
    FOUR_HOUR("4"),

    /** 周 */
    WEEK("week"),

    /** 月 */
    MONTH("month");

    private String index;

    // 构造方法
    private PeriodType(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}
