package com.baibei.shiyi.cash.service.impl;

import com.baibei.shiyi.cash.dao.FuqingAccountDepositMapper;
import com.baibei.shiyi.cash.model.FuqingAccountDeposit;
import com.baibei.shiyi.cash.service.IFuqingAccountDepositService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;



/**
 * @author: uqing
 * @date: 2019/12/11 17:12:38
 * @description: FuqingAccountDeposit服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FuqingAccountDepositServiceImpl extends AbstractService<FuqingAccountDeposit> implements IFuqingAccountDepositService {

    @Autowired
    private FuqingAccountDepositMapper tblFuqingAccountDepositMapper;

}
