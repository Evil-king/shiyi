package com.baibei.shiyi.admin.modules.system.web;

import com.baibei.shiyi.admin.modules.system.bean.dto.AddUserDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.ChangePasswordDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.UserPageListDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.UserPageListVo;
import com.baibei.shiyi.admin.modules.system.service.ISysUserService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/10/15 15:25
 * @description:
 */
@RestController
@RequestMapping("/admin/user")
public class AdminSysUserController {
    @Autowired
    private ISysUserService sysUserService;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('ACCESS_MANAGE_USER'))")
    public ApiResult<MyPageInfo<UserPageListVo>> pageList(@RequestBody @Validated UserPageListDto userPageListDto) {
        return ApiResult.success(sysUserService.pageList(userPageListDto));
    }

    @RequestMapping("/addOrEdit")
    public ApiResult addOrEdit(@RequestBody @Validated AddUserDto addUserDto) {
        if(!StringUtils.isEmpty(addUserDto.getPassword())&&!StringUtils.isEmpty(addUserDto.getRepeatPassword())){
            if(!addUserDto.getPassword().equals(addUserDto.getRepeatPassword())){
                return ApiResult.badParam("两次输入密码不一致");
            }
        }
        return sysUserService.addOrEdit(addUserDto);
    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping("/edit")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('ACCESS_MANAGE_USER'))")
    public ApiResult edit(@RequestBody @Validated AddUserDto addUserDto) {
        if (addUserDto.getId() == null) {
            return ApiResult.error("用户类型不能为空");
        }
        if(!StringUtils.isEmpty(addUserDto.getPassword())&&!StringUtils.isEmpty(addUserDto.getRepeatPassword())){
            if(!addUserDto.getPassword().equals(addUserDto.getRepeatPassword())){
                return ApiResult.badParam("两次输入密码不一致");
            }
        }
        this.sysUserService.edit(addUserDto);
        return ApiResult.success();
    }

    /**
     * 添加
     * @param addUserDto
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('ACCESS_MANAGE_USER'))")
    public ApiResult add(@RequestBody @Validated AddUserDto addUserDto) {
        if(!StringUtils.isEmpty(addUserDto.getPassword())&&!StringUtils.isEmpty(addUserDto.getRepeatPassword())){
            if(!addUserDto.getPassword().equals(addUserDto.getRepeatPassword())){
                return ApiResult.badParam("两次输入密码不一致");
            }
        }
        this.sysUserService.add(addUserDto);
        return ApiResult.success();
    }

    @RequestMapping("/changePassword")
    private ApiResult changePassword(@RequestBody @Validated ChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatPassword())) {
            return ApiResult.badParam("两次输入密码不一致");
        }
        return sysUserService.changePassword(changePasswordDto);
    }
}
