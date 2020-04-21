package com.baibei.shiyi.trade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_transaction_transfer_log_record")
public class TransferLogRecord {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 记录表id
     */
    @Column(name = "transfer_log_id")
    private Long transferLogId;

    /**
     * 商品编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 转入客户编码
     */
    @Column(name = "in_customer_no")
    private String inCustomerNo;

    /**
     * 转出客户编码
     */
    @Column(name = "out_customer_no")
    private String outCustomerNo;

    /**
     * 成交单价
     */
    private BigDecimal price;

    /**
     * 过户数量
     */
    @Column(name = "transfer_num")
    private Integer transferNum;

    /**
     * 成本价
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 解锁时间
     */
    @Column(name = "trade_time")
    private String tradeTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否包含手续费 0-包含 1-不包含
     */
    @Column(name = "isFee")
    private String isfee;

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
     * 状态(1:正常，0:删除)
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
     * 获取记录表id
     *
     * @return transfer_log_id - 记录表id
     */
    public Long getTransferLogId() {
        return transferLogId;
    }

    /**
     * 设置记录表id
     *
     * @param transferLogId 记录表id
     */
    public void setTransferLogId(Long transferLogId) {
        this.transferLogId = transferLogId;
    }

    /**
     * 获取商品编码
     *
     * @return product_trade_no - 商品编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置商品编码
     *
     * @param productTradeNo 商品编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取转入客户编码
     *
     * @return in_customer_no - 转入客户编码
     */
    public String getInCustomerNo() {
        return inCustomerNo;
    }

    /**
     * 设置转入客户编码
     *
     * @param inCustomerNo 转入客户编码
     */
    public void setInCustomerNo(String inCustomerNo) {
        this.inCustomerNo = inCustomerNo;
    }

    /**
     * 获取转出客户编码
     *
     * @return out_customer_no - 转出客户编码
     */
    public String getOutCustomerNo() {
        return outCustomerNo;
    }

    /**
     * 设置转出客户编码
     *
     * @param outCustomerNo 转出客户编码
     */
    public void setOutCustomerNo(String outCustomerNo) {
        this.outCustomerNo = outCustomerNo;
    }

    /**
     * 获取成交单价
     *
     * @return price - 成交单价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置成交单价
     *
     * @param price 成交单价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取过户数量
     *
     * @return transfer_num - 过户数量
     */
    public Integer getTransferNum() {
        return transferNum;
    }

    /**
     * 设置过户数量
     *
     * @param transferNum 过户数量
     */
    public void setTransferNum(Integer transferNum) {
        this.transferNum = transferNum;
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

    /**
     * 获取解锁时间
     *
     * @return trade_time - 解锁时间
     */
    public String getTradeTime() {
        return tradeTime;
    }

    /**
     * 设置解锁时间
     *
     * @param tradeTime 解锁时间
     */
    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
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
     * 获取状态(1:正常，0:删除)
     *
     * @return flag - 状态(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:删除)
     *
     * @param flag 状态(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getIsfee() {
        return isfee;
    }

    public void setIsfee(String isfee) {
        this.isfee = isfee;
    }
}