package com.baibei.shiyi.cash.feign.base.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname Apply1010PagelistVo
 * @Description 获取台账列表vo
 * @Date 2019/11/11 19:51
 * @Created by Longer
 */
@Data
public class Apply1010PagelistVo extends BaseRowModel {


    private Long id;

    /**
     * 子账户
     */
    @ExcelProperty(index = 1, value = "子账户")
    private String custAcctid;

    /**
     * 1=虚拟账号
     */
    @ExcelProperty(index = 2, value = "子账户性质")
    private Byte custFlag;

    /**
     * 1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     */
    @ExcelProperty(index = 3, value = "子账户属性")
    private Byte custType;

    /**
     * 子账户状态 1：正常  2：已销户
     */
    @ExcelProperty(index = 4, value = "子账户状态")
    private Byte custStatus;

    /**
     * 会员编号
     */
    @ExcelProperty(index = 5, value = "会员编码")
    private String customerNo;

    /**
     * 上级监管账号
     */
    @ExcelProperty(index = 6, value = "上级监管账号")
    private String mainAcctid;

    /**
     * 会员名称
     */
    @ExcelProperty(index = 7, value = "会员名称")
    private String custName;

    /**
     * 账户总余额
     */
    @ExcelProperty(index = 8, value = "账户总额")
    private BigDecimal totalAmount;

    /**
     * 账户可用余额
     */
    @ExcelProperty(index = 9, value = "账户可用余额")
    private BigDecimal totalBalance;

    /**
     * 账户总冻结金额
     */
    @ExcelProperty(index = 9, value = "账户总冻结金额")
    private BigDecimal totalFreezeAmount;

    /**
     * 维护日期
     */
    @ExcelProperty(index = 10, value = "维护日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String tranDate;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

    /**
     * 创建时间
     */
    @ExcelProperty(index = 14, value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyTime;
}
