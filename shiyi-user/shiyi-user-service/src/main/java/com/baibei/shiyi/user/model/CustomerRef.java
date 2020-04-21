package com.baibei.shiyi.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_ref")
public class CustomerRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 机构ID
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 直属推荐人ID
     */
    @Column(name = "recommender_id")
    private String recommenderId;

    /**
     * 操作类型（1：注册，2：用户转移）
     */
    @Column(name = "operation_type")
    private Byte operationType;

    /**
     * 操作人（后台用户ID）
     */
    private Long operator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 状态（1：正常；0：已删除）
     */
    private Byte flag;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取机构ID
     *
     * @return org_id - 机构ID
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * 设置机构ID
     *
     * @param orgId 机构ID
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取直属推荐人ID
     *
     * @return recommender_id - 直属推荐人ID
     */
    public String getRecommenderId() {
        return recommenderId;
    }

    /**
     * 设置直属推荐人ID
     *
     * @param recommenderId 直属推荐人ID
     */
    public void setRecommenderId(String recommenderId) {
        this.recommenderId = recommenderId;
    }

    /**
     * 获取操作类型（1：注册，2：用户转移）
     *
     * @return operation_type - 操作类型（1：注册，2：用户转移）
     */
    public Byte getOperationType() {
        return operationType;
    }

    /**
     * 设置操作类型（1：注册，2：用户转移）
     *
     * @param operationType 操作类型（1：注册，2：用户转移）
     */
    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    /**
     * 获取操作人（后台用户ID）
     *
     * @return operator - 操作人（后台用户ID）
     */
    public Long getOperator() {
        return operator;
    }

    /**
     * 设置操作人（后台用户ID）
     *
     * @param operator 操作人（后台用户ID）
     */
    public void setOperator(Long operator) {
        this.operator = operator;
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
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取状态（1：正常；0：已删除）
     *
     * @return flag - 状态（1：正常；0：已删除）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：正常；0：已删除）
     *
     * @param flag 状态（1：正常；0：已删除）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}