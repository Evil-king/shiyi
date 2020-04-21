package com.baibei.shiyi.order.transactional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/20 16:35
 * @description: 事务提交回调
 */
@Component
public class TransactionalCallbackService {
    /**
     * 事务提交之后执行回调action
     *
     * @param action
     */
    public void execute(final ICallbackAction action) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                action.callback();
            }
        });
    }
}