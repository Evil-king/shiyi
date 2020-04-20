package com.baibei.shiyi.cash.service;

import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.model.OrderDeposit;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/11/02 17:09:32
 * @description: OrderDeposit服务接口
 */
public interface IOrderDepositService extends Service<OrderDeposit> {

    /**
     * 入金
     *
     * @return
     */
    PABDepositVo deposit(PABDepositDto depositDto);

    /**
     * 入金流水
     * @return
     */
    List<OrderDeposit> getPeriodOrderList(String batchNo);

    int safetyUpdateOrderBySelective(OrderDeposit updateEntity, String orderNo);

    OrderDeposit getByOrderNo(String orderNo);


    /**
     * 根据外部流水号查询入金流水(1005)
     * @param externalNo
     * @return
     */
    OrderDeposit getOrderByExternalNo(String externalNo);

    OrderDeposit getOrderByOrderNo(String orderNo);
}
