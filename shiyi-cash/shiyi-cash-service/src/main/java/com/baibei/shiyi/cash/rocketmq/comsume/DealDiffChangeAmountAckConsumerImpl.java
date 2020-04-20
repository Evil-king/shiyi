package com.baibei.shiyi.cash.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.cash.feign.base.message.Apply1318ConsumerMessage;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.service.IWithDrawDepositDiffService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 调账修改资金结果消息处理
 */
@Component
@Slf4j
public class DealDiffChangeAmountAckConsumerImpl implements IConsumer<DealDiffMessage> {
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;

    @Override
    public ApiResult execute(DealDiffMessage dealDiffMessage) {
        ApiResult apiResult;
        try{
            withDrawDepositDiffService.dealDiffAck(dealDiffMessage);
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.error("DealDiffChangeAmountAckConsumerImpl执行报错：",e);
            e.printStackTrace();
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}