package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PABSigningRecordDto {

    @Length(max = 32, message = "会员子账号不能超过32位")
    @NotNull
    private String supAcctId;

    @NotNull(message = "银行流水号不能为空")
    private String thirdLogNo;

    @NotNull(message = "功能标识不能为空")
    @Pattern(regexp = "1|2|3", message = "功能标识不正确")
    private String funcFlag;


    @NotNull(message = "会员名称不能为空")
    @Size(max = 120, message = "会员名称长度不能超过120")
    private String custName;

    /**
     * 会员代码
     */
    @NotNull(message = "会员代码不能为空")
    @Size(max = 32, message = "会员代码长度不能超过32位")
    private String customerNo;

    /**
     * 会员代码
     */
    @NotNull(message = "会员代码不能为空")
    @Length(max = 32, message = "会员代码长度不能超过32位")
    private String custAcctId;


    /**
     * 注册类型
     */
    @NotNull(message = "会员证件类型不能为空")
    @Length(max = 2, message = "会员证件类型长度错误")
    private String idType;

    /**
     * 会员证件号码
     */
    @NotNull(message = "会员证件号码不能为空")
    @Length(max = 20, message = "会员证件号码长度不能超过20")
    private String idCode;

    /**
     * 出入金账号
     */
    @NotNull(message = "出入金账号不能为空")
    @Length(max = 32, message = "出入金账号不能超过32位")
    private String relatedAcctId;


    //1、出金账号 2、入金账号 3、出金账号&入金账号
    @NotNull(message = "账号性质不能为空")
    @Pattern(regexp = "1|2|3", message = "账号性质格式不支持")
    private String acctFlag;


    @NotNull(message = "转账方式不能为空")
    @Pattern(regexp = "1|2|3", message = "转账方式格式错误")
    private String tranType;


    @NotNull(message = "账号名称不能为空")
    @Length(max = 120, message = "账号名称长度不能超过120")
    private String acctName;


    @Length(max = 12, message = "联行号长度不能超过12")
    private String bankCode;


    @Length(max = 120, message = "开户行长度名称不能超过120")
    private String bankName;


    @Length(max = 32, message = "原入金账号长度不能超过32")
    private String oldRelatedAcctId;


    @Length(max = 120, message = "保留域长度不能超过120")
    private String reserve;

}
