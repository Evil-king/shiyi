package com.baibei.shiyi.admin.modules.account.dao;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface AccountBalanceMapper extends MyMapper<AccountBalance> {

    List<AccountBalanceVo> findPageList(AccountBalanceDto accountBalanceDto);

    /**
     * 查询重复的数据
     *
     * @param accountBalance
     * @return
     */
    List<AccountBalanceVo> duplicateList(AccountBalance accountBalance);
}