package com.baibei.shiyi.cash.rocketmq.listeners;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

/**
 * @Classname AuditWithdrawTransactionListenerImpl
 * @Description 审核出金，审核不通过时，发送异事物消息给用户加钱
 * @Date 2019/11/4 19:28
 * @Created by Longer
 */
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "Groups-Apply1318")
public class Apply1318TransactionListenerImpl implements RocketMQLocalTransactionListener {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        log.info("Apply1318TransactionListenerImpl:executeLocalTransaction,message={}", message.toString());
        try {
            PABSendDto pabSendDto = RocketMQUtil.getObjFromMessage(message, PABSendDto.class);
            if (pabSendDto == null) {
                return RocketMQLocalTransactionState.ROLLBACK;
            }
            OrderWithdraw orderWithdraw = JacksonUtil.jsonToBean(o.toString(),OrderWithdraw.class);
            //更新订单状态为（系统处理中状态）
            log.info("将出金状态改成“系统处理中”,orderInfo：",orderWithdraw);
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_SYS_DOING);
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, orderWithdraw.getOrderNo());
            if(i==0){
                throw new ServiceException("1318更新订单状态为'系统处理中'失败");
            }
            log.info("请求1318接口消息正式发送 orderInfo：",orderWithdraw);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            log.error("Apply1318TransactionListenerImpl:执行本地事务异常,消息回滚", e);
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
