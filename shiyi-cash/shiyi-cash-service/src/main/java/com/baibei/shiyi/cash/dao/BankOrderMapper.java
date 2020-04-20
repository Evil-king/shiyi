package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.bean.vo.SumBankOrderVo;
import com.baibei.shiyi.cash.feign.base.dto.BankOrderDto;
import com.baibei.shiyi.cash.feign.base.vo.BankOrderVo;
import com.baibei.shiyi.cash.model.BankOrder;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface BankOrderMapper extends MyMapper<BankOrder> {

    List<BankOrderVo> myList(BankOrderDto bankOrderDto);

    SumBankOrderVo getSumData(BankOrderDto bankOrderDto);
}