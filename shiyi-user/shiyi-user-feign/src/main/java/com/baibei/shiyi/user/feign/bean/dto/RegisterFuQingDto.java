package com.baibei.shiyi.user.feign.bean.dto;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/12/3 17:30
 * @description:
 */
@Data
public class RegisterFuQingDto {
    private static final long serialVersionUID = -642063858563777970L;
    private String menuId;
    private String functionId;
    private String bankProCode;
    private String operatorNo;
    private String gtid;
    private String version;
    private String exchangeId="1001022";
    private String memCode;
    private String exchangeFundAccount;
    private String tradeAccount;
    private String memberType="5";
    private String memberMainType="2";
    private String fullName;
    private String shortName;
    private String enFullName;
    private String enShortName;
    private String exchangeMemberStatus="1";
    //private String parentMemCode;
    //private String legalName;
    private String idKind="111";
    private String idNo;
    private String gender;
    private String nationality="CHN";
    private String businessCert;
    //private String organCode;
    private String taxCert;
    //private String registerAddr;
    //private String businessAddr;
    private String contactName;
    private String contactTel;
    private String contactFax;
    private String contactEmail;

    private String regAddr;
    private String comAddr;

    //private String mobile;
    private String upMemCode;
    private String brokerCode="8880888";
    private String tel;
    //private String relatedAccountStr;
    private String legalPerson;
    private String orgCode;

    private String taxCertType;


    private String createMan = "外部接口";
    private Boolean isIntefaceAdd = true;

    private String initDate;
    private String requestId;
    private String bankAccount;
    private String bankAccountName;
    private String moneyType="CNY";
    private String signType="1";
    private String busiDatetime;

    private String contactAddress;
    private String bankCardType;
    private String bankNode;

    private String bankTotalNode;
}
