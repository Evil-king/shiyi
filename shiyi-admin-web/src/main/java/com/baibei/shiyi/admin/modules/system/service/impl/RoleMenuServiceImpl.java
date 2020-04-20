package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.dao.RoleMenuMapper;
import com.baibei.shiyi.admin.modules.system.model.RoleMenu;
import com.baibei.shiyi.admin.modules.system.service.IRoleMenuService;
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
 * @date: 2019/10/09 11:06:06
 * @description: RoleMenu服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleMenuServiceImpl extends AbstractService<RoleMenu> implements IRoleMenuService {

    @Autowired
    private RoleMenuMapper tblRoleMenuMapper;

    @Override
    public List<Long> getMenuIdList(Long roleId) {
        Condition condition = new Condition(RoleMenu.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        List<RoleMenu> list = findByCondition(condition);
        List<Long> result = new ArrayList<>();
        for (RoleMenu rolesMenus : list) {
            result.add(rolesMenus.getMenuId());
        }
        return result;
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(roleId);
        tblRoleMenuMapper.delete(roleMenu);
    }

    @Override
    public List<MenuVo> findByRoleId(Long roleId) {
        return tblRoleMenuMapper.findByRoleId(roleId);
    }
}
