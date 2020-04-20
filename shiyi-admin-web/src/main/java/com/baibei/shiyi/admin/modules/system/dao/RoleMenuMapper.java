package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.RoleMenu;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuMapper extends MyMapper<RoleMenu> {

    List<MenuVo> findByRoleId(@Param("roleId") Long roleId);
}