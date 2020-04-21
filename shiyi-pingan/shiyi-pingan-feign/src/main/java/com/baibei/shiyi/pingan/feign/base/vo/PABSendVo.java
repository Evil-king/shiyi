package com.baibei.shiyi.pingan.feign.base.vo;

import lombok.Data;

@Data
public class PABSendVo {

    /**
     * 请求的结果 成功000000
     */
    private String rspCode;

    /**
     * 请求的结果的消息
     */
    private String rspMsg;

    /**
     * 请求系统流水号
     */
    private String externalNo;

    /**
     * 消息解析结果
     */
    private String backBodyMessages;

    /**
     * 错误时，返回完整的消息体
     */
    private String tranBackMessage;
}
