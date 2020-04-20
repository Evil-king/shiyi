package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.feign.base.dto.WithDrawDepositDiffDto;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.model.WithDrawDepositDiff;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface WithDrawDepositDiffMapper extends MyMapper<WithDrawDepositDiff> {

    List<WithDrawDepositDiffVo> myList(WithDrawDepositDiffDto withDrawDepositDiffDto);
}