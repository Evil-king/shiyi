package com.baibei.shiyi.account.rocketmq;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.KeyValue;
import com.baibei.shiyi.order.feign.base.message.SendIntegralMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/8 18:10
 * @description:
 */
@RestController
@RequestMapping("/api/rocketmq")
@Slf4j
public class RocketMQDemoController {
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.order.sendIntegral.topics}")
    private String sendIntegralTopic;

    /**
     * 发送消息
     *
     * @return
     */
    @GetMapping("/sendMsg")
    public ApiResult sendMsg() {
        SendIntegralMsg msg = new SendIntegralMsg();
        msg.setCustomerNo("001001");
        List<KeyValue> kvList = new ArrayList<>();
        KeyValue kv = new KeyValue();
        kv.setKey("shiyi");
        kv.setValue("100");

        KeyValue kv2 = new KeyValue();
        kv2.setKey("consumption");
        kv2.setValue("99");
        kvList.add(kv);
        kvList.add(kv2);
        msg.setIntegralList(kvList);

        rocketMQUtil.sendMsg(sendIntegralTopic, JSON.toJSONString(msg), UUID.randomUUID().toString());
        return ApiResult.success();
    }

}