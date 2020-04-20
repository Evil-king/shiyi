package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.BranchOfficeProxyMapper;
import com.baibei.shiyi.admin.modules.system.model.BranchOfficeProxy;
import com.baibei.shiyi.admin.modules.system.service.IBranchOfficeProxyService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: BranchOfficeProxy服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BranchOfficeProxyServiceImpl extends AbstractService<BranchOfficeProxy> implements IBranchOfficeProxyService {

    @Autowired
    private BranchOfficeProxyMapper tblAdminBranchOfficeProxyMapper;

    @Override
    public void deleteByBranchCompanyId(Long branchCompanyId) {
        Condition condition = new Condition(BranchOfficeProxy.class);
        condition.createCriteria().andEqualTo("branchCompanyId", branchCompanyId);
        tblAdminBranchOfficeProxyMapper.deleteByCondition(condition);
    }
}
