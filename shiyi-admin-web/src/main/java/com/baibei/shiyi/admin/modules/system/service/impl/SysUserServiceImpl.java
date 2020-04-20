package com.baibei.shiyi.admin.modules.system.service.impl;

import com.baibei.shiyi.admin.modules.security.utils.JwtTokenUtil;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.system.bean.dto.AddUserDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.ChangePasswordDto;
import com.baibei.shiyi.admin.modules.system.bean.dto.UserPageListDto;
import com.baibei.shiyi.admin.modules.system.bean.vo.UserPageListVo;
import com.baibei.shiyi.admin.modules.system.dao.SysUserMapper;
import com.baibei.shiyi.admin.modules.system.model.Role;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.admin.modules.system.model.User;
import com.baibei.shiyi.admin.modules.system.model.UsersRoles;
import com.baibei.shiyi.admin.modules.system.service.ISysUserService;
import com.baibei.shiyi.admin.modules.system.service.IUsersRolesService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.MD5Util;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/10/15 15:40:22
 * @description: SysUser服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends AbstractService<SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper tblAdminSysUserMapper;

    @Autowired
    private IUsersRolesService usersRolesService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public MyPageInfo<UserPageListVo> pageList(UserPageListDto userPageListDto) {
        PageHelper.startPage(userPageListDto.getCurrentPage(), userPageListDto.getPageSize());
        List<UserPageListVo> userPageListVos = tblAdminSysUserMapper.findListVo(userPageListDto);
        for (int i = 0; i < userPageListVos.size(); i++) {
            List<Long> roleIds = new ArrayList<>();
            List<Role> roleNames = usersRolesService.selectRoleByUserId(userPageListVos.get(i).getId());
            String roleName = "";
            for (int j = 0; j < roleNames.size(); j++) {
                roleName = roleName + roleNames.get(j).getName() + " ";
                roleIds.add(roleNames.get(j).getId());
            }
            userPageListVos.get(i).setRoleName(roleName);
            userPageListVos.get(i).setRoleIds(roleIds);
        }
        MyPageInfo<UserPageListVo> myPageInfo = new MyPageInfo<>(userPageListVos);
        return myPageInfo;
    }

    @Override
    public ApiResult addOrEdit(AddUserDto addUserDto) {
        //获取当前用户的用户名
        if (StringUtils.isEmpty(addUserDto.getId())) {
            this.add(addUserDto);
        } else {
            //此处为编辑
            this.edit(addUserDto);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult edit(AddUserDto addUserDto) {
        SysUser sysUser = tblAdminSysUserMapper.selectByPrimaryKey(addUserDto.getId());
        if(!(StringUtils.isEmpty(addUserDto.getPassword())||StringUtils.isEmpty(addUserDto.getRepeatPassword()))){
            sysUser.setPassword(MD5Util.md5Hex(addUserDto.getPassword(),sysUser.getSalt()));
        }
        sysUser.setRealname(addUserDto.getRealname());
        sysUser.setMobile(addUserDto.getMobile());
        sysUser.setUserStatus(addUserDto.getUserStatus());
        sysUser.setModifyTime(new Date());
        sysUser.setModifyBy(SecurityUtils.getUsername());
        tblAdminSysUserMapper.updateByPrimaryKeySelective(sysUser);
        //不管是否选择角色，先将其全部删除
        usersRolesService.deleteByUserRole(sysUser.getId());
        List<Long> roleIds = addUserDto.getRoleIds();
        for (int i = 0; i < roleIds.size(); i++) {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setUserId(addUserDto.getId());
            usersRoles.setRoleId(roleIds.get(i));
            usersRoles.setId(IdWorker.getId());
            usersRolesService.insertOne(usersRoles);
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult add(AddUserDto addUserDto) {
        String username = SecurityUtils.getUsername();
        Condition condition = new Condition(SysUser.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("username", addUserDto.getUsername());
        List<SysUser> sysUsers = tblAdminSysUserMapper.selectByCondition(condition);
        if (sysUsers.size() > 0) {
            return ApiResult.badParam("此账号名已被使用");
        }
        //空的为添加
        SysUser user = BeanUtil.copyProperties(addUserDto, SysUser.class);
        String salt = MD5Util.getRandomSalt(10);
        user.setId(IdWorker.getId());
        user.setSalt(salt);
        user.setPassword(MD5Util.md5Hex(addUserDto.getPassword(), salt));
        user.setCreateBy(username);
        user.setCreateTime(new Date());
        user.setModifyBy(username);
        user.setModifyTime(new Date());
        user.setFlag(new Byte("1"));
        tblAdminSysUserMapper.insertSelective(user);
        //添加完用户之后得加角色
        List<Long> roleIds = addUserDto.getRoleIds();
        for (int i = 0; i < roleIds.size(); i++) {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setUserId(user.getId());
            usersRoles.setRoleId(roleIds.get(i));
            usersRoles.setId(IdWorker.getId());
            usersRolesService.insertOne(usersRoles);
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult changePassword(ChangePasswordDto changePasswordDto) {
        //获取当前用户的用户名
        String username = SecurityUtils.getUsername();
        if (!username.equals(changePasswordDto.getUsername())) {
            return ApiResult.badParam("无修改他人信息的权限");
        }
        Condition condition = new Condition(SysUser.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("username", changePasswordDto.getUsername());
        List<SysUser> sysUsers = tblAdminSysUserMapper.selectByCondition(condition);
        if (sysUsers.size() > 0) {
            SysUser user = sysUsers.get(0);
            String password = MD5Util.md5Hex(changePasswordDto.getOldPassword(), user.getSalt());
            if (password.equals(user.getPassword())) {
                user.setPassword(MD5Util.md5Hex(changePasswordDto.getNewPassword(), user.getSalt()));
                user.setModifyTime(new Date());
                user.setModifyBy(changePasswordDto.getUsername());
                tblAdminSysUserMapper.updateByCondition(user, condition);
                jwtTokenUtil.redisTokenClear(changePasswordDto.getUsername());
                SecurityContextHolder.clearContext();
                return ApiResult.success();
            } else {
                return ApiResult.badParam("旧密码有误");
            }
        } else {
            return ApiResult.badParam("此用户不存在");
        }
    }

    @Override
    public SysUser findByName(String username) {
        Condition condition = new Condition(SysUser.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("username", username);
        List<SysUser> users = tblAdminSysUserMapper.selectByCondition(condition);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
