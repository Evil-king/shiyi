package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_stock_record")
public class StockRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流水号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 关联订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 关联库存id
     */
    @Column(name = "stock_id")
    private Long stockId;

    /**
     * 关联商品id
     */
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 执行者编码
     */
    @Column(name = "operator_no")
    private String operatorNo;

    /**
     * 类型（trade=交易；sys=系统）
     */
    @Column(name = "change_type")
    private String changeType;

    /**
     * 扣减增加（out=扣减；in=增加）
     */
    private String retype;

    /**
     * 源库存数量
     */
    @Column(name = "before_count")
    private BigDecimal beforeCount;

    /**
     * 更改数量
     */
    @Column(name = "change_count")
    private BigDecimal changeCount;

    /**
     * 剩余库存
     */
    @Column(name = "after_count")
    private BigDecimal afterCount;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

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
     * 获取关联订单号
     *
     * @return order_no - 关联订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置关联订单号
     *
     * @param orderNo 关联订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取关联库存id
     *
     * @return stock_id - 关联库存id
     */
    public Long getStockId() {
        return stockId;
    }

    /**
     * 设置关联库存id
     *
     * @param stockId 关联库存id
     */
    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    /**
     * 获取关联商品id
     *
     * @return product_id - 关联商品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置关联商品id
     *
     * @param productId 关联商品id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return sku_id
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * @param skuId
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * 获取执行者编码
     *
     * @return operator_no - 执行者编码
     */
    public String getOperatorNo() {
        return operatorNo;
    }

    /**
     * 设置执行者编码
     *
     * @param operatorNo 执行者编码
     */
    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    /**
     * 获取类型（trade=交易；sys=系统）
     *
     * @return change_type - 类型（trade=交易；sys=系统）
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * 设置类型（trade=交易；sys=系统）
     *
     * @param changeType 类型（trade=交易；sys=系统）
     */
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    /**
     * 获取扣减增加（detuch=扣减；add=增加）
     *
     * @return retype - 扣减增加（detuch=扣减；add=增加）
     */
    public String getRetype() {
        return retype;
    }

    /**
     * 设置扣减增加（detuch=扣减；add=增加）
     *
     * @param retype 扣减增加（detuch=扣减；add=增加）
     */
    public void setRetype(String retype) {
        this.retype = retype;
    }

    /**
     * 获取源库存数量
     *
     * @return before_count - 源库存数量
     */
    public BigDecimal getBeforeCount() {
        return beforeCount;
    }

    /**
     * 设置源库存数量
     *
     * @param beforeCount 源库存数量
     */
    public void setBeforeCount(BigDecimal beforeCount) {
        this.beforeCount = beforeCount;
    }

    /**
     * 获取更改数量
     *
     * @return change_count - 更改数量
     */
    public BigDecimal getChangeCount() {
        return changeCount;
    }

    /**
     * 设置更改数量
     *
     * @param changeCount 更改数量
     */
    public void setChangeCount(BigDecimal changeCount) {
        this.changeCount = changeCount;
    }

    /**
     * 获取剩余库存
     *
     * @return after_count - 剩余库存
     */
    public BigDecimal getAfterCount() {
        return afterCount;
    }

    /**
     * 设置剩余库存
     *
     * @param afterCount 剩余库存
     */
    public void setAfterCount(BigDecimal afterCount) {
        this.afterCount = afterCount;
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
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return flag
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * @param flag
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}