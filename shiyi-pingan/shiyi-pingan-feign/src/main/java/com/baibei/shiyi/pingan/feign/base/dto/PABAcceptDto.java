package com.baibei.shiyi.pingan.feign.base.dto;

import lombok.Data;

/**
 * 平安银行消息,把内容组装成bean对象,发送给用户,我要消息体
 */
@Data
public class PABAcceptDto {

    // 交易码
    private String TranFunc;

    // 银行流水号
    private String bankExternalNo;

    // 业务报文体
    private String message;

    //完整的通讯报文头+业务报文头＋业务报文体
    private String tranMessage;
}
