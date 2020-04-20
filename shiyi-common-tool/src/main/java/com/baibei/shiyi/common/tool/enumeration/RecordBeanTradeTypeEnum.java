package com.baibei.shiyi.common.tool.enumeration;

/** 积分流水交易类型，前缀为积分类型（）
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum RecordBeanTradeTypeEnum {
    CONSUMPTION_RECHARGE("consumption_recharge", "积分充值"),
    CONSUMPTION_BUY_GIVE("consumption_buy_give", "购买赠送"),
    CONSUMPTION_GIVE_PAY("consumption_give_pay", "赠送支出"),
    CONSUMPTION_RETURN_GOODS("consumption_return_goods", "退货扣回"),
    CONSUMPTION_RETURN_INCOME("consumption_return_INCOME", "退款收入"),
    CONSUMPTION_GAME_CONSUME("consumption_game_consume", "游戏消耗"),
    CONSUMPTION_GAME_INCOME("consumption_game_income","游戏收入"),
    CONSUMPTION_GAME_GIVE("consumption_game_give","游戏赠送"),
    CONSUMPTION_GAME_REWARD("consumption_game_reward","游戏派奖"),
    CONSUMPTION_CONSUME_PAY("consumption_consume_pay","消费支出"),
    CONSUMPTION_SALE_INCOME("consumption_sale_income","商品销售收入"),
    CONSUMPTION_SERVICE_FEE_INCOME("consumption_service_fee_income","服务费收入"),
    CONSUMPTION_OPERATION_REWARD("consumption_operation_reward","运营活动"),
    EXCHANGE_IN("exchange_in","积分转入"),
    EXCHANGE_OUT("exchange_out","积分转出"),
    EXCHANGE_CONSIGNMENT_SERVICE_FEE("exchange_consignment_service_fee","寄售服务费"),
    EXCHANGE_BUY_PAY("exchange_buy_pay","购买支出"),
    EXCHANGE_BUY_GIVE("exchange_buy_give","购买赠送"),
    EXCHANGE_SALE_INCOME("exchange_sale_income","销售收入"),
    EXCHANGE_PASSCHECK_PAY("exchange_passcheck_pay","兑换红木券支出"),
    EXCHANGE_PASSCHECK_INCOME("exchange_passcheck_income","兑换红木券收入"),
    EXCHANGE_PASSCHECK_SERVICE_FEE("exchange_passcheck_service_fee","兑换红木券服务费"),
    EXCHANGE_MALL_SALE_REWARD("exchange_mall_sale_reward","商城销售奖励"),
    SHIYI_BUY_GIVE("shiyi_buy_give","购买赠送"),
    SHIYI_CONSUME_PAY("shiyi_consume_pay","消费支出"),
    SHIYI_OPERATION_REWARD("shiyi_operation_reward","运营活动"),
    MALLACCOUNT_IN("mallAccount_in","转入"),
    MALLACCOUNT_OUT("mallAccount_out","转出"),
    MALLACCOUNT_OUT_FEE("mallAccount_out_fee","转出手续费"),
    MALLACCOUNT_COMSUME_PAY("mallAccount_comsume_pay","消费支出"),
    MALLACCOUNT_SALE_INCOME("mallAccount_sale_income","销售收入"),
    MALLACCOUNT_RETURN_INCOME("mallAccount_return_income","退款收入"),
    MALLACCOUNT_RETURN_PAY("mallAccount_return_pay","退款支出"),
    MALLACCOUNT_ORDER_BUY("mallaccount_order_buy","买入"),
    MALLACCOUNT_ORDER_SELL("mallaccount_order_sell","卖出"),
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

    RecordBeanTradeTypeEnum(String code, String msg) {
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
        for (RecordBeanTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() .equals(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     * 根据提示信息获取到对应的状态码
     *
     * @param msg
     * @return
     */
    public static String getCode(String msg) {
        for (RecordBeanTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }
}
