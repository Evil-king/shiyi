package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 11:29
 * @description:
 */
public enum MallOrderStatusEnum {
    /**
     * 订单状态，init=订单初始化；wait=待支付；undelivery=待发货（已支付）；pay_fail=支付失败；deliveryed=待收货；cancel=已取消；completed=已完成
     */

    INIT("init", "初始化"),
    WAIT("wait", "待付款"),
    UNDELIVERY("undelivery", "待发货"),
    PAY_FAIL("pay_fail", "支付失败"),
    DELIVERYED("deliveryed", "待收货"),
    CANCEL("cancel", "已取消"),
    COMPLETED("completed", "已完成");

    private String code;
    private String desc;

    MallOrderStatusEnum(String code, String desc) {
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
        for (MallOrderStatusEnum item : values()) {
            if (code.equals(item.getCode())) {
                return item.getDesc();
            }
        }
        return "";
    }

}