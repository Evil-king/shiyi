package com.baibei.shiyi.admin.modules.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_admin_branch_office_proxy")
public class BranchOfficeProxy {
    /**
     * 自增长主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分公司主键
     */
    @Column(name = "branch_company_id")
    private Long branchCompanyId;

    /**
     * 机构id
     */
    @Column(name = "organization_id")
    private Long organizationId;

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
     * 获取分公司主键
     *
     * @return branch_company_id - 分公司主键
     */
    public Long getBranchCompanyId() {
        return branchCompanyId;
    }

    /**
     * 设置分公司主键
     *
     * @param branchCompanyId 分公司主键
     */
    public void setBranchCompanyId(Long branchCompanyId) {
        this.branchCompanyId = branchCompanyId;
    }

    /**
     * 获取机构id
     *
     * @return organization_id - 机构id
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     * 设置机构id
     *
     * @param organizationId 机构id
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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
}