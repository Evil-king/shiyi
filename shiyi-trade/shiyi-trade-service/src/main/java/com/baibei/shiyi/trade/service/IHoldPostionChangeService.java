package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.model.HoldDetails;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/11 13:44
 * @description:
 */
public interface IHoldPostionChangeService {
    /**
     * 冻结客户持仓
     *
     * @param customerNo
     * @param productTradeNo
     * @param count
     */
    void frozen(String customerNo, String productTradeNo, Integer count);

    /**
     * 解冻持仓
     *
     * @param customerNo
     * @param productTradeNo
     * @param count
     */
    void unfreeze(String customerNo, String productTradeNo, Integer count);


    /**
     * 解冻并扣减持仓
     *
     * @param bo
     */
    void unfreezeAndDeduct(ChangeHoldPositionBo bo);


    /**
     * 扣减客户持仓
     *
     * @param bo
     */
    void deduct(ChangeHoldPositionBo bo);

    /**
     * 新增客户持仓
     * 默认持仓未解锁，只增加持仓总数、锁仓数
     *
     * @param bo
     */
    void increase(ChangeHoldPositionBo bo);

    /**
     * 解锁持仓
     *
     * @param details
     */
    void unlock(HoldDetails details);


    /**
     * 持仓变动操作
     *
     * @param boList
     */
    void changeHoldPosition(List<ChangeHoldPositionBo> boList);
}