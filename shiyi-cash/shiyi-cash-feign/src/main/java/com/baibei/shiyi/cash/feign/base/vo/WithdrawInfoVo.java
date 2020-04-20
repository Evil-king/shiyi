package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;

/**
 * @Classname WithdrawInfoVo
 * @Description TODO
 * @Date 2019/11/5 15:37
 * @Created by Longer
 */
@Data
public class WithdrawInfoVo {
    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 账号名称
     */
    private String acctName;

    /**
     * 出入金账号
     */
    private String relatedAcctId;

    /**
     * 出金手续费率
     */
    private String withdrawFeeRate;

    /**
     * 最低手续费
     */
    private float withdrawFee;

    /**
     * 出金开始时间
     */
    private String withdrawStartTime;

    /**
     * 出金开始时间
     */
    private String withdrawEndTime;

    /**
     * 出金时间范围
     */
    private String withdrawScopeTime;

    /**
     * 出金时间文案
     */
    private String withdrawTimeStr;

    /**
     * 支行名称
     */
    private String branchName;

    public String getWithdrawTimeStr(){
        return "提现时间："+getWithdrawScopeTime()+"（交易日）";
    }

}
