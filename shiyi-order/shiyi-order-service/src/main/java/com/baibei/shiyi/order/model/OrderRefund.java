package com.baibei.shiyi.order.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ord_order_refund")
public class OrderRefund {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联订单明细表主键
     */
    @Column(name = "order_item_id")
    private Long orderItemId;

    /**
     * 审核状态，pass=审核通过，reject=审核拒绝
     */
    @Column(name = "review_status")
    private String reviewStatus;

    /**
     * 审核时间
     */
    @Column(name = "review_time")
    private Date reviewTime;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款处理状态，退款处理状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     */
    @Column(name = "refund_status")
    private String refundStatus;

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
     * 获取关联订单明细表主键
     *
     * @return order_item_id - 关联订单明细表主键
     */
    public Long getOrderItemId() {
        return orderItemId;
    }

    /**
     * 设置关联订单明细表主键
     *
     * @param orderItemId 关联订单明细表主键
     */
    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * 获取审核状态，pass=审核通过，reject=审核拒绝
     *
     * @return review_status - 审核状态，pass=审核通过，reject=审核拒绝
     */
    public String getReviewStatus() {
        return reviewStatus;
    }

    /**
     * 设置审核状态，pass=审核通过，reject=审核拒绝
     *
     * @param reviewStatus 审核状态，pass=审核通过，reject=审核拒绝
     */
    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    /**
     * 获取审核时间
     *
     * @return review_time - 审核时间
     */
    public Date getReviewTime() {
        return reviewTime;
    }

    /**
     * 设置审核时间
     *
     * @param reviewTime 审核时间
     */
    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    /**
     * 获取退款金额
     *
     * @return refund_amount - 退款金额
     */
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    /**
     * 设置退款金额
     *
     * @param refundAmount 退款金额
     */
    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 获取退款原因
     *
     * @return reason - 退款原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置退款原因
     *
     * @param reason 退款原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取退款处理状态，退款处理状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     *
     * @return refund_status - 退款处理状态，退款处理状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置退款处理状态，退款处理状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     *
     * @param refundStatus 退款处理状态，退款处理状态，apply_refund=退款申请；refunded=已退款；reject_refund=退款驳回
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
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