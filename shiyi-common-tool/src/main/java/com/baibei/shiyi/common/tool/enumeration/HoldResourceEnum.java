package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 11:02
 * @description: 持仓单来源
 */
public enum HoldResourceEnum {
    /**
     * 来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     */
    BUY("buy", "买入"),
    SELL("sell", "卖出"),
    EXCHANGE("exchange", "兑换"),
    BUY_TRANSFER("buy_transfer", "买入过户"),
    SELL_TRANSFER("sell_transfer", "卖出过户");

    private String code;
    private String desc;

    HoldResourceEnum(String code, String desc) {
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
        for (HoldResourceEnum item : values()) {
            if (code.equals(item.getCode())) {
                return item.getDesc();
            }
        }
        return "";
    }
}