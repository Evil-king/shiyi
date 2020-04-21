package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_hold_position")
public class HoldPosition {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 交易商姓名，冗余
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 持仓总数
     */
    @Column(name = "remaind_count")
    private Integer remaindCount;

    /**
     * 持仓总冻结数
     */
    @Column(name = "frozen_count")
    private Integer frozenCount;

    /**
     * 持仓可卖总数
     */
    @Column(name = "can_sell_count")
    private Integer canSellCount;

    /**
     * 总锁仓数
     */
    @Column(name = "lock_count")
    private Integer lockCount;

    /**
     * 成本价
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String remark;

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
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户编号
     *
     * @return customer_no - 用户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户编号
     *
     * @param customerNo 用户编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取交易商姓名，冗余
     *
     * @return customer_name - 交易商姓名，冗余
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置交易商姓名，冗余
     *
     * @param customerName 交易商姓名，冗余
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取商品交易编码
     *
     * @return product_trade_no - 商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置商品交易编码
     *
     * @param productTradeNo 商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取持仓总数
     *
     * @return remaind_count - 持仓总数
     */
    public Integer getRemaindCount() {
        return remaindCount;
    }

    /**
     * 设置持仓总数
     *
     * @param remaindCount 持仓总数
     */
    public void setRemaindCount(Integer remaindCount) {
        this.remaindCount = remaindCount;
    }

    /**
     * 获取持仓总冻结数
     *
     * @return frozen_count - 持仓总冻结数
     */
    public Integer getFrozenCount() {
        return frozenCount;
    }

    /**
     * 设置持仓总冻结数
     *
     * @param frozenCount 持仓总冻结数
     */
    public void setFrozenCount(Integer frozenCount) {
        this.frozenCount = frozenCount;
    }

    /**
     * 获取持仓可卖总数
     *
     * @return can_sell_count - 持仓可卖总数
     */
    public Integer getCanSellCount() {
        return canSellCount;
    }

    /**
     * 设置持仓可卖总数
     *
     * @param canSellCount 持仓可卖总数
     */
    public void setCanSellCount(Integer canSellCount) {
        this.canSellCount = canSellCount;
    }

    /**
     * 获取总锁仓数
     *
     * @return lock_count - 总锁仓数
     */
    public Integer getLockCount() {
        return lockCount;
    }

    /**
     * 设置总锁仓数
     *
     * @param lockCount 总锁仓数
     */
    public void setLockCount(Integer lockCount) {
        this.lockCount = lockCount;
    }

    /**
     * 获取成本价
     *
     * @return cost_price - 成本价
     */
    public BigDecimal getCostPrice() {
        return costPrice;
    }

    /**
     * 设置成本价
     *
     * @param costPrice 成本价
     */
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }


}