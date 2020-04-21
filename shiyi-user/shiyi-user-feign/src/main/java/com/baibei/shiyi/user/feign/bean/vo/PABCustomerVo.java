package com.baibei.shiyi.user.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class PABCustomerVo {


    private Long id;


    private String customerNo;


    /**
     * 是否业务系统签约(0未签约 1已签约)
     */
    private String signing;


    /**
     * 手机号
     */
    private String mobile;


    /**
     * 实名信息
     */
    private String realName;

    /**
     * 身份证信息
     */
    private String idCard;

    /**
     * 直接推荐人ID
     */
    private String recommenderId;

    /**
     * 直属归属
     */
    private Long orgId;

    /**
     * 1,：普通用户 2：代理商
     */
    private Byte customerType;

    /**
     * 100:正常101：限制商城登录102：限制交易登录等等
     */
    private String customerStatus;
    /**
     * 所属代理编码（三层代理才有）
     */
    private Long orgCode;

    /**
     * 是否第一次购销
     */
    private String isSign;

    /**
     * 实名认证（1=已实名认证；0=未实名认证）
     */
    private String realnameVerification;
    /**
     * 所属市级代理编码
     */
    private Long cityAgentCode;
    private String memCodeClear;

    private String bankClientNo;

    private String fundAccountClear;
    /**
     * 加密时的盐值
     */
    private String salt;

    /**
     * 二维码链接地址
     */
    private String qrcode;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * 状态（1：正常；0：已删除）
     */
    private Byte flag;
}
