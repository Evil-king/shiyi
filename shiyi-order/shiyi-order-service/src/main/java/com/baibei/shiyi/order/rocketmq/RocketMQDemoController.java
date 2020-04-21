package com.baibei.shiyi.order.rocketmq;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.order.rocketmq.message.DemoMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 发送消息
     *
     * @return
     */
    @GetMapping("/sendMsg")
    public ApiResult sendMsg() {
        String orderNo = NoUtil.getMallOrderNo();
        DemoMsg demoMsg = new DemoMsg();
        demoMsg.setOrderNo(orderNo);
        demoMsg.setField1("field1");
        demoMsg.setField2("field2");
        rocketMQUtil.sendMsg("Topic-Demo", JacksonUtil.beanToJson(demoMsg), orderNo);
        return ApiResult.success();
    }
}