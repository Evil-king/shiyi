package com.baibei.shiyi.user.common.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/31 16:32
 * @description:
 */
@Data
public class CustomerPageListVo {
    /**
     * 序号
     */
    private String id;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 手机归属地
     */
    private String mobileLocation;
    /**
     * 直接推荐
     */
    private String recommenderId;
    /**
     * 间接推荐
     */
    private String indirectId;
    /**
     * 直属代理（可能为各个代理中的其中一个）
     */
    private String orgId;
    /**
     * 普通代理
     */
    private String normalAgent;
    /**
     * 区代
     */
    private String areaAgent;
    /**
     * 市代
     */
    private String cityAgent;
    /**
     * 所属机构
     */
    private String organization;
    /**
     * 所属事业部
     */
    private String division;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 实名名称
     */
    private String realName;
    /**
     * 身份证号
     */
    private String idcrad;
    /**
     * 签约银行
     */
    private String signingBank;
    /**
     * 银行卡号
     */
    private String bankCardNo;
    /**
     * 存管银行
     */
    private String depositoryBank;
    /**
     * 签约时间
     */
    private String signingTime;
    /**
     * 返还账号
     */
    private String returnNo;
    /**
     * 返还比率
     */
    private String returnRatio;
    /**
     * 注册时间
     */
    private String createTime;
    /**
     * 状态
     */
    private String status;
}
