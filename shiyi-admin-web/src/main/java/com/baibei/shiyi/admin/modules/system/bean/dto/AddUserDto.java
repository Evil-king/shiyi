package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/10/16 11:02
 * @description:
 */
@Data
public class AddUserDto {
    private Long id;
    /**
     * 账号
     */
    private String username;
    /**
     * 职位
     */
    private String position;
    /**
     * 姓名
     */
    private String realname;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态 '是否启用（enable:启用,disable禁用）'
     */
    private String userStatus;
    /**
     * 账号类型
     */
    private String orgType;
    /**
     * 账号所属（机构id）
     */
    private Long organizationId;
    /**
     * 角色id集合
     */
    private List<Long> roleIds;
    /**
     * 密码
     */
    private String password;
    /**
     * 重复密码
     */
    private String repeatPassword;
}
