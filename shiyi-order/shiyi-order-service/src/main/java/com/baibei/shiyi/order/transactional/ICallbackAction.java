package com.baibei.shiyi.order.transactional;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/20 16:35
 * @description: 事务提交之后的回调action
 */
public interface ICallbackAction {

    /**
     * 回调执行动作
     */
    void callback();

}