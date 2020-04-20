package com.baibei.shiyi.admin.modules.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_admin_organization")
public class Organization {
    /**
     * 自增长主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 组织机构名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 父id
     */
    private Long pid;

    @Column(name = "register_mobile")
    private String registerMobile;


    public String getRegisterMobile() {
        return registerMobile;
    }

    public void setRegisterMobile(String registerMobile) {
        this.registerMobile = registerMobile;
    }

    /**
     * 排序
     */
    private Long sort;

    /**
     * 类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     */
    @Column(name = "org_type")
    private String orgType;

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
     * 组织机构编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 组织机构(enable:启用,disable禁用)
     */
    @Column(name = "org_status")
    private String orgStatus;

    /**
     * 客户编号
     */
    @Column(name = "customer_no")
    private String customerNO;

    /**
     * 获取自增长主键
     *
     * @return id - 自增长主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增长主键
     *
     * @param id 自增长主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取组织机构名称
     *
     * @return org_name - 组织机构名称
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置组织机构名称
     *
     * @param orgName 组织机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Long getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Long sort) {
        this.sort = sort;
    }

    /**
     * 获取类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     *
     * @return org_type - 类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     */
    public String getOrgType() {
        return orgType;
    }

    /**
     * 设置类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     *
     * @param orgType 类型（platform：平台；business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司）
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
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
     * 获取组织机构编码
     *
     * @return org_code - 组织机构编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置组织机构编码
     *
     * @param orgCode 组织机构编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取组织机构(enable:启用,disable禁用)
     *
     * @return org_status - 组织机构(enable:启用,disable禁用)
     */
    public String getOrgStatus() {
        return orgStatus;
    }

    /**
     * 设置组织机构(enable:启用,disable禁用)
     *
     * @param orgStatus 组织机构(enable:启用,disable禁用)
     */
    public void setOrgStatus(String orgStatus) {
        this.orgStatus = orgStatus;
    }

    public String getCustomerNO() {
        return customerNO;
    }

    public void setCustomerNO(String customerNO) {
        this.customerNO = customerNO;
    }
}