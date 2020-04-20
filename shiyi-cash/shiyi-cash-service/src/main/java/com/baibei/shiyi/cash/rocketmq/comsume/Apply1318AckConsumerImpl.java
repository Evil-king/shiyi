package com.baibei.shiyi.cash.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.cash.feign.base.message.Apply1318ConsumerMessage;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 出入金服务请求1318接口结果返回消息
 */
@Component
@Slf4j
public class Apply1318AckConsumerImpl implements IConsumer<Apply1318ConsumerMessage> {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;

    @Override
    public ApiResult execute(Apply1318ConsumerMessage apply1318ConsumerMessage) {
        ApiResult apiResult;
        try{
            orderWithdrawService.apply1318Ack(apply1318ConsumerMessage);
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.error("出入金服务请求1318接口结果返回消息执行报错：",e);
            e.printStackTrace();
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}