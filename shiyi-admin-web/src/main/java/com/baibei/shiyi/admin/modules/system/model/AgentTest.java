package com.baibei.shiyi.admin.modules.system.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_agent_test")
public class AgentTest {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 机构编号
     */
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
     * 代理级别
     */
    @Column(name = "agent_level")
    private String agentLevel;

    /**
     * 1:是返还，0:没返还
     */
    @Column(name = "return_flag")
    private Long returnFlag;

    /**
     * 推荐代理
     */
    @Column(name = "recommender_agent_code")
    private String recommenderAgentCode;

    /**
     * 推荐代理手机号
     */
    @Column(name = "recommender_agent_mobile")
    private String recommenderAgentMobile;

    /**
     * 所属机构
     */
    @Column(name = "s_org_code")
    private String sOrgCode;

    /**
     * 所属机构手机号
     */
    @Column(name = "s_org_mobile")
    private String sOrgMobile;

    /**
     * 所属区代
     */
    @Column(name = "district_code")
    private String districtCode;

    /**
     * 所属区代手机号
     */
    @Column(name = "district_mobile")
    private String districtMobile;

    /**
     * 所属市代
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 所属市代手机号
     */
    @Column(name = "city_mobile")
    private String cityMobile;

    /**
     * 返还客户代码
     */
    @Column(name = "return_customer_code")
    private String returnCustomerCode;

    /**
     * 返还客户手机号
     */
    @Column(name = "return_customer_mobile")
    private String returnCustomerMobile;

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
     * 获取ID
     *
     * @return id - ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取机构编号
     *
     * @return org_code - 机构编号
     */
    public Long getOrgCode() {
        return orgCode;
    }

    /**
     * 设置机构编号
     *
     * @param orgCode 机构编号
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
     * 获取代理级别
     *
     * @return agent_level - 代理级别
     */
    public String getAgentLevel() {
        return agentLevel;
    }

    /**
     * 设置代理级别
     *
     * @param agentLevel 代理级别
     */
    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    /**
     * 获取1:是返还，0:没返还
     *
     * @return return_flag - 1:是返还，0:没返还
     */
    public Long getReturnFlag() {
        return returnFlag;
    }

    /**
     * 设置1:是返还，0:没返还
     *
     * @param returnFlag 1:是返还，0:没返还
     */
    public void setReturnFlag(Long returnFlag) {
        this.returnFlag = returnFlag;
    }

    /**
     * 获取推荐代理
     *
     * @return recommender_agent_code - 推荐代理
     */
    public String getRecommenderAgentCode() {
        return recommenderAgentCode;
    }

    /**
     * 设置推荐代理
     *
     * @param recommenderAgentCode 推荐代理
     */
    public void setRecommenderAgentCode(String recommenderAgentCode) {
        this.recommenderAgentCode = recommenderAgentCode;
    }

    /**
     * 获取推荐代理手机号
     *
     * @return recommender_agent_mobile - 推荐代理手机号
     */
    public String getRecommenderAgentMobile() {
        return recommenderAgentMobile;
    }

    /**
     * 设置推荐代理手机号
     *
     * @param recommenderAgentMobile 推荐代理手机号
     */
    public void setRecommenderAgentMobile(String recommenderAgentMobile) {
        this.recommenderAgentMobile = recommenderAgentMobile;
    }

    /**
     * 获取所属机构
     *
     * @return s_org_code - 所属机构
     */
    public String getsOrgCode() {
        return sOrgCode;
    }

    /**
     * 设置所属机构
     *
     * @param sOrgCode 所属机构
     */
    public void setsOrgCode(String sOrgCode) {
        this.sOrgCode = sOrgCode;
    }

    /**
     * 获取所属机构手机号
     *
     * @return s_org_mobile - 所属机构手机号
     */
    public String getsOrgMobile() {
        return sOrgMobile;
    }

    /**
     * 设置所属机构手机号
     *
     * @param sOrgMobile 所属机构手机号
     */
    public void setsOrgMobile(String sOrgMobile) {
        this.sOrgMobile = sOrgMobile;
    }

    /**
     * 获取所属区代
     *
     * @return district_code - 所属区代
     */
    public String getDistrictCode() {
        return districtCode;
    }

    /**
     * 设置所属区代
     *
     * @param districtCode 所属区代
     */
    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    /**
     * 获取所属区代手机号
     *
     * @return district_mobile - 所属区代手机号
     */
    public String getDistrictMobile() {
        return districtMobile;
    }

    /**
     * 设置所属区代手机号
     *
     * @param districtMobile 所属区代手机号
     */
    public void setDistrictMobile(String districtMobile) {
        this.districtMobile = districtMobile;
    }

    /**
     * 获取所属市代
     *
     * @return city_code - 所属市代
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     * 设置所属市代
     *
     * @param cityCode 所属市代
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**
     * 获取所属市代手机号
     *
     * @return city_mobile - 所属市代手机号
     */
    public String getCityMobile() {
        return cityMobile;
    }

    /**
     * 设置所属市代手机号
     *
     * @param cityMobile 所属市代手机号
     */
    public void setCityMobile(String cityMobile) {
        this.cityMobile = cityMobile;
    }

    /**
     * 获取返还客户代码
     *
     * @return return_customer_code - 返还客户代码
     */
    public String getReturnCustomerCode() {
        return returnCustomerCode;
    }

    /**
     * 设置返还客户代码
     *
     * @param returnCustomerCode 返还客户代码
     */
    public void setReturnCustomerCode(String returnCustomerCode) {
        this.returnCustomerCode = returnCustomerCode;
    }

    /**
     * 获取返还客户手机号
     *
     * @return return_customer_mobile - 返还客户手机号
     */
    public String getReturnCustomerMobile() {
        return returnCustomerMobile;
    }

    /**
     * 设置返还客户手机号
     *
     * @param returnCustomerMobile 返还客户手机号
     */
    public void setReturnCustomerMobile(String returnCustomerMobile) {
        this.returnCustomerMobile = returnCustomerMobile;
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