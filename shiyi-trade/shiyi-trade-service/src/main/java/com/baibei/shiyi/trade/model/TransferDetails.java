package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_transaction_transfer_details")
public class TransferDetails {
    /**
     * 主键id
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
     * 流水号
     */
    @Column(name = "serial_number")
    private String serialNumber;

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
     * 商品编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 过户数量
     */
    @Column(name = "transfer_num")
    private Integer transferNum;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 买方手续费
     */
    @Column(name = "buy_fee")
    private BigDecimal buyFee;

    /**
     * 卖方手续费
     */
    @Column(name = "sell_fee")
    private BigDecimal sellFee;

    /**
     * 成本价
     */
    @Column(name = "cost_price")
    private BigDecimal costPrice;

    /**
     * 备注
     */
    private String remark;

    /**
     * 执行状态 success-成功 fail-失败
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
     * 状态(1:正常，0:删除)
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
     * 获取流水号
     *
     * @return serial_number - 流水号
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * 设置流水号
     *
     * @param serialNumber 流水号
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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
     * 获取单价
     *
     * @return price - 单价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置单价
     *
     * @param price 单价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取买方手续费
     *
     * @return buy_fee - 买方手续费
     */
    public BigDecimal getBuyFee() {
        return buyFee;
    }

    /**
     * 设置买方手续费
     *
     * @param buyFee 买方手续费
     */
    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    /**
     * 获取卖方手续费
     *
     * @return sell_fee - 卖方手续费
     */
    public BigDecimal getSellFee() {
        return sellFee;
    }

    /**
     * 设置卖方手续费
     *
     * @param sellFee 卖方手续费
     */
    public void setSellFee(BigDecimal sellFee) {
        this.sellFee = sellFee;
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
     * 获取执行状态 success-成功 fail-失败
     *
     * @return status - 执行状态 success-成功 fail-失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置执行状态 success-成功 fail-失败
     *
     * @param status 执行状态 success-成功 fail-失败
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
}