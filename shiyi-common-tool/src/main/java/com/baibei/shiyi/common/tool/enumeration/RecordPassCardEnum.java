package com.baibei.shiyi.common.tool.enumeration;

/** 通证流水
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum RecordPassCardEnum {
    EXTRACT_PAY("extract_pay","提取支出"),
    SERVICE_CHARGE("service_charge","手续费"),
    EMPOWERMENT_PASS_CARD_INCOME("empowerment_pass_card_income","赋能红木券收入"),
    SERVICE_CHARGE_BACK("service_charge_back","手续费回退"),
    PASS_CARD_BACK("pass_card_back","红木券回退"),
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

    RecordPassCardEnum(String code, String msg) {
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
        for (RecordPassCardEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     * 根据提示信息获取到对应的状态码
     *
     * @param msg
     * @return
     */
    public static String getCode(String msg) {
        for (RecordPassCardEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
