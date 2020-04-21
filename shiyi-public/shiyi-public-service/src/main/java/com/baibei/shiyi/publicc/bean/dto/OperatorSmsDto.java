package com.baibei.shiyi.publicc.bean.dto;

import lombok.Data;

@Data
public class OperatorSmsDto {
    private String smsType;//短信模板
    private String contentNo;//短信内容编号
    private String phone;
}
