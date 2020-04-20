package com.baibei.shiyi.common.tool.enumeration;

/** 待赋能流水
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum RecordEmpowermentEnum {
    EMPOWERMENT_PASS_CARD_PAY("empowerment_pass_card_pay","赋能红木券支出"),
    DAY_RELEASE("day_release","每日释放"),
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

    RecordEmpowermentEnum(String code, String msg) {
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
        for (RecordEmpowermentEnum resultCodeMsg : values()) {
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
        for (RecordEmpowermentEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
