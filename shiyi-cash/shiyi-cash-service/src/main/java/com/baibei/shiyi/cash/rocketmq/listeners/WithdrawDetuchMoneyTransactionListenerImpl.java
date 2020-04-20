/*
package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

*/
/**
 * @Classname AuditWithdrawTransactionListenerImpl
 * @Description 审核出金，审核不通过时，发送异事物消息给用户加钱
 * @Date 2019/11/4 19:28
 * @Created by Longer
 *//*

@Slf4j
@RocketMQTransactionListener(txProducerGroup = "Groups-WithdrawDetuchMoney")
public class WithdrawDetuchMoneyTransactionListenerImpl implements RocketMQLocalTransactionListener {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("WithdrawDetuchMoneyTransactionListenerImpl:executeLocalTransaction,message={}", message.toString());
        try {
            OrderWithdrawDto orderWithdrawDto = RocketMQUtil.getObjFromMessage(message, OrderWithdrawDto.class);
            if (orderWithdrawDto == null) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            orderWithdrawService.createOrder(orderWithdrawDto);
            log.info("出金申请，扣钱消息正式发送",message.toString());
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("WithdrawDetuchMoneyTransactionListenerImpl:执行本地事务异常,消息回滚", e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("checkLocalTransaction,message={}", message.toString());
        try {
            OrderWithdrawDto orderWithdrawDto = RocketMQUtil.getObjFromMessage(message, OrderWithdrawDto.class);
            if (orderWithdrawDto == null) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            OrderWithdraw orderWithdraw = orderWithdrawService.getByOrderNo(orderWithdrawDto.getOrderNo());
            boolean flag = orderWithdraw != null;
            return flag ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
        } catch (Exception e) {
            log.error("回查事务异常,消息回滚", e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
*/
