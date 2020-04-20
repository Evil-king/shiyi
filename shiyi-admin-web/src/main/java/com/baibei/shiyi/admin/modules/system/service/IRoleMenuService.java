package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.RoleMenu;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/10/09 11:06:06
 * @description: RoleMenu服务接口
 */
public interface IRoleMenuService extends Service<RoleMenu> {


    List<Long> getMenuIdList(Long roleId);

    void deleteByRoleId(Long roleId);

    List<MenuVo> findByRoleId(Long roleId);
}
