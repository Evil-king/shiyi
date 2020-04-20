package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.model.Account;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AccountMapper extends MyMapper<Account> {
    Integer updateFreezingAmount(Account account);

    List<SumWithdrawAndDepositVo> sumWithdrawAndDeposit();

    void synchronizationBalance();
}