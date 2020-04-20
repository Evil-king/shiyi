package com.baibei.shiyi.common.tool.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum ResultTypeEnum {
    DOING("doing", "处理中"),
    SUCCESS("success", "成功"), //退款结果
    FAIL("fail", "失败");


    private static final Map<String, ResultTypeEnum> statusMap = new HashMap(ResultTypeEnum.values().length);


    static {
        for (ResultTypeEnum result : ResultTypeEnum.values()) {
            statusMap.put(result.getResult(), result);
        }
    }

    private String result;

    /**
     * 结果描述
     */
    private String resultMsg;


    ResultTypeEnum(String result, String resultMsg) {
        this.result = result;
        this.resultMsg = resultMsg;
    }

    public static ResultTypeEnum getResultMap(String result) {
        return statusMap.get(result);
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
