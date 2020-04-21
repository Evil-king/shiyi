package com.baibei.shiyi.pingan.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PABMessage {

    /**
     * 平安银行接口名称
     */
    @NotNull
    private Integer tranFunc;

    /**
     * 通讯报文头+业务报文头＋业务报文体
     */
    @NotNull
    private String message;

}
