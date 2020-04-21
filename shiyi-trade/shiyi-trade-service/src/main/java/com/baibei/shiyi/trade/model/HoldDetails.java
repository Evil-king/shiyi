package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_hold_details")
public class
HoldDetails {
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
     * 期初数量
     */
    private Integer count;

    /**
     * 买入时的商品最新价
     */
    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    /**
     * 可交易日期
     */
    @Column(name = "trade_time")
    private Date tradeTime;

    /**
     * 获得持仓的来源流水号
     */
    @Column(name = "hold_no")
    private String holdNo;

    /**
     * 持有来源，deal=立即买入、entrust=委托买入、transfer=非交易过户、exchange=兑换、storage=入库
     */
    private String resource;

    /**
     * 是否已解锁(1:已解锁，0:未解锁)
     */
    @Column(name = "unlock_flag")
    private Byte unlockFlag;

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
     * 获取期初数量
     *
     * @return count - 期初数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置期初数量
     *
     * @param count 期初数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取买入时的商品最新价
     *
     * @return buy_price - 买入时的商品最新价
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     * 设置买入时的商品最新价
     *
     * @param buyPrice 买入时的商品最新价
     */
    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    /**
     * 获取可交易日期
     *
     * @return trade_time - 可交易日期
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * 设置可交易日期
     *
     * @param tradeTime 可交易日期
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * 获取获得持仓的来源流水号
     *
     * @return hold_no - 获得持仓的来源流水号
     */
    public String getHoldNo() {
        return holdNo;
    }

    /**
     * 设置获得持仓的来源流水号
     *
     * @param holdNo 获得持仓的来源流水号
     */
    public void setHoldNo(String holdNo) {
        this.holdNo = holdNo;
    }

    /**
     * 获取持有来源，deal=立即买入、entrust=委托买入、transfer=非交易过户、exchange=兑换、storage=入库
     *
     * @return resource - 持有来源，deal=立即买入、entrust=委托买入、transfer=非交易过户、exchange=兑换、storage=入库
     */
    public String getResource() {
        return resource;
    }

    /**
     * 设置持有来源，deal=立即买入、entrust=委托买入、transfer=非交易过户、exchange=兑换、storage=入库
     *
     * @param resource 持有来源，deal=立即买入、entrust=委托买入、transfer=非交易过户、exchange=兑换、storage=入库
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 获取是否已解锁(1:已解锁，0:未解锁)
     *
     * @return unlock_flag - 是否已解锁(1:已解锁，0:未解锁)
     */
    public Byte getUnlockFlag() {
        return unlockFlag;
    }

    /**
     * 设置是否已解锁(1:已解锁，0:未解锁)
     *
     * @param unlockFlag 是否已解锁(1:已解锁，0:未解锁)
     */
    public void setUnlockFlag(Byte unlockFlag) {
        this.unlockFlag = unlockFlag;
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