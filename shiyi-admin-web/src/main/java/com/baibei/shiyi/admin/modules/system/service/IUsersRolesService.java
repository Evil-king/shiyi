package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.model.UsersRoles;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: UsersRoles服务接口
 */
public interface IUsersRolesService extends Service<UsersRoles> {

    List<Long> getRoleIdList(Long userId);


    /**
     * 根据用户对应的角色
     *
     * @param userId
     */
    void deleteByUserRole(Long userId);

    List<Role> selectRoleByUserId(Long id);

    void insertOne(UsersRoles usersRoles);
}
