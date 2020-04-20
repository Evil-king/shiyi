package com.baibei.shiyi.common.tool.enumeration;

/** 配售权流水交易类型
 * @author: hyc
 * @date: 2019/5/29 19:33
 * @description:
 */
public enum AllotmentTradeTypeEnum {

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

    AllotmentTradeTypeEnum(String code, String msg) {
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
        for (AllotmentTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code) ) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
