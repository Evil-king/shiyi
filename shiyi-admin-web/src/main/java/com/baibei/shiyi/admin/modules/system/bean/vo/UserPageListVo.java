package com.baibei.shiyi.admin.modules.system.bean.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/10/15 15:30
 * @description:
 */
@Data
public class UserPageListVo {
    /**
     * id
     */
    private Long id;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 账号
     */
    private String username;
    /**
     * 职位
     */
    private String position;
    /**
     * 真实姓名
     */
    private String realname;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 账号类型
     */
    private String orgType;
    /**
     * 账号归属（机构名称）
     */
    private String orgName;
    /**
     * 机构id
     */
    private Long organizationId;
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 角色id
     */
    private List<Long> roleIds;
    /**
     * 是否启用（enable:启用,disable禁用）
     */
    private String userStatus;
}
