package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/5/27 15:02
 * @description: 账号状态
 */
public enum EntrustOrderResultEnum {
    /**
     * 委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     */
    WAIT_DEAL("wait_deal", "待成交"),//
    ALL_DEAL("all_deal", "全部成交"),//
    SYSTEM_REVOKE("system_revoke", "系统撤单"),//
    CUSTOMER_REVOKE("customer_revoke", "客户撤单"),//
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

    EntrustOrderResultEnum(String code, String msg) {
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
        for (EntrustOrderResultEnum resultCodeMsg : values()) {
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
        for (EntrustOrderResultEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
