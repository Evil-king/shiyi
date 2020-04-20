package com.baibei.shiyi.admin.modules.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_admin_sys_user")
public class SysUser {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户归属
     */
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 最后修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 最后修改者
     */
    @Column(name = "modify_by")
    private String modifyBy;

    /**
     * 是否删除(0:删除,1:未删除)
     */
    private Byte flag;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     */
    @Column(name = "org_type")
    private String orgType;

    /**
     * 是否启用（enable:启用,disable禁用）
     */
    @Column(name = "user_status")
    private String userStatus;

    private String position;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取用户归属
     *
     * @return organization_id - 用户归属
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     * 设置用户归属
     *
     * @param organizationId 用户归属
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 获取真实姓名
     *
     * @return realname - 真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置真实姓名
     *
     * @param realname 真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建者
     *
     * @return create_by - 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建者
     *
     * @param createBy 创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取最后修改时间
     *
     * @return modify_time - 最后修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置最后修改时间
     *
     * @param modifyTime 最后修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取最后修改者
     *
     * @return modify_by - 最后修改者
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置最后修改者
     *
     * @param modifyBy 最后修改者
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    /**
     * 获取是否删除(0:删除,1:未删除)
     *
     * @return flag - 是否删除(0:删除,1:未删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(0:删除,1:未删除)
     *
     * @param flag 是否删除(0:删除,1:未删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取盐值
     *
     * @return salt - 盐值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置盐值
     *
     * @param salt 盐值
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * 获取是否启用（enable:启用,disable禁用）
     *
     * @return user_status - 是否启用（enable:启用,disable禁用）
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * 设置是否启用（enable:启用,disable禁用）
     *
     * @param userStatus 是否启用（enable:启用,disable禁用）
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }
}