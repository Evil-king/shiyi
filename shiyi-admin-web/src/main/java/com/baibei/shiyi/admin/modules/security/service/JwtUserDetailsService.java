package com.baibei.shiyi.admin.modules.security.service;


import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.admin.modules.security.JwtUser;
import com.baibei.shiyi.admin.modules.system.bean.dto.DeptDTO;
import com.baibei.shiyi.admin.modules.system.bean.dto.JobDTO;
import com.baibei.shiyi.admin.modules.system.bean.dto.UserDTO;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.admin.modules.system.model.User;
import com.baibei.shiyi.admin.modules.system.service.ISysUserService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author jie
 * @date 2018-11-22
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private JwtPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = userService.findByName(username);
        if (user == null) {
            throw new ServiceException("账号不存在");
        } else {
            if (user.getUserStatus().equals(Constants.OrganizationStatus.DISABLE)) {
                throw new ServiceException("账号已被禁用");
            }
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            return createJwtUser(userDTO);
        }
    }

    public UserDetails createJwtUser(UserDTO user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getOrganizationId(),
                user.getRealname(),
                user.getMobile(),
                user.getOrgType(),
                user.getPosition(),
                user.getSalt(),
                permissionService.mapToGrantedAuthorities(user),
                user.getUserStatus(),
                user.getCreateTime()
        );
    }
}
