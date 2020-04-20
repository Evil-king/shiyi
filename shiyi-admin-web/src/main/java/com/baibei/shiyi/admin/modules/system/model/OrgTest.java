package com.baibei.shiyi.admin.modules.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_org_test")
public class OrgTest {
    /**
     * 所属机构
     */
    @Id
    @Column(name = "org_code")
    private Long orgCode;

    /**
     * 机构名称
     */
    @Column(name = "org_name")
    private String orgName;

    /**
     * 机构手机号
     */
    @Column(name = "org_mobile")
    private String orgMobile;

    /**
     * 机构身份证号
     */
    @Column(name = "org_idnumber")
    private String orgIdnumber;

    /**
     * 所属事业机构编号
     */
    @Column(name = "cause_code")
    private String causeCode;

    /**
     * 所属事业部手机号
     */
    @Column(name = "cause_mobile")
    private String causeMobile;

    /**
     * 1:待处理,0:已处理, 2:处理失败
     */
    private Long flag;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "business_type")
    private String businessType;

    /**
     * 获取所属机构
     *
     * @return org_code - 所属机构
     */
    public Long getOrgCode() {
        return orgCode;
    }

    /**
     * 设置所属机构
     *
     * @param orgCode 所属机构
     */
    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取机构名称
     *
     * @return org_name - 机构名称
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * 设置机构名称
     *
     * @param orgName 机构名称
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * 获取机构手机号
     *
     * @return org_mobile - 机构手机号
     */
    public String getOrgMobile() {
        return orgMobile;
    }

    /**
     * 设置机构手机号
     *
     * @param orgMobile 机构手机号
     */
    public void setOrgMobile(String orgMobile) {
        this.orgMobile = orgMobile;
    }

    /**
     * 获取机构身份证号
     *
     * @return org_idnumber - 机构身份证号
     */
    public String getOrgIdnumber() {
        return orgIdnumber;
    }

    /**
     * 设置机构身份证号
     *
     * @param orgIdnumber 机构身份证号
     */
    public void setOrgIdnumber(String orgIdnumber) {
        this.orgIdnumber = orgIdnumber;
    }

    /**
     * 获取所属事业机构编号
     *
     * @return cause_code - 所属事业机构编号
     */
    public String getCauseCode() {
        return causeCode;
    }

    /**
     * 设置所属事业机构编号
     *
     * @param causeCode 所属事业机构编号
     */
    public void setCauseCode(String causeCode) {
        this.causeCode = causeCode;
    }

    /**
     * 获取所属事业部手机号
     *
     * @return cause_mobile - 所属事业部手机号
     */
    public String getCauseMobile() {
        return causeMobile;
    }

    /**
     * 设置所属事业部手机号
     *
     * @param causeMobile 所属事业部手机号
     */
    public void setCauseMobile(String causeMobile) {
        this.causeMobile = causeMobile;
    }

    /**
     * 获取1:待处理,0:已处理, 2:处理失败
     *
     * @return flag - 1:待处理,0:已处理, 2:处理失败
     */
    public Long getFlag() {
        return flag;
    }

    /**
     * 设置1:待处理,0:已处理, 2:处理失败
     *
     * @param flag 1:待处理,0:已处理, 2:处理失败
     */
    public void setFlag(Long flag) {
        this.flag = flag;
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
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return business_type
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * @param businessType
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}