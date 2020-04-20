package com.baibei.shiyi.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pass_card_extract_order")
public class PassCardExtractOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 兑换商编号
     */
    @Column(name = "extract_customer_no")
    private String extractCustomerNo;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 操作人用户名
     */
    @Column(name = "operator_name")
    private String operatorName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核状态（wait：待审核 success：审核通过 fail:驳回）
     */
    private String status;

    /**
     * 手续费
     */
    @Column(name = "service_charge")
    private BigDecimal serviceCharge;

    /**
     * 总额
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 商品名
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

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
     * 删除状态（1：有效，0:无效）
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
     * 获取通证余额
     *
     * @return extract_customer_no - 通证余额
     */
    public String getExtractCustomerNo() {
        return extractCustomerNo;
    }

    /**
     * 设置通证余额
     *
     * @param extractCustomerNo 通证余额
     */
    public void setExtractCustomerNo(String extractCustomerNo) {
        this.extractCustomerNo = extractCustomerNo;
    }

    /**
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取操作人用户名
     *
     * @return operator_name - 操作人用户名
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * 设置操作人用户名
     *
     * @param operatorName 操作人用户名
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
     * 获取审核状态（wait：待审核 success：审核通过 fail:驳回）
     *
     * @return status - 审核状态（wait：待审核 success：审核通过 fail:驳回）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置审核状态（wait：待审核 success：审核通过 fail:驳回）
     *
     * @param status 审核状态（wait：待审核 success：审核通过 fail:驳回）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取手续费
     *
     * @return service_charge - 手续费
     */
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    /**
     * 设置手续费
     *
     * @param serviceCharge 手续费
     */
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * 获取总额
     *
     * @return total_price - 总额
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置总额
     *
     * @param totalPrice 总额
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取数量
     *
     * @return amount - 数量
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置数量
     *
     * @param amount 数量
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
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
     * 获取商品名
     *
     * @return product_name - 商品名
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名
     *
     * @param productName 商品名
     */
    public void setProductName(String productName) {
        this.productName = productName;
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
     * 获取删除状态（1：有效，0:无效）
     *
     * @return flag - 删除状态（1：有效，0:无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置删除状态（1：有效，0:无效）
     *
     * @param flag 删除状态（1：有效，0:无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}