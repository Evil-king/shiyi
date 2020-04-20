package com.baibei.shiyi.account.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.message.WithdrawDetuchAccountMessage;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 出金扣钱
 */
@Component
@Slf4j
public class WithdrawDetuchMoneyConsumerImpl implements IConsumer<OrderWithdrawDto> {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Value("${rocketmq.withdrawDetuchAccountAck.topics}")
    private String detuchMoneyAckTopic;

    @Override
    public ApiResult execute(OrderWithdrawDto orderWithdrawDto) {
        boolean flag;//扣款成功与否
        try{
            accountService.withdrawDetuchMoney(orderWithdrawDto);
            flag=true;
        }catch (Exception e){
            flag=false;
            log.info("出金扣钱失败，用户编码为："+orderWithdrawDto.getCustomerNo()+
                    "，订单号为："+orderWithdrawDto.getOrderNo()+"，出金金额为："+orderWithdrawDto.getOrderAmt());
            log.error("WithdrawDetuchMoneyConsumerImpl:出金扣钱报错：",e);
            e.printStackTrace();
        }
        WithdrawDetuchAccountMessage withdrawDetuchAccountMessage = new WithdrawDetuchAccountMessage();
        withdrawDetuchAccountMessage.setOrderNo(orderWithdrawDto.getOrderNo());
        withdrawDetuchAccountMessage.setCustomerNo(orderWithdrawDto.getCustomerNo());
        withdrawDetuchAccountMessage.setAmount(orderWithdrawDto.getAmount());
        withdrawDetuchAccountMessage.setFee(orderWithdrawDto.getFee());
        withdrawDetuchAccountMessage.setResult(flag?"success":"fail");
        //发送消息
        rocketMQUtil.sendMsg(detuchMoneyAckTopic,JacksonUtil.beanToJson(withdrawDetuchAccountMessage),orderWithdrawDto.getOrderNo());
        //直接return成功，不走重试机制
        return ApiResult.success();
    }
}