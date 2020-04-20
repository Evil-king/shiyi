package com.baibei.shiyi.common.tool.enumeration;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/13 11:29
 * @description:
 */
public enum ProductSourceEnum {
    /**
     * 商品上架来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */

    INTEGRALMODULE("integralmodule", "积分舱"),
    FIRSTMODULE("firstmodule", "头等舱"),
    UPMODULE("upmodule", "升仓"),
    EXTENDMODULE("extendmodule", "传承仓"),
    SHAREMODULE("sharemodule", "共享仓");

    private String code;
    private String desc;

    ProductSourceEnum(String code, String desc) {
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
        for (ProductSourceEnum item : values()) {
            if (code.equals(item.getCode())) {
                return item.getDesc();
            }
        }
        return "";
    }

}