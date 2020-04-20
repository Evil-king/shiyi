package com.baibei.shiyi.admin.modules.system.dao;

import com.baibei.shiyi.admin.modules.system.bean.dto.MenuPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.Menu;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends MyMapper<Menu> {

    List<MenuVo> findByMenuList(MenuPageDto menuDto);

    List<MenuVo> findByPidMenuList(@Param(value = "pid") Long pid);

    List<MenuVo> findByMenuType(@Param(value = "menuType") String menuType);

    /**
     * 根据菜单Id获取
     * @param menuIds
     * @return
     */
    List<MenuVo> findByMenuTypeList(@Param(value = "menuIds") Iterable<Long> menuIds);
}