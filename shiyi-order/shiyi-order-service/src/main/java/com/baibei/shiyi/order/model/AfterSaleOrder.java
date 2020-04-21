package com.baibei.shiyi.order.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_aftersale_order")
public class AfterSaleOrder {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 主订单编号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 服务单号
     */
    @Column(name = "server_no")
    private String serverNo;

    /**
     * 子订单编号
     */
    @Column(name = "order_item_no")
    private String orderItemNo;

    /**
     * 价格
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 售后类型,refund-退款,exchange-换货
     */
    private String type;

    /**
     * 售后状态,init-初始化,waiting-待审核,revoked--已撤销,doing-处理中(售后收货),fail-驳回,success-审核通过,refunded-已退款,reissued-已补发
     */
    private String status;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 退款/补发时间
     */
    @Column(name = "finish_time")
    private Date finishTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 申请时间
     */
    @Column(name = "application_time")
    private Date applicationTime;

    /**
     * 审核通过时间
     */
    @Column(name = "success_time")
    private Date successTime;

    /**
     * 售后收货时间
     */
    @Column(name = "doing_time")
    private Date doingTime;

    /**
     * 撤销时间
     */
    @Column(name = "revoked_time")
    private Date revokedTime;

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
     * 获取主订单编号
     *
     * @return order_no - 主订单编号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置主订单编号
     *
     * @param orderNo 主订单编号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取服务单号
     *
     * @return server_no - 服务单号
     */
    public String getServerNo() {
        return serverNo;
    }

    /**
     * 设置服务单号
     *
     * @param serverNo 服务单号
     */
    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

    /**
     * 获取子订单编号
     *
     * @return order_item_no - 子订单编号
     */
    public String getOrderItemNo() {
        return orderItemNo;
    }

    /**
     * 设置子订单编号
     *
     * @param orderItemNo 子订单编号
     */
    public void setOrderItemNo(String orderItemNo) {
        this.orderItemNo = orderItemNo;
    }

    /**
     * 获取价格
     *
     * @return total_amount - 价格
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置价格
     *
     * @param totalAmount 价格
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获取售后类型,refund-退款,exchange-换货
     *
     * @return type - 售后类型,refund-退款,exchange-换货
     */
    public String getType() {
        return type;
    }

    /**
     * 设置售后类型,refund-退款,exchange-换货
     *
     * @param type 售后类型,refund-退款,exchange-换货
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取售后状态,init-初始化,waiting-待审核,revoked--已撤销,doing-处理中(售后收货),fail-驳回,success-审核通过,refunded-已退款,reissued-已补发
     *
     * @return status - 售后状态,init-初始化,waiting-待审核,revoked--已撤销,doing-处理中(售后收货),fail-驳回,success-审核通过,refunded-已退款,reissued-已补发
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置售后状态,init-初始化,waiting-待审核,revoked--已撤销,doing-处理中(售后收货),fail-驳回,success-审核通过,refunded-已退款,reissued-已补发
     *
     * @param status 售后状态,init-初始化,waiting-待审核,revoked--已撤销,doing-处理中(售后收货),fail-驳回,success-审核通过,refunded-已退款,reissued-已补发
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取退款/补发时间
     *
     * @return finish_time - 退款/补发时间
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * 设置退款/补发时间
     *
     * @param finishTime 退款/补发时间
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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
     * 获取申请时间
     *
     * @return application_time - 申请时间
     */
    public Date getApplicationTime() {
        return applicationTime;
    }

    /**
     * 设置申请时间
     *
     * @param applicationTime 申请时间
     */
    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    /**
     * 获取审核通过时间
     *
     * @return success_time - 审核通过时间
     */
    public Date getSuccessTime() {
        return successTime;
    }

    /**
     * 设置审核通过时间
     *
     * @param successTime 审核通过时间
     */
    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    /**
     * 获取售后收货时间
     *
     * @return doing_time - 售后收货时间
     */
    public Date getDoingTime() {
        return doingTime;
    }

    /**
     * 设置售后收货时间
     *
     * @param doingTime 售后收货时间
     */
    public void setDoingTime(Date doingTime) {
        this.doingTime = doingTime;
    }

    /**
     * 获取撤销时间
     *
     * @return revoked_time - 撤销时间
     */
    public Date getRevokedTime() {
        return revokedTime;
    }

    /**
     * 设置撤销时间
     *
     * @param revokedTime 撤销时间
     */
    public void setRevokedTime(Date revokedTime) {
        this.revokedTime = revokedTime;
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