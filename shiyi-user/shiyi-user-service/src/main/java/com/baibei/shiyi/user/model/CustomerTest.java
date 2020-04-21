package com.baibei.shiyi.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_test")
public class CustomerTest {
    /**
     * 客户代码
     */
    @Id
    @Column(name = "customer_code")
    private String customerCode;

    /**
     * 客户名称
     */
    @Column(name = "customer_username")
    private String customerUsername;

    /**
     * 联系电话
     */
    @Column(name = "customer_mobile")
    private String customerMobile;

    /**
     * 推荐客户编号
     */
    @Column(name = "recommender_code")
    private String recommenderCode;

    /**
     * 推荐客户名称
     */
    @Column(name = "recommender_username")
    private String recommenderUsername;

    /**
     * 推荐客户手机号
     */
    @Column(name = "recommender_mobile")
    private String recommenderMobile;

    /**
     * 直属上级
     */
    @Column(name = "directly_code")
    private String directlyCode;

    /**
     * 直属手机号
     */
    @Column(name = "directly_mobile")
    private String directlyMobile;

    /**
     * 所属机构
     */
    @Column(name = "org_code")
    private Long orgCode;

    /**
     * 机构手机号
     */
    @Column(name = "org_mobile")
    private String orgMobile;

    /**
     * 1,：普通用户 2：代理商
     */
    @Column(name = "customer_type")
    private Byte customerType;

    /**
     * 代理编号
     */
    @Column(name = "agent_code")
    private String agentCode;

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

    /**
     * 获取客户代码
     *
     * @return customer_code - 客户代码
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * 设置客户代码
     *
     * @param customerCode 客户代码
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * 获取客户名称
     *
     * @return customer_username - 客户名称
     */
    public String getCustomerUsername() {
        return customerUsername;
    }

    /**
     * 设置客户名称
     *
     * @param customerUsername 客户名称
     */
    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    /**
     * 获取联系电话
     *
     * @return customer_mobile - 联系电话
     */
    public String getCustomerMobile() {
        return customerMobile;
    }

    /**
     * 设置联系电话
     *
     * @param customerMobile 联系电话
     */
    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    /**
     * 获取推荐客户编号
     *
     * @return recommender_code - 推荐客户编号
     */
    public String getRecommenderCode() {
        return recommenderCode;
    }

    /**
     * 设置推荐客户编号
     *
     * @param recommenderCode 推荐客户编号
     */
    public void setRecommenderCode(String recommenderCode) {
        this.recommenderCode = recommenderCode;
    }

    /**
     * 获取推荐客户名称
     *
     * @return recommender_username - 推荐客户名称
     */
    public String getRecommenderUsername() {
        return recommenderUsername;
    }

    /**
     * 设置推荐客户名称
     *
     * @param recommenderUsername 推荐客户名称
     */
    public void setRecommenderUsername(String recommenderUsername) {
        this.recommenderUsername = recommenderUsername;
    }

    /**
     * 获取推荐客户手机号
     *
     * @return recommender_mobile - 推荐客户手机号
     */
    public String getRecommenderMobile() {
        return recommenderMobile;
    }

    /**
     * 设置推荐客户手机号
     *
     * @param recommenderMobile 推荐客户手机号
     */
    public void setRecommenderMobile(String recommenderMobile) {
        this.recommenderMobile = recommenderMobile;
    }

    /**
     * 获取直属上级
     *
     * @return directly_code - 直属上级
     */
    public String getDirectlyCode() {
        return directlyCode;
    }

    /**
     * 设置直属上级
     *
     * @param directlyCode 直属上级
     */
    public void setDirectlyCode(String directlyCode) {
        this.directlyCode = directlyCode;
    }

    /**
     * 获取直属手机号
     *
     * @return directly_mobile - 直属手机号
     */
    public String getDirectlyMobile() {
        return directlyMobile;
    }

    /**
     * 设置直属手机号
     *
     * @param directlyMobile 直属手机号
     */
    public void setDirectlyMobile(String directlyMobile) {
        this.directlyMobile = directlyMobile;
    }

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
     * 获取1,：普通用户 2：代理商
     *
     * @return customer_type - 1,：普通用户 2：代理商
     */
    public Byte getCustomerType() {
        return customerType;
    }

    /**
     * 设置1,：普通用户 2：代理商
     *
     * @param customerType 1,：普通用户 2：代理商
     */
    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    /**
     * 获取代理编号
     *
     * @return agent_code - 代理编号
     */
    public String getAgentCode() {
        return agentCode;
    }

    /**
     * 设置代理编号
     *
     * @param agentCode 代理编号
     */
    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
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
}