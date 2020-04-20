package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.dao.UsersRolesMapper;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.model.UsersRoles;
import com.baibei.shiyi.admin.modules.system.service.IUsersRolesService;
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
 * @description: UsersRoles服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UsersRolesServiceImpl extends AbstractService<UsersRoles> implements IUsersRolesService {

    @Autowired
    private UsersRolesMapper tblAdminUsersRolesMapper;

    @Override
    public List<Long> getRoleIdList(Long userId) {
        Condition condition = new Condition(UsersRoles.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("userId", userId);
        List<UsersRoles> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (UsersRoles usersRoles : list) {
            result.add(usersRoles.getRoleId());
        }
        return result;
    }

    @Override
    public void deleteByUserRole(Long userId) {
        Condition condition = new Condition(UsersRoles.class);
        condition.createCriteria().andEqualTo("userId", userId);
        tblAdminUsersRolesMapper.deleteByCondition(condition);
    }

    @Override
    public List<Role> selectRoleByUserId(Long userId) {
        return tblAdminUsersRolesMapper.selectRoleByUserId(userId);
    }

    @Override
    public void insertOne(UsersRoles usersRoles) {
        tblAdminUsersRolesMapper.insertSelective(usersRoles);
    }
}
