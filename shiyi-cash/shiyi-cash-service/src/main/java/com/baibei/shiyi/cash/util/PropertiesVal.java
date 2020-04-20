package com.baibei.shiyi.cash.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/05/29
 */
@Data
@Component
public class PropertiesVal {

    //交易网名称
    @Value("${cash.tranWebName}")
    private String tranWebName;

    //出金开关(on=开；off=关)
    @Value("${withdraw.switch}")
    private String withdrawSwitch;

    //strt--出金手续费费率、出金最小手续费
    @Value("${withdraw.fee.rate}")
    private String rate;
    @Value("${withdraw.fee}")
    private BigDecimal fee;
    //end

    //start--入金时间、出金时间
    @Value("${withdraw.time}")
    private String withdrawTime;
    @Value("${deposit.time}")
    private String depositTime;
    //end


    //start--每个用户当天出金额度累计限度(含当前申请的金额在内)
    @Value("${withdrawal.amount}")
    private BigDecimal withdrawalAmount;
    //end

    //start--银行端发起出金提示语
    @Value("抱歉，出金申请不成功，请前往交易网端进行操作。如有疑问，可联系在线客服。")
    private String withdrawStr;
    //end

    //出金加钱的事物生产组
    @Value("${withdraw.addmoney.txProducerGroup}")
    private String withdrawAddMoneyTxProducerGroup;

    //审核出金的事物消息主题
    @Value("${rocketmq.withdraw.addmoney.topics}")
    private String withdrawAddMoneyTopic;


    //出金扣钱的事物生产组
    @Value("${withdraw.detuchmoney.txProducerGroup}")
    private String withdrawDetuchMoneyTxProducerGroup;

    //出金扣钱的事物消息主题
    @Value("${rocketmq.withdraw.detuchmoney.tx.topics}")
    private String withdrawDetuchMoneyTxTopic;


    //出金扣钱的事物消息主题
    @Value("${rocketmq.withdraw.aplly1318.tx.topics}")
    private String apply1318TxTopic;

    //调账修改用户资金消息主题
    @Value("${rocketmq.dealdiff.changeAmount.topics}")
    private String dealdiffTopic;

    //调账结果消息主题
    @Value("${rocketmq.dealdiff.changeAmount.ack.topics}")
    private String dealdiffAckTopic;

    //调账结果消息主题
    @Value("${rocketmq.settlement.fee.topics}")
    private String setFeeTopics;

    //清算消息
    @Value("${rocketmq.settlement.clean.topics}")
    private String setCleanTopics;

}
