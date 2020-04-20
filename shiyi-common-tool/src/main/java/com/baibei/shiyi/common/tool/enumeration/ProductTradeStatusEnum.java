package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 11:02
 * @description:
 */
public enum ProductTradeStatusEnum {

    WAIT("wait", "待上市"),
    TRADING("trading", "上市"),
    EXIT("exit", "退市");

    private String code;
    private String desc;

    ProductTradeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public static String getDesc(String code) {
        for (ProductTradeStatusEnum item : values()) {
            if (code.equals(item.getCode())) {
                return item.getDesc();
            }
        }
        return "";
    }
}