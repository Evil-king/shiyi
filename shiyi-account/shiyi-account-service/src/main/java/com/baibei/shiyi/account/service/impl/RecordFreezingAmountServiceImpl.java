package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.dao.RecordFreezingAmountMapper;
import com.baibei.shiyi.account.model.RecordFreezingAmount;
import com.baibei.shiyi.account.service.IRecordFreezingAmountService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: hyc
* @date: 2019/05/24 10:39:07
* @description: RecordFreezingAmount服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordFreezingAmountServiceImpl extends AbstractService<RecordFreezingAmount> implements IRecordFreezingAmountService {

    @Autowired
    private RecordFreezingAmountMapper recordFreezingAmountMapper;

}
