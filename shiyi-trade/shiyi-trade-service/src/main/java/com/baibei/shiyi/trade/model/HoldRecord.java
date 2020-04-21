package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_hold_record")
public class HoldRecord {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流水号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     */
    private String resource;

    /**
     * 来源对应的单号
     */
    @Column(name = "resource_no")
    private String resourceNo;

    /**
     * 收支类型，in=获得持仓；out=扣减持仓
     */
    @Column(name = "re_type")
    private String reType;

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
     * 获取流水号
     *
     * @return record_no - 流水号
     */
    public String getRecordNo() {
        return recordNo;
    }

    /**
     * 设置流水号
     *
     * @param recordNo 流水号
     */
    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
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
     * 获取数量
     *
     * @return count - 数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置数量
     *
     * @param count 数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取价格
     *
     * @return price - 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置价格
     *
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     *
     * @return resource - 来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     */
    public String getResource() {
        return resource;
    }

    /**
     * 设置来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     *
     * @param resource 来源，buy=买入、sell=卖出、exchange=兑换、buy_transfer=买入过户、sell_transfer=卖出过户
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 获取来源对应的单号
     *
     * @return resource_no - 来源对应的单号
     */
    public String getResourceNo() {
        return resourceNo;
    }

    /**
     * 设置来源对应的单号
     *
     * @param resourceNo 来源对应的单号
     */
    public void setResourceNo(String resourceNo) {
        this.resourceNo = resourceNo;
    }

    /**
     * 获取收支类型，in=获得持仓；out=扣减持仓
     *
     * @return re_type - 收支类型，in=获得持仓；out=扣减持仓
     */
    public String getReType() {
        return reType;
    }

    /**
     * 设置收支类型，in=获得持仓；out=扣减持仓
     *
     * @param reType 收支类型，in=获得持仓；out=扣减持仓
     */
    public void setReType(String reType) {
        this.reType = reType;
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