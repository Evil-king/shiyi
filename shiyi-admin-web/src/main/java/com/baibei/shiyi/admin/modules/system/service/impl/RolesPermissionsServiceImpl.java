package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.RolesPermissionsMapper;
import com.baibei.shiyi.admin.modules.system.model.RolesPermissions;
import com.baibei.shiyi.admin.modules.system.service.IRolesPermissionsService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: RolesPermissions服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolesPermissionsServiceImpl extends AbstractService<RolesPermissions> implements IRolesPermissionsService {

    @Autowired
    private RolesPermissionsMapper tblAdminRolesPermissionsMapper;


    @Override
    public List<RolesPermissions> getRefByPermissionId(Long permissionId) {
        Condition condition = new Condition(RolesPermissions.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("permissionId", permissionId);
        List<RolesPermissions> list = findByCondition(condition);
        return list;
    }

    @Override
    public List<Long> getPermissionList(List<Long> roleIdList) {
        Condition condition = new Condition(RolesPermissions.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andIn("roleId", roleIdList);
        List<RolesPermissions> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (RolesPermissions rolesPermissions : list) {
            result.add(rolesPermissions.getPermissionId());
        }
        return result;
    }
}
