package com.baibei.shiyi.user.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/11/4 15:30
 * @description:
 */
@Data
public class CustomerListVo   extends BaseRowModel {
    private Long id;
    /**
     * 手机号
     */
    @ExcelProperty(index = 1, value = "手机号")
    private String mobile;
    /**
     * 用户编号
     */
    @ExcelProperty(index = 2, value = "用户编号")
    private String customerNo;
    /**
     * 直接推荐人
     */
    @ExcelProperty(index = 3, value = "直接推荐")
    private String recommenderId;
    /**
     * 间接推荐人
     */
    @ExcelProperty(index = 4, value = "间接推荐")
    private String indirectRecommendId;
    /**
     * 直属代理
     */
    @ExcelProperty(index = 5, value = "直属代理")
    private String orgId;
    /**
     * 普通代理
     */
    @ExcelProperty(index = 6, value = "普通代理")
    private String ordinaryAgent;
    /**
     * 区级代理
     */
    @ExcelProperty(index = 7, value = "区级代理")
    private String areaAgent;
    /**
     * 市级代理
     */
    @ExcelProperty(index = 8, value = "市级代理")
    private String cityAgent;
    /**
     * 所属机构
     */
    @ExcelProperty(index = 9, value = "所属机构")
    private String organization;
    /**
     * 所属事业部
     */
    @ExcelProperty(index = 10, value = "所属事业部")
    private String business;
    @ExcelProperty(index = 11, value = "性别")
    private String sex;

    @ExcelProperty(index = 12, value = "年龄")
    private Integer age;

    @ExcelProperty(index = 13, value = "实名")
    private String realname;

    @ExcelProperty(index = 14, value = "身份证号")
    private String idCard;

    @ExcelProperty(index = 15, value = "绑卡银行")
    private String signingBank;

    @ExcelProperty(index = 16, value = "分行")
    private String branchName;

    @ExcelProperty(index = 17, value = "银行卡号")
    private String signingBankCardNo;

    //资金子账户
    @ExcelProperty(index = 18, value = "资金子账户")
    private String fundAccountClear;
    //银行虚户
    @ExcelProperty(index = 19, value = "银行虚户")
    private String bankClientNo;

    @ExcelProperty(index = 20, value = "开户时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date signingTime;

    @ExcelProperty(index = 21, value = "注册时间")
    private String createTime;

    @ExcelProperty(index = 22, value = "状态")
    private String customerStatus;

    @ExcelProperty(index = 23, value = "法人")
    private String isLegalPerson;
}
