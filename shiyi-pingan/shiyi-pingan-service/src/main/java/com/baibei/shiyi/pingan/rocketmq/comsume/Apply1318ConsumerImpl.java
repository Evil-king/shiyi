package com.baibei.shiyi.pingan.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.cash.feign.base.message.Apply1318ConsumerMessage;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.service.IPABSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 请求1318接口
 */
@Component
@Slf4j
public class Apply1318ConsumerImpl implements IConsumer<PABSendDto> {
    @Autowired
    private IPABSendService pabSendService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Value("${rocketmq.apply1318ack.topics}")
    private String apply1318ackTopics;

    @Override
    public ApiResult execute(PABSendDto pabSendDto) {
        PABSendVo pabSendVo = new PABSendVo();
        try{
            ApiResult<PABSendVo> pabSendVoApiResult = pabSendService.sendMessage(pabSendDto);
            pabSendVo=pabSendVoApiResult.getData();
        }catch (Exception e){
            log.error("Apply1318ConsumerImpl：请求1318接口消息执行报错：",e);
            e.printStackTrace();
        }
        //发送确认消息
        Apply1318ConsumerMessage apply1318ConsumerMessage = new Apply1318ConsumerMessage();
        apply1318ConsumerMessage.setOrderNo(pabSendDto.getThirdLogNo());
        apply1318ConsumerMessage.setMessage(pabSendVo.getBackBodyMessages());
        rocketMQUtil.sendMsg(apply1318ackTopics,JacksonUtil.beanToJson(apply1318ConsumerMessage),apply1318ConsumerMessage.getOrderNo());
        return ApiResult.success();
    }
}