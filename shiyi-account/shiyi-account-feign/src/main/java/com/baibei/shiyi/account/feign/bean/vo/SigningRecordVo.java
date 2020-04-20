package com.baibei.shiyi.account.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class SigningRecordVo {

    private Long id;

    /**
     * 资金汇总账号
     */
    private String supAcctId;

    /**
     * 会员子账号(银行系统给予会员资金明细帐户的唯一标识，由银行系统自动分配)
     */
    private String custAcctId;

    /**
     * 会员名称
     */
    private String custName;

    /**
     * 会员证件类型(73-社会信用代码证,1-身份证,52-组织机构代码证)
     */
    private String idType;

    /**
     * 会员证件名称
     */
    private String idCode;

    /**
     * 出入金账号
     */
    private String relatedAcctId;

    /**
     * 账号性质(3：出金账号&入金账号)
     */
    private String acctFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除(1:指定,2:修改,3:删除)
     */
    private Byte funcFlag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 转账方式(1、本行2、同城3、异地汇款)
     */
    private String tranType;

    /**
     * 账号名称
     */
    private String acctName;

    /**
     * 银行号
     */
    private String bankCode;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 原出入金账号
     */
    private String oldRelatedAcctId;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 交易网流水号
     */
    private String thirdLogNo;

    /**
     * 会员代码((由交易网生成,标识会员在平台身份的ID)
     */
    private String thirdCustId;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;
}
