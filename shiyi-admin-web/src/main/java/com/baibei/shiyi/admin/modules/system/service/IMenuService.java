package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.bean.dto.MenuDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.Menu;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;
import java.util.Map;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: Menu服务接口
 */
public interface IMenuService extends Service<Menu> {

    MyPageInfo<MenuVo> pageInfo(MenuPageDto menuDto);

    void save(MenuDto menuDto);

    void update(MenuDto menuDto);

    /**
     * @return
     */
    List<Map<String, Object>> getAllMenu(List<MenuVo> menus);


    List<MenuVo> findByPid(Long pid);

    /**
     *
     */
    List<MenuVo> findByMenuType(String menuType);

    /**
     * 获取角色具有的菜单
     *
     * @param roleIdList
     * @return
     */
    List<MenuVo> findByRoles(List<Long> roleIdList);

    /**
     * 获取用户的ID
     *
     * @return
     */
    List<Menu> getByIds(List<Long> menuIdList);

    /**
     * 验证某一类型的权限标识是唯一的
     *
     * @return
     */
    Boolean onlyMenuPermission(String menuType, String permission, Long id);

    /**
     * 查询用户的菜单
     *
     * @param roleIdList 角色Id列表
     * @return
     */
    List<MenuVo> findUserByMenu(List<Long> roleIdList);

    /**
     * 根据菜单Id获取菜单
     *
     * @return
     */
    List<MenuVo> findByMenu(Iterable<Long> menuIds);
}
