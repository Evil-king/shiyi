package com.baibei.component.rocketmq.core.aop;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.component.rocketmq.service.IRocketmqConsumeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/14 11:02
 * @description: RocketMQ的监听切面，统一处理日志记录，消息消费失败处理
 */
@Aspect
@Component
@Slf4j
public class RocketMQListenerAspect {
    // 消费失败最大重试次数
    @Value("${rocketmq.maxReconsumeTimes}")
    private String maxReconsumeTimes;
    @Autowired
    private IRocketmqConsumeService rocketmqConsumeService;

    /**
     * 环绕通知
     * notice：注意项目包路径要与切面路径一致
     *
     * @param joinPoint
     * @param message
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.baibei.*.*.rocketmq.listeners.*.onMessage(..)) &&" + "args(message)")
    public Object around(ProceedingJoinPoint joinPoint, MessageExt message) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String declaringTypeName = signature.getDeclaringTypeName();
        log.info("{} receive message，message={}", declaringTypeName, message.toString());
        // 判断是否达到重试最大次数
        int reconsumeTimes = message.getReconsumeTimes();
        if (reconsumeTimes > Integer.valueOf(maxReconsumeTimes)) {
            log.info("The maximum number of retries has been reached, the current number of retries is {}, " +
                    "and the maximum number of system configurations is {}", reconsumeTimes, maxReconsumeTimes);
            // 写入消息消费表
            rocketmqConsumeService.save(message, RocketMQUtil.FAIL);
            return null;
        }
        return joinPoint.proceed();
    }
}