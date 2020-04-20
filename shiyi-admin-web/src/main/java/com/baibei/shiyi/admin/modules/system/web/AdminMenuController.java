package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.MenuPageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.MenuVo;
import com.baibei.shiyi.admin.modules.security.constants.AuthorityExpression;
import com.baibei.shiyi.admin.modules.system.model.Menu;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.admin.modules.system.service.IMenuService;
import com.baibei.shiyi.admin.modules.system.service.ISysUserService;
import com.baibei.shiyi.admin.modules.system.service.IUsersRolesService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/menu")
@Slf4j
public class AdminMenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IUsersRolesService usersRolesService;

    @Autowired
    private AuthorityExpression authorityExpression;


    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<MenuVo>> pageList(@RequestBody @Validated MenuPageDto menuDto) {
        return ApiResult.success(menuService.pageInfo(menuDto));
    }

    /**
     * 构建前端路由所需要的菜单和目录
     * <p>
     * 1、查询菜单对应的目录,因为包含菜单和目录
     * 2、遍历所有的目录
     *
     * @return
     */
    @PostMapping(path = "/buildMenu")
    public ApiResult<List<MenuVo>> buildMenus() {
        String userName = SecurityUtils.getUsername();
        log.info("获取当前用户[{}]的菜单", userName);
        SysUser user = userService.findByName(userName);
        List<Long> roleIdList = usersRolesService.getRoleIdList(user.getId());//查询用户包含的角色
        List<MenuVo> menuVos = menuService.findUserByMenu(roleIdList); //角色包含的资源
        return ApiResult.success(menuVos);
    }


    /**
     * 构建菜单的权限标识
     *
     * @param menuDto
     * @return
     */
    @PostMapping(path = "/save")
    public ApiResult save(@RequestBody @Validated MenuDto menuDto) {
        ApiResult apiResult = verification(menuDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return apiResult;
        }
        if (menuDto.getPid() != null) {
            if (menuDto.getPermission() != null && menuDto.getMenuType().equals(Constants.MenuType.BUTTON)) {
                Menu menu = menuService.findById(menuDto.getPid());
                menuDto.setPrefix(menu.getPermission() + "_");
            }
        }
        this.menuService.save(menuDto);
        return ApiResult.success();
    }

    @PostMapping(path = "/update")
    public ApiResult update(@RequestBody @Validated MenuDto menuDto) {
        if (menuDto.getId() == null) {
            return ApiResult.error("修改失败,id不能为空");
        }
        ApiResult apiResult = verification(menuDto);
        if (!apiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            return apiResult;
        }
        if (menuDto.getPid() != null) {
            if (menuDto.getPermission() != null && menuDto.getMenuType().equals(Constants.MenuType.BUTTON)) {
                Menu menu = menuService.findById(menuDto.getPid());
                menuDto.setPrefix(menu.getPermission() + "_");
            }
        }
        this.menuService.update(menuDto);
        return ApiResult.success();
    }

    private ApiResult verification(MenuDto menuDto) {
        if (!Constants.MenuType.DIRECTORY.equals(menuDto.getMenuType())) {
            if (menuDto.getPid() == null) {
                return ApiResult.error("父类菜单不能为空");
            }
            if (menuDto.getPermission() == null) {
                return ApiResult.error("权限标识不能为空");
            }
            if (Constants.MenuType.MENU.equals(menuDto.getMenuType())) {
                boolean result = this.menuService.onlyMenuPermission(menuDto.getMenuType(), menuDto.getPermission(), menuDto.getId());
                if (result) {
                    return ApiResult.error("菜单的权限标识必须唯一");
                }
            }
            if (Constants.MenuType.BUTTON.equals(menuDto.getMenuType())) {
                Menu menu = this.menuService.findById(menuDto.getPid());
                menuDto.setPrefix(menu.getPermission());
            }
        }
        return ApiResult.success();
    }


    @PostMapping(path = "/deleteById")
    public ApiResult deleteById(@RequestBody MenuDto menuDto) {
        if (menuDto.getId() == null) {
            return ApiResult.error("删除Id不能为空");
        }
        List<MenuVo> menuVos = this.menuService.findByPid(menuDto.getId());
        if (menuVos.size() > 0) {
            return ApiResult.error("当前菜单还有子类,无法删除");
        }
        this.menuService.deleteById(menuDto.getId());
        return ApiResult.success();
    }

    /**
     * 根据菜单类型获取不同的菜单按钮
     *
     * @return
     */
    @PostMapping(path = "/getByMenuType")
    public ApiResult<List<Map<String, Object>>> getByMenuType(@RequestBody MenuDto menuDto) {
        if (menuDto.getMenuType() == null) {
            return ApiResult.error("菜单类型不能为空");
        }
        if (Constants.MenuType.DIRECTORY.equals(menuDto.getMenuType())) {
            return ApiResult.success();
        }
        if (Constants.MenuType.MENU.equals(menuDto.getMenuType())) {
            menuDto.setMenuType(Constants.MenuType.DIRECTORY);
        }
        if (Constants.MenuType.BUTTON.equals(menuDto.getMenuType())) {
            menuDto.setMenuType(Constants.MenuType.MENU);
        }
        List<Map<String, Object>> mapList = new ArrayList<>();
        this.menuService.findByMenuType(menuDto.getMenuType()).stream().forEach(menuVo -> {
            Map<String, Object> result = new HashMap<>();
            result.put("id", menuVo.getId());
            result.put("name", menuVo.getName());
            result.put("menuType", menuVo.getMenuType());
            mapList.add(result);
        });
        return ApiResult.success(mapList);
    }

    /**
     * 查询目录的订单
     * {menuType:menuType}
     */
    @PostMapping(path = "/getMenuByOperating")
    public ApiResult<List<Map<String, Object>>> getMenuByOperating() {
        return ApiResult.success(this.menuService.getAllMenu(this.menuService.findByMenuType(Constants.MenuType.DIRECTORY)));
    }

    /**
     * 返回系统通用的权限
     *
     * @return
     */
    @PostMapping(path = "/getSystemCommonAuthority")
    public ApiResult<List<Map<String, Object>>> getSystemCommonAuthority() {
        List<Map<String, Object>> authority = authorityExpression.getSystemCommonAuthority();
        return ApiResult.success(authority);
    }

}
