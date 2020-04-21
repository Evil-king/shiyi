package com.baibei.shiyi.user.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/11/1 14:16
 * @description:
 */
@Data
public class AdminCustomerBalanceVo extends BaseRowModel {
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
    /**
     * 消费积分余额
     */
    @ExcelProperty(index = 11, value = "消费积分")
    private BigDecimal consumptionBalance;
    /**
     * 兑换积分
     */
    @ExcelProperty(index = 12, value = "兑换积分")
    private BigDecimal exchangeBalance;
    /**
     * 世屹无忧积分
     */
    @ExcelProperty(index = 13, value = "世屹无忧积分")
    private BigDecimal shiyiBalance;
    @ExcelProperty(index = 14, value = "可用余额")
    private BigDecimal moneyBalance;
    @ExcelProperty(index = 15, value = "可提余额")
    private BigDecimal withdrawBalance;
    @ExcelProperty(index = 16, value = "商城账户")
    private BigDecimal mallBalance;
}
