package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/5/27 15:02
 * @description: 账号状态
 */
public enum HoldDetailResourceEnum {
    STORAGE("storage", "入库(初始化)"),
    DEAL_BUY("deal_buy", "摘牌买入"),
    TRANSFER("transfer", "非交易过户"),
    EXCHANGE("exchange", "兑换"),
    ;

    private String code;
    private String msg;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    HoldDetailResourceEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param code
     * @return
     */
    public static String getMsg(String code) {
        for (HoldDetailResourceEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     *  根据提示信息获取到对应的状态码
     *
     * @param msg
     * @return
     */
    public static String getCode(String msg) {
        for (HoldDetailResourceEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
