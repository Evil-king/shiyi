package com.baibei.shiyi.pingan.feign.base.vo;

import lombok.Data;


@Data
public class PABAcceptVo {

    private String backBodyMessages;

    /**
     * 请求的结果 成功000000就可以不用填写
     * 失败需要填写rspCode
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
}
