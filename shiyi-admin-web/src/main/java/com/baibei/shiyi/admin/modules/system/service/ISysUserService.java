package com.baibei.shiyi.admin.modules.system.service;

import com.baibei.shiyi.admin.modules.system.bean.dto.AddUserDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.ChangePasswordDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.UserPageListDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.UserPageListVo;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.admin.modules.system.model.User;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;


/**
 * @author: hyc
 * @date: 2019/10/15 15:40:22
 * @description: SysUser服务接口
 */
public interface ISysUserService extends Service<SysUser> {

    MyPageInfo<UserPageListVo> pageList(UserPageListDto userPageListDto);

    /**
     * 修改后台用户的基本信息
     *
     * @param addUserDto
     * @return
     */
    ApiResult addOrEdit(AddUserDto addUserDto);

    /**
     * 编辑
     *
     * @param addUserDto
     * @return
     */
    ApiResult edit(AddUserDto addUserDto);

    /**
     * 添加
     * @param addUserDto
     * @return
     */
    ApiResult add(AddUserDto addUserDto);

    /**
     * 修改密码
     *
     * @param changePasswordDto
     * @return
     */
    ApiResult changePassword(ChangePasswordDto changePasswordDto);


    SysUser findByName(String username);
}
