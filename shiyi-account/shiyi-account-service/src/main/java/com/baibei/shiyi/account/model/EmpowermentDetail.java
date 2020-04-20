package com.baibei.shiyi.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_account_empowerment_detail")
public class EmpowermentDetail {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 总释放量
     */
    @Column(name = "total_release")
    private BigDecimal totalRelease;

    /**
     * 每日释放量
     */
    @Column(name = "day_release")
    private BigDecimal dayRelease;

    /**
     * 剩余释放量
     */
    @Column(name = "surplus_release")
    private BigDecimal surplusRelease;

    /**
     * 状态（enable：启用disable：禁用）
     */
    private String status;

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
     * 状态（1：有效；0：无效）
     */
    private Byte flag;

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
     * 获取交易商编码
     *
     * @return customer_no - 交易商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码
     *
     * @param customerNo 交易商编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取总释放量
     *
     * @return total_release - 总释放量
     */
    public BigDecimal getTotalRelease() {
        return totalRelease;
    }

    /**
     * 设置总释放量
     *
     * @param totalRelease 总释放量
     */
    public void setTotalRelease(BigDecimal totalRelease) {
        this.totalRelease = totalRelease;
    }

    /**
     * 获取每日释放量
     *
     * @return day_release - 每日释放量
     */
    public BigDecimal getDayRelease() {
        return dayRelease;
    }

    /**
     * 设置每日释放量
     *
     * @param dayRelease 每日释放量
     */
    public void setDayRelease(BigDecimal dayRelease) {
        this.dayRelease = dayRelease;
    }

    /**
     * 获取剩余释放量
     *
     * @return surplus_release - 剩余释放量
     */
    public BigDecimal getSurplusRelease() {
        return surplusRelease;
    }

    /**
     * 设置剩余释放量
     *
     * @param surplusRelease 剩余释放量
     */
    public void setSurplusRelease(BigDecimal surplusRelease) {
        this.surplusRelease = surplusRelease;
    }

    /**
     * 获取状态（enable：启用disable：禁用）
     *
     * @return status - 状态（enable：启用disable：禁用）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（enable：启用disable：禁用）
     *
     * @param status 状态（enable：启用disable：禁用）
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取状态（1：有效；0：无效）
     *
     * @return flag - 状态（1：有效；0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：有效；0：无效）
     *
     * @param flag 状态（1：有效；0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}