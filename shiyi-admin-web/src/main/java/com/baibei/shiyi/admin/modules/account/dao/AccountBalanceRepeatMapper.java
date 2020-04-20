package com.baibei.shiyi.admin.modules.account.dao;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceRepeatDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceRepeatVo;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceRepeat;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface AccountBalanceRepeatMapper extends MyMapper<AccountBalanceRepeat> {

    List<AccountBalanceRepeatVo> findPageList(AccountBalanceRepeatDto accountBalanceRepeatDto);
}