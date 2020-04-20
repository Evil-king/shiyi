package com.baibei.shiyi.common.tool.enumeration;

/** 资金可用余额流水交易类型
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum TradeMoneyTradeTypeEnum {
    RECHARGE("recharge", "入金"),
    WITHDRAW("withdraw", "出金"),
    WITHDRAW_FEE_PAY("withdraw_fee_pay", "出金手续费支出"),
    WITHDRAW_FEE_INCOME("withdraw_fee_income", "出金手续费收入"),
    CONSUMPTION_PAY("consumption_pay", "消费支出"),
    SALE_INCOME("sale_income","销售收入"),
    REFUND_INCOME("refund_income","退款收入"),
    REFUND_PAY("refund_pay","退款支出"),
    SALE_REWARD("sale_reward","销售奖励"),
    REWARD_PAY("reward_pay","奖励支出"),
    CONSIGNMENT_INCOME("consignment_income","寄售收入"),
    CONSIGNMENT_PAY("consignment_pay","寄售支出"),
    WITHDRAW_BACK("withdraw_back", "出金金额回退"),
    WITHDRAW_FEE_BACK("withdraw_fee_back", "出金手续费回退"),
    OPERATEREVIEW_WITHDRAW_BACK("operatereview_withdraw_back", "出金金额回补"),
    AMOUNT_RETURN_FEE_INCOME("amount_return_fee_income","手续费返还收入"),
    ACCOUNT_ADJUSTMENT_FEE_ADD("account_adjustment_fee_add", "资金支付"),//增加资金
    ACCOUNT_ADJUSTMENT_FEE_SUB("account_adjustment_fee_sub", "资金补扣"),//减少资金
    ACCOUNT_ADJUSTMENT_ADD("account_adjustment_add", "资金支付"),//增加资金
    ACCOUNT_ADJUSTMENT_SUB("account_adjustment_sub", "资金补扣"),//减少资金
    ORDER_BUY("order_buy","摘牌买入"),
    PICK_ORDER_SELL("pick_order_sell","摘牌卖出"),
    ORDER_SELL("order_sell","挂牌卖出"),
    HANG_ORDER_BUY("hang_order_buy","挂牌买入"),
    ORDER_FEE_BUY("order_fee_buy","买入手续费"),
    BUY_TRANSFER("buy_transfer","买入过户"),
    SELL_TRANSFER("sell_transfer","卖出过户"),
    ORDER_FEE_SELL("order_fee_sell","卖出手续费"),
    ADMIN_ACCOUNT_IN("admin_account_in","转入"),
    ADMIN_ACCOUNT_OUT("admin_account_out","转出"),
    CORRECT("correct","冲正"),
    ;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    TradeMoneyTradeTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param code
     * @return
     */
    public static String getMsg(String code) {
        for (TradeMoneyTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     *  根据提示信息获取到对应的状态码
     *
     * @param msg
     * @return
     */
    public static String getCode(String msg) {
        for (TradeMoneyTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
