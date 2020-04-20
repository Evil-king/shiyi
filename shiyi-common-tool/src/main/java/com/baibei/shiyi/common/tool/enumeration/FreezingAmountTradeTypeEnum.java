package com.baibei.shiyi.common.tool.enumeration;

/** 冻结资金可用余额流水交易类型
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum FreezingAmountTradeTypeEnum {
    COMMISSION_FREEZE("commission_freeze","分佣冻结"),
    LIST_BUY_FREEZE("list_buy_freeze","挂牌买入冻结"),
    LIST_REVOKE("list_revoke","挂牌买入撤单")
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

    FreezingAmountTradeTypeEnum(String code, String msg) {
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
        for (FreezingAmountTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code) ) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
