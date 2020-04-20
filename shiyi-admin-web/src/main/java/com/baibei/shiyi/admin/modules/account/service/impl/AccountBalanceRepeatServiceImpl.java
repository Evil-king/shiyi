package com.baibei.shiyi.admin.modules.account.service.impl;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceRepeatDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceRepeatVo;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.dao.AccountBalanceRepeatMapper;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceRepeat;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceRepeatService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/12/02 22:57:52
 * @description: AccountBalanceRepeat服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountBalanceRepeatServiceImpl extends AbstractService<AccountBalanceRepeat> implements IAccountBalanceRepeatService {

    @Autowired
    private AccountBalanceRepeatMapper tblAdminAccountBalanceRepeatMapper;

    @Override
    public MyPageInfo<AccountBalanceRepeatVo> findPageList(AccountBalanceRepeatDto accountBalanceRepeat) {
        if (accountBalanceRepeat.getCurrentPage() != null && accountBalanceRepeat.getPageSize() != null) {
            PageHelper.startPage(accountBalanceRepeat.getCurrentPage(), accountBalanceRepeat.getPageSize());
        }
        accountBalanceRepeat.setStartTime(new Date());
        accountBalanceRepeat.setEndTime(new Date());
        List<AccountBalanceRepeatVo> pageList = tblAdminAccountBalanceRepeatMapper.findPageList(accountBalanceRepeat);
        return new MyPageInfo<>(pageList);
    }
}
