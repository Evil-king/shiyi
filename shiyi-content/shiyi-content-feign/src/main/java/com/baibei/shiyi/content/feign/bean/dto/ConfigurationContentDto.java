package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfigurationContentDto {
    @NotNull(message = "收款账户不能为空")
    private String accountNo;
    @NotNull(message = "收款账号不能为空")
    private String accountName;
    @NotNull(message = "开户银行不能为空")
    private String bankName;
    private String inContent;
    private String outContent;
}
