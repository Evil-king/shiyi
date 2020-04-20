package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.model.UsersRoles;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface UsersRolesMapper extends MyMapper<UsersRoles> {
    List<Role> selectRoleByUserId(Long userId);
}