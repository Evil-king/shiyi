package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/5/27 15:02
 * @description: 账号状态
 */
public enum CustomerStatusEnum {
    NORMAL("normal", "正常"),// 账号正常
    LIMIT_MALL_LOGIN("limit_login", "限制登录"),// 限制登录
    LIMIT_WITHDRAW("limit_withdraw", "限制提现"),// 限制提现
    LIMIT_WITHDRAW_COIN("limit_withdraw_coin", "限制提币"),// 限制买入
    LIMIT_CONSIGNMENT("limit_consignment", "限制寄售"),// 限制卖出
    LIMIT_PAY("limit_pay", "限制支付"),
    LIMIT_BUY("limit_buy","限制买入"),
    LIMIT_SELL("limit_sell","限制卖出"),
    LIMIT_EMPOWERMENT("limit_empowerment","限制赋能"),
    LIMIT_EXTRACT("limit_extract","限制提取"),
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

    CustomerStatusEnum(String code, String msg) {
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
        for (CustomerStatusEnum resultCodeMsg : values()) {
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
        for (CustomerStatusEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
