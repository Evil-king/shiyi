package com.baibei.shiyi.cash.service;

import com.baibei.shiyi.cash.feign.base.dto.DepositWithDrawDto;
import com.baibei.shiyi.cash.feign.base.vo.DepositWithdrawVo;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

public interface IDepositWithdrawService {

    MyPageInfo<DepositWithdrawVo> pageList(DepositWithDrawDto depositWithDrawDto);
}
