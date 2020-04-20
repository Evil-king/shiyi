package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.system.bean.dto.MenuDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.dao.MenuMapper;
import com.baibei.shiyi.admin.modules.system.model.Menu;
import com.baibei.shiyi.admin.modules.system.service.IMenuService;
import com.baibei.shiyi.admin.modules.system.service.IRoleMenuService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: uqing
 * @date: 2019/08/07 09:05:52
 * @description: Menu服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MenuServiceImpl extends AbstractService<Menu> implements IMenuService {

    @Autowired
    private MenuMapper tblAdminMenuMapper;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    public MyPageInfo<MenuVo> pageInfo(MenuPageDto menuDto) {
        List<MenuVo> menuVosList = pageList(menuDto);
        MyPageInfo<MenuVo> myPageInfo = new MyPageInfo<>(menuVosList);
        return myPageInfo;
    }

    public List<MenuVo> pageList(MenuPageDto menuPageDto) {
        if (menuPageDto.getCurrentPage() != null && menuPageDto.getPageSize() != null) {
            PageHelper.startPage(menuPageDto.getCurrentPage(), menuPageDto.getPageSize());
        }
        menuPageDto.setPid(0L);
        List<MenuVo> menuVoList = tblAdminMenuMapper.findByMenuList(menuPageDto);
        for (MenuVo menuVo : menuVoList) { //查询目录
            menuVo.setChildList(childList(menuVo));
        }
        return menuVoList;
    }

    /**
     * 子菜单
     *
     * @return
     */
    public List<MenuVo> childList(MenuVo menuVo) {
        List<MenuVo> menuVoList = tblAdminMenuMapper.findByPidMenuList(menuVo.getId());
        menuVo.setChildList(menuVoList);
        if (!menuVoList.isEmpty()) {
            for (MenuVo childMenu : menuVoList) {
                childMenu.setChildList(childList(childMenu));
            }
        }
        return menuVoList;
    }

    @Override
    public void save(MenuDto menuDto) {
        Menu menu = BeanUtil.copyProperties(menuDto, Menu.class);
        if (menuDto.getPid() == null && Constants.MenuType.DIRECTORY.equals(menuDto.getMenuType())) {
            menu.setPid(0L);
        }
        menu.setId(IdWorker.getId());
        menu.setFlag(new Byte(Constants.Flag.VALID));
        menu.setCreateTime(new Date());
        menu.setModifyTime(new Date());
        menu.setPrefix(menuDto.getPrefix());
        this.save(menu);
    }


    @Override
    public void update(MenuDto menuDto) {
        Menu menu = BeanUtil.copyProperties(menuDto, Menu.class);
        if (menuDto.getPid() == null && Constants.MenuType.DIRECTORY.equals(menuDto.getMenuType())) {
            menu.setPid(0L);
        }
        this.update(menu);
    }

    @Override
    public List<Map<String, Object>> getAllMenu(List<MenuVo> menus) {
        List<Map<String, Object>> list = new LinkedList<>();
        menus.stream().forEach(menu -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("label", menu.getName());
            map.put("menuType", menu.getMenuType());
            map.put("children", getAllMenu(this.findByPid(menu.getId())));

            list.add(map);
        });
        return list;
    }


    @Override
    public List<MenuVo> findByPid(Long pid) {
        return tblAdminMenuMapper.findByPidMenuList(pid);
    }

    @Override
    public List<MenuVo> findByMenuType(String menuType) {
        return this.tblAdminMenuMapper.findByMenuType(menuType);
    }


    @Override
    public List<MenuVo> findByRoles(List<Long> roleIdList) {
        List<MenuVo> menus = new LinkedList<>();
        for (Long roleId : roleIdList) {
            List<MenuVo> menuIdList = this.roleMenuService.findByRoleId(roleId);
            menus.addAll(menuIdList);
        }
        return menus;
    }

    @Override
    public List<Menu> getByIds(List<Long> menuIdList) {
        if (!CollectionUtils.isEmpty(menuIdList)) {
            Condition condition = new Condition(Menu.class);
            Example.Criteria criteria = condition.createCriteria();
            criteria.andIn("id", menuIdList);
            criteria.andNotEqualTo("menuType", Constants.MenuType.BUTTON);
            return findByCondition(condition);
        }
        return new ArrayList<>();
    }

    @Override
    public Boolean onlyMenuPermission(String menuType, String permission, Long id) {
        Condition condition = new Condition(Menu.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("menuType", menuType);
        if (id != null) {
            criteria.andNotEqualTo("id", id);
        }
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        criteria.andEqualTo("permission", permission);
        List<Menu> list = this.findByCondition(condition);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }


    /**
     * 1、当前端只勾选某一个按钮的时候，前端不会发送菜单Id和目录Id给我，这样构建菜单的时候就导致没有菜单，需要遍历按钮去获取上级
     * 2、当前端没有勾选按钮，或者只勾选菜单和目录
     * 3、
     *
     * @param roleIdList 角色Id列表
     * @return
     */
    @Override
    public List<MenuVo> findUserByMenu(List<Long> roleIdList) {
        List<MenuVo> menus = new LinkedList<>();
        for (Long roleId : roleIdList) {
            List<MenuVo> menuIdList = this.roleMenuService.findByRoleId(roleId);
            menus.addAll(menuIdList);
        }
        Map<String, List<MenuVo>> menuGroup = menus.stream().collect(Collectors.groupingBy(menuVo -> menuVo.getMenuType()));

        // 按钮
        // 用户拥有多个角色出现多个菜单
        List<MenuVo> buttonGroupList = menuGroup.get(Constants.MenuType.BUTTON);

        // 菜单
        List<MenuVo> menuGroupList = menuGroup.get(Constants.MenuType.MENU);

        // 目录
        List<MenuVo> directoryGroupList = menuGroup.get(Constants.MenuType.DIRECTORY);

        // 查询按钮的所有的菜单
        if (buttonGroupList != null) {
            buttonGroupList = this.deduplication(buttonGroupList);
            menuGroupList = getUniqueMenuVo(menuGroupList, buttonGroupList);
        }
        menuGroupList = this.deduplication(menuGroupList);

        if (!CollectionUtils.isEmpty(menuGroupList)) {
            directoryGroupList = getUniqueMenuVo(directoryGroupList, menuGroupList);
        }

        Boolean isMenuNull = CollectionUtils.isEmpty(menuGroupList);

        Boolean isButtonNull = CollectionUtils.isEmpty(buttonGroupList);

        //找出菜单包含的所有的目录
        if (!CollectionUtils.isEmpty(directoryGroupList)) {
            directoryGroupList = this.deduplication(directoryGroupList);
            if (isMenuNull) {
                return directoryGroupList;
            }
            for (MenuVo directory : directoryGroupList) { //遍历每一层目录
                List<MenuVo> menuVos = new ArrayList<>();
                for (MenuVo menu : menuGroupList) { //遍历每一层菜单
                    if (menu.getPid().equals(directory.getId())) {
                        menuVos.add(menu);
                    }

                    if (isButtonNull) {
                        continue;
                    }
                    List<Map<String, Object>> MenuButtonList = new ArrayList<>();
                    for (MenuVo button : buttonGroupList) {
                        if (button.getPid().equals(menu.getId())) {
                            Map<String, Object> buttonMap = new HashMap<>();
                            buttonMap.put("id", button.getId());
                            buttonMap.put("name", button.getName());
                            buttonMap.put("permission", button.getPermission());
                            MenuButtonList.add(buttonMap);
                        }
                    }
                    menu.setButtonList(MenuButtonList);
                }
                directory.setChildList(menuVos);
            }
        } else {
            directoryGroupList = new ArrayList<>();
        }
        return directoryGroupList;
    }

    @Override
    public List<MenuVo> findByMenu(Iterable<Long> menuIds) {
        return this.tblAdminMenuMapper.findByMenuTypeList(menuIds);
    }

    /**
     * 根据子类pid获取所有的父类的菜单
     *
     * @param fatherList
     * @param childList
     * @return
     */
    private List<MenuVo> getUniqueMenuVo(List<MenuVo> fatherList, List<MenuVo> childList) {
        if (!CollectionUtils.isEmpty(fatherList)) {
            Set<Long> menuSet = childList.stream().map(child -> child.getPid()).collect(Collectors.toSet());
            List<MenuVo> menuList = findByMenu(menuSet);
            if (menuList != null) {
                fatherList.addAll(menuList);
            }
            return fatherList;
        }
        return fatherList;
    }


    private List<MenuVo> deduplication(List<MenuVo> menuVoList) {
        if (!CollectionUtils.isEmpty(menuVoList)) {
            menuVoList = menuVoList.stream().collect(Collectors
                    .collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> {
                        // 根据Id进行去重
                        return o.getId();
                    }))), ArrayList::new));
            return menuVoList;
        }
        return menuVoList;
    }

}
