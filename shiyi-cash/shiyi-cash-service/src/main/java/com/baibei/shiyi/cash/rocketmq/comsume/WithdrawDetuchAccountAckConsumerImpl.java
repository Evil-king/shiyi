package com.baibei.shiyi.cash.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.cash.feign.base.message.WithdrawDetuchAccountMessage;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 用户出金申请，确认扣钱消息消费者
 */
@Component
@Slf4j
public class WithdrawDetuchAccountAckConsumerImpl implements IConsumer<WithdrawDetuchAccountMessage> {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;


    @Override
    public ApiResult execute(WithdrawDetuchAccountMessage withdrawDetuchAccountMessage) {
        log.info("用户出金申请，确认扣钱消息消费者正在消费....");
        ApiResult apiResult;
        try{
            orderWithdrawService.accountAckUpdateStatus(withdrawDetuchAccountMessage);
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.error("用户出金申请，确认扣钱消息消费者执行报错：",e);
            e.printStackTrace();
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}