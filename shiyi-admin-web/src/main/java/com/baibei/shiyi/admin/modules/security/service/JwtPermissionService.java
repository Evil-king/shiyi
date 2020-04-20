package com.baibei.shiyi.admin.modules.security.service;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.system.bean.dto.UserDTO;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.system.model.Permission;
import com.baibei.shiyi.admin.modules.system.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sun.nio.cs.ext.MacCentralEurope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtPermissionService {

    @Autowired
    private IUsersRolesService usersRolesService;
    @Autowired
    private IRolesPermissionsService rolesPermissionsService;
    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IMenuService menuService;


    public Collection<GrantedAuthority> mapToGrantedAuthorities(UserDTO user) {
        System.out.println("--------------------loadPermissionByUser:" + user.getUsername() + "---------------------");
//        List<Permission> permissionList = getPermission(user.getId());
        List<MenuVo> menuVos = getMenuPermission(user.getId());
        if (CollectionUtils.isEmpty(menuVos)) {
            return new ArrayList<>();
        }
        // stop 权限控制
        return menuVos.stream().filter(x -> !StringUtils.isEmpty(x.getPermission())).map(
                result -> {
                    String permission = result.getPermission();
                    if (!StringUtils.isEmpty(result.getPrefix())) {
                        permission = result.getPrefix() + permission;
                    }
                    return new SimpleGrantedAuthority(permission);
                }
        ).collect(Collectors.toList());

    }

    /**
     * 获取用户权限e
     *
     * @param userId
     * @return
     */
    private List<Permission> getPermission(Long userId) {
        // 查询用户角色ID列表
        List<Long> roleIdList = usersRolesService.getRoleIdList(userId);
        // 查询角色有的权限列表
        List<Long> permissionIdList = rolesPermissionsService.getPermissionList(roleIdList);
        if (!CollectionUtils.isEmpty(permissionIdList)) {
            return permissionService.listByIds(permissionIdList);
        }
        return null;
    }

    /**
     * 根据用户获取角色信息,获取用户具有的所有的菜单的权限标识
     *
     * @param userId
     * @return
     */
    private List<MenuVo> getMenuPermission(Long userId) {
        List<Long> roleIdList = usersRolesService.getRoleIdList(userId);
        List<MenuVo> menuList = menuService.findByRoles(roleIdList);
        return menuList;
    }
}
