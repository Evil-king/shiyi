package com.baibei.shiyi.user.feign.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/28 19:24
 * @description:
 */
@Data
public class CustomerVo {
    private String customerNo;
    //手机号
    private String mobile;
    //推荐人交易商编码
    private String recommenderId;
    //机构ID
    private Long orgId;
    //用户类型（1：普通用户 2：代理商）
    private Byte customerType;
    //用户状态，详情请见CustomerStatusEnum
    private String customerStatus;
    //头像
    private String userPicture;
    //交易余额
    private BigDecimal moneyBalance;
    //冻结金额
    private BigDecimal freezingAmount;
    //总资金
    private BigDecimal totalBalance;
    //可提金额
    private BigDecimal withdraw;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 消费积分
     */
    private BigDecimal consumptionBalance;
    /**
     * 兑换积分
     */
    private BigDecimal exchangeBalance;
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
     * 世屹无忧
     */
    private BigDecimal shiyiBalance;
    //是否业务系统签约(0未签约 1已签约)
    private String signing;
    //是否存在资金密码(0不存在 1存在)
    private String fundPassword;

    /**
     * 资金汇总账号
     */
    private String supAcctId;
    /**
     * 是否第一次购销（1：是，0：否）
     */
    private String isSign;

    /**
     * 实名认证（1=已实名认证；0=未实名认证）
     */
    private String realnameVerification;
}
