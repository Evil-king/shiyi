package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.feign.base.dto.DepositWithDrawDto;
import com.baibei.shiyi.cash.feign.base.vo.DepositWithdrawVo;
import com.baibei.shiyi.cash.model.FuqingAccountDeposit;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface FuqingAccountDepositMapper extends MyMapper<FuqingAccountDeposit> {

    List<DepositWithdrawVo> pageList(DepositWithDrawDto depositWithDrawDto);
}