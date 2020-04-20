package com.baibei.shiyi.account.feign.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/10/29 17:29
 * @description:
 */
@Data
public class CustomerBeanVo {
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 消费积分余额
     */
    private BigDecimal consumptionBalance=BigDecimal.ZERO;
    /**
     * 兑换积分余额
     */
    private BigDecimal exchangeBalance=BigDecimal.ZERO;
    /**
     * 待赋能余额（兑换积分释放而来）
     */
    private BigDecimal exchangeEmpowermentBalance=BigDecimal.ZERO;
    /**
     * 通证余额
     */
    private BigDecimal passCardBalance=BigDecimal.ZERO;
    /**
     * 商城账户余额
     */
    private BigDecimal mallAccountBalance=BigDecimal.ZERO;
    /**
     * 屹家无忧余额
     */
    private BigDecimal shiyiBalance=BigDecimal.ZERO;

    /**
     * 可用资金
     */
    private BigDecimal moneyBalance;
    /**
     * 可提金额
     */
    private BigDecimal withdrawBalance;
}

