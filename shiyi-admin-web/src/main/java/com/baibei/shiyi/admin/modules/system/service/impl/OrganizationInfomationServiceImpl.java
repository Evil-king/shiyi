package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.OrganizationInfomationMapper;
import com.baibei.shiyi.admin.modules.system.model.OrganizationInfomation;
import com.baibei.shiyi.admin.modules.system.service.IOrganizationInfomationService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author: hyc
 * @date: 2019/10/10 09:51:43
 * @description: OrganizationInfomation服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrganizationInfomationServiceImpl extends AbstractService<OrganizationInfomation> implements IOrganizationInfomationService {

    @Autowired
    private OrganizationInfomationMapper tblAdminOrganizationInfomationMapper;

    @Override
    public OrganizationInfomation findByOrganizationId(Long organizationId) {
        return this.findBy("organizationId", organizationId);
    }
}
