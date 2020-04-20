package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.model.OrderDeposit;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface OrderDepositMapper extends MyMapper<OrderDeposit> {
    List<OrderDeposit> selectPeriodOrderList(String batchNo);
}