package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.bean.dto.RoleDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RoleGroupDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RolePageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.ChooseRuleVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.RoleVo;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: Role服务接口
 */
public interface IRoleService extends Service<Role> {

    void addRoleGroup(RoleGroupDto roleGroupDto);

    void addRole(RoleDto roleDto);

    void updateRoleGroup(RoleGroupDto roleGroupDto);

    void updateRole(RoleDto roleDto);

    List<RoleVo> pageList(RolePageDto rolePageDto);

    MyPageInfo<RoleVo> pageInfo(RolePageDto rolePageDto);

    void deleteByRole(Long id);

    List<Role> findByPid(Long pid);

    /**
     * 添加角色对应的菜单
     *
     * @param roleDto
     */
    void addRoleMenu(RoleDto roleDto);

    List<ChooseRuleVo> getAll();
}
