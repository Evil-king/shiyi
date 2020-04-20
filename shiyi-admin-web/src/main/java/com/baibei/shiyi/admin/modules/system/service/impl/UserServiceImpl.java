//package com.baibei.shiyi.admin.modules.system.service.impl;
//
//import com.baibei.shiyi.admin.modules.system.bean.dto.UserDTO;
//import com.baibei.shiyi.admin.modules.system.dao.UserMapper;
//import com.baibei.shiyi.admin.modules.system.model.User;
//import com.baibei.shiyi.admin.modules.system.model.UsersRoles;
//import com.baibei.shiyi.admin.modules.system.service.IUserService;
//import com.baibei.shiyi.admin.modules.system.service.IUsersRolesService;
//import com.baibei.shiyi.common.core.mybatis.AbstractService;
//import com.baibei.shiyi.common.tool.constants.Constants;
//import com.baibei.shiyi.common.tool.exception.ServiceException;
//import com.baibei.shiyi.common.tool.utils.BeanUtil;
//import com.baibei.shiyi.common.tool.utils.IdWorker;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
///**
// * @author: uqing
// * @date: 2019/08/06 16:05:45
// * @description: User服务实现
// */
//@Service
//@Transactional(rollbackFor = Exception.class)
//public class UserServiceImpl extends AbstractService<User> implements IUserService {
//
//    @Autowired
//    private UserMapper tblAdminUserMapper;
//
//    @Autowired
//    private IUsersRolesService usersRolesService;
//
//    @Override
//    public User findByName(String username) {
//        User user = this.findBy("username", username);
//        return user;
//    }
//
//    @Override
//    public UserDTO create(User user) {
//        User userName = this.findByName(user.getUsername());
//        if (userName != null) {
//            throw new ServiceException("用户已经存在");
//        }
//        User userEmail = this.findByEmail(user.getEmail());
//        if (userEmail != null) {
//            throw new ServiceException("email已经存在");
//        }
//        user.setId(IdWorker.getId());
//        user.setPassword("e10adc3949ba59abbe56e057f20f883e"); //默认密码为123456
//        user.setAvatar("https://i.loli.net/2019/04/04/5ca5b971e1548.jpeg");
//        user.setCreateTime(new Date());
//        this.save(user);
//        List<UsersRoles> usersRolesList = new ArrayList<>();
//        user.getRoles().stream().forEach(
//                role -> {
//                    UsersRoles usersRoles = new UsersRoles();
//                    usersRoles.setUserId(user.getId());
//                    usersRoles.setRoleId(role.getId());
//                    usersRolesList.add(usersRoles);
//                }
//        );
//        if (!usersRolesList.isEmpty()) {
//            this.usersRolesService.save(usersRolesList);
//        }
//        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        if (user.getEnabled().equals(Constants.OrganizationStatus.ENABLE)) {
//            userDTO.setUserStatus(Constants.OrganizationStatus.ENABLE);
//        } else {
//            userDTO.setUserStatus(Constants.OrganizationStatus.DISABLE);
//        }
//        return userDTO;
//    }
//
//    @Override
//    public User findByEmail(String email) {
//        return findBy("email", email);
//    }
//
//    @Override
//    public void updateUser(User user) {
//        this.update(user);
//        this.usersRolesService.deleteByUserRole(user.getId());
//        List<UsersRoles> usersRolesList = new ArrayList<>();
//        user.getRoles().stream().forEach(role -> {
//            UsersRoles usersRoles = new UsersRoles();
//            usersRoles.setUserId(user.getId());
//            usersRoles.setRoleId(role.getId());
//            usersRolesList.add(usersRoles);
//        });
//        if (!usersRolesList.isEmpty()) {
//            this.usersRolesService.save(usersRolesList);
//        }
//    }
//}
