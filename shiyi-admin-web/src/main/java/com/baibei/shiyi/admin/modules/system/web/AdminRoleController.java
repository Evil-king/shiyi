package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.RoleDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RoleGroupDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.RolePageDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.ChooseRuleVo;
import com.baibei.shiyi.admin.modules.system.bean.vo.RoleVo;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.service.IRoleService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {


    @Autowired
    private IRoleService roleService;

    /**
     * 添加角色组
     *
     * @return
     */
    @PostMapping("/addRoleGroup")
    public ApiResult addRoleGroup(@Validated @RequestBody RoleGroupDto roleGroupDto) {
        roleService.addRoleGroup(roleGroupDto);
        return ApiResult.success();
    }

    /**
     * 查询角色列表信息
     *
     * @return
     */
    @PostMapping(path = "/pageList")
    public ApiResult<List<RoleVo>> pageList(@RequestBody RolePageDto rolePageDto) {
        rolePageDto.setRoleType(Constants.RoleType.GROUP);
        return ApiResult.success(roleService.pageList(rolePageDto));
    }

    /**
     * 修改角色组
     *
     * @return
     */
    @PostMapping("/updateRoleGroup")
    public ApiResult updateRoleGroup(@Validated @RequestBody RoleGroupDto roleGroupDto) {
        if (roleGroupDto.getId() == null) {
            return ApiResult.error("角色组Id不能为空");
        }
        roleService.updateRoleGroup(roleGroupDto);
        return ApiResult.success();
    }

    /**
     * 修改角色
     *
     * @return
     */
    @PostMapping("/updateRole")
    public ApiResult updateRole(@Validated @RequestBody RoleDto roleDto) {
        if (roleDto.getId() == null) {
            return ApiResult.error("角色id不能为空");
        }
        roleService.updateRole(roleDto);
        return ApiResult.success();
    }

    /**
     * 添加角色
     *
     * @return
     */
    @PostMapping("/addRole")
    public ApiResult addRole(@Validated @RequestBody RoleDto roleDto) {
        if (roleDto.getPid() == null) {
            return ApiResult.error("角色组不能为空");
        }
        roleService.addRole(roleDto);
        return ApiResult.success();
    }

    /**
     * 删除角色或者角色组
     *
     * @param roleDto
     * @return
     */
    @PostMapping("/deleteById")
    public ApiResult deleteById(@RequestBody RoleDto roleDto) {
        if (roleDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        this.roleService.deleteByRole(roleDto.getId());
        return ApiResult.success();
    }

    /**
     * 添加角色给菜单权限
     *
     * @return
     */
    @PostMapping("/addRoleMenu")
    public ApiResult addRoleMenu(@RequestBody RoleDto roleDto) {
        if (roleDto.getId() == null || roleDto.getMenuId().isEmpty()) {
            return ApiResult.error("角色Id不能为空");
        }
        Role role = roleService.findById(roleDto.getId());
        if (!Constants.RoleType.ONE.equals(role.getRoleType())) {
            return ApiResult.error("当前角色类型不支持添加菜单");
        }
        roleService.addRoleMenu(roleDto);
        return ApiResult.success();
    }

    @PostMapping("/getAll")
    private ApiResult<List<ChooseRuleVo>> getAll(){
        return ApiResult.success(roleService.getAll());
    }
}
