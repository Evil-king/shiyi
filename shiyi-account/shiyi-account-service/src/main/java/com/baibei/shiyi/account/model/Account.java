package com.baibei.shiyi.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_account")
public class Account {
    /**
     * 主键
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
     * 盐值
     */
    private String salt;

    /**
     * 资金密码
     */
    private String password;


    /**
     * 可用资金
     */
    private BigDecimal balance;

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * 总资金
     */
    @Column(name="total_balance")
    private BigDecimal totalBalance;

    /**
     * 挂牌买入冻结资金
     */
    @Column(name = "freezing_amount")
    private BigDecimal freezingAmount;

    /**
     * 可提现金额
     */
    @Column(name = "withdrawable_cash")
    private BigDecimal withdrawableCash;

    /**
     * 分润冻结资金（只做展示，具体操作在存管账户）
     */
    @Column(name = "freezing_bonus")
    private BigDecimal freezingBonus;

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
     * 状态（1:有效 0：无效）
     */
    private Byte flag;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
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
     * 获取资金密码
     *
     * @return password - 资金密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置资金密码
     *
     * @param password 资金密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取可用资金
     *
     * @return balance - 可用资金
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置可用资金
     *
     * @param balance 可用资金
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取挂牌买入冻结资金
     *
     * @return freezing_amount - 挂牌买入冻结资金
     */
    public BigDecimal getFreezingAmount() {
        return freezingAmount;
    }

    /**
     * 设置挂牌买入冻结资金
     *
     * @param freezingAmount 挂牌买入冻结资金
     */
    public void setFreezingAmount(BigDecimal freezingAmount) {
        this.freezingAmount = freezingAmount;
    }

    /**
     * 获取可提现金额
     *
     * @return withdrawable_cash - 可提现金额
     */
    public BigDecimal getWithdrawableCash() {
        return withdrawableCash;
    }

    /**
     * 设置可提现金额
     *
     * @param withdrawableCash 可提现金额
     */
    public void setWithdrawableCash(BigDecimal withdrawableCash) {
        this.withdrawableCash = withdrawableCash;
    }

    /**
     * 获取分润冻结资金（只做展示，具体操作在存管账户）
     *
     * @return freezing_bonus - 分润冻结资金（只做展示，具体操作在存管账户）
     */
    public BigDecimal getFreezingBonus() {
        return freezingBonus;
    }

    /**
     * 设置分润冻结资金（只做展示，具体操作在存管账户）
     *
     * @param freezingBonus 分润冻结资金（只做展示，具体操作在存管账户）
     */
    public void setFreezingBonus(BigDecimal freezingBonus) {
        this.freezingBonus = freezingBonus;
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
     * 获取状态（1:有效 0：无效）
     *
     * @return flag - 状态（1:有效 0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1:有效 0：无效）
     *
     * @param flag 状态（1:有效 0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}