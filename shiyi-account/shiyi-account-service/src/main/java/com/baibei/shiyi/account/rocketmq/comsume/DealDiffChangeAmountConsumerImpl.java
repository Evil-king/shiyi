package com.baibei.shiyi.account.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.cash.feign.base.dto.CashChangeAmountDto;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 调账
 */
@Component
@Slf4j
public class DealDiffChangeAmountConsumerImpl implements IConsumer<DealDiffMessage> {
    @Autowired
    private IAccountService accountService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Value("${rocketmq.dealdiff.changeAmount.ack.topics}")
    private String dealDiffAckTopics;

    @Override
    public ApiResult execute(DealDiffMessage dealDiffMessage) {
        try{
            accountService.dealDiffChangeAmount(dealDiffMessage);
            dealDiffMessage.setResultFlag(Constants.Status.SUCCESS);
        }catch (Exception e){
            dealDiffMessage.setResultFlag(Constants.Status.FAIL);
            log.error("DealDiffChangeAmountConsumerImpl:调账操作资金报错：",e);
            e.printStackTrace();
        }
        rocketMQUtil.sendMsg(dealDiffAckTopics,JacksonUtil.beanToJson(dealDiffMessage),UUID.randomUUID().toString());
        //直接return success，无论修改资金成功与否，都不重试消费
        return ApiResult.success();
    }
}