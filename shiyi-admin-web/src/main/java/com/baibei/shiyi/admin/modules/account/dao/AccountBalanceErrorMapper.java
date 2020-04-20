package com.baibei.shiyi.admin.modules.account.dao;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceErrorDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceErrorVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceError;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface AccountBalanceErrorMapper extends MyMapper<AccountBalanceError> {

    List<AccountBalanceErrorVo> findPageList(AccountBalanceErrorDto accountBalanceErrorDto);
}