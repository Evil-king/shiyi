package com.baibei.shiyi.admin.modules.account.service.impl;

import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceErrorDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceErrorVo;
import com.baibei.shiyi.admin.modules.account.dao.AccountBalanceErrorMapper;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceError;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceErrorService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/11/29 11:22:11
 * @description: accountBalanceError服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountBalanceErrorServiceImpl extends AbstractService<AccountBalanceError> implements IAccountBalanceErrorService {

    @Autowired
    private AccountBalanceErrorMapper tblAdminAccountBalanceErrorMapper;

    @Override
    public MyPageInfo<AccountBalanceErrorVo> findPageList(AccountBalanceErrorDto accountBalanceError) {
        if (accountBalanceError.getCurrentPage() != null && accountBalanceError.getPageSize() != null) {
            PageHelper.startPage(accountBalanceError.getCurrentPage(), accountBalanceError.getPageSize());
        }
        List<AccountBalanceErrorVo> accountBalanceErrorVos = tblAdminAccountBalanceErrorMapper.findPageList(accountBalanceError);
        MyPageInfo<AccountBalanceErrorVo> pageInfo = new MyPageInfo<>(accountBalanceErrorVos);
        return pageInfo;
    }
}
