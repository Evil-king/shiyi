package com.baibei.shiyi.user.service.impl;

import com.baibei.shiyi.user.dao.CustomerRefMapper;
import com.baibei.shiyi.user.model.CustomerRef;
import com.baibei.shiyi.user.service.ICustomerRefService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: hyc
* @date: 2019/05/24 09:44:04
* @description: CustomerRef服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerRefServiceImpl extends AbstractService<CustomerRef> implements ICustomerRefService {

    @Autowired
    private CustomerRefMapper customerRefMapper;

}
