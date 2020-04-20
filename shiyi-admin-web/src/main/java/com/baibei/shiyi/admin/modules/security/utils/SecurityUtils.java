package com.baibei.shiyi.admin.modules.security.utils;

import com.baibei.shiyi.admin.modules.security.JwtUser;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 获取当前登录的用户
 *
 * @author jie
 * @date 2019-01-17
 */
public class SecurityUtils {

    public static UserDetails getUserDetails() {
        UserDetails userDetails = null;
        try {
            userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("登录状态过期");
        }
        return userDetails;
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        JwtUser jwtUser = (JwtUser) getUserDetails();
        return jwtUser.getUsername();
    }

    /**
     * 获取系统用户id
     *
     * @return 系统用户id
     */
    public static Long getUserId() {
        JwtUser jwtUser = (JwtUser) getUserDetails();
        return jwtUser.getId();
    }
}
