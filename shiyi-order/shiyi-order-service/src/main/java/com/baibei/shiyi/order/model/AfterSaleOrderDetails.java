package com.baibei.shiyi.order.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_aftersale_order_details")
public class AfterSaleOrderDetails {
    /**
     * 主键server_no
     */
    @Id
    @Column(name = "server_no")
    private String serverNo;

    /**
     * 子订单编号
     */
    @Column(name = "order_item_no")
    private String orderItemNo;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 联系电话
     */
    @Column(name = "receiverPhone")
    private String receiverphone;

    /**
     * 联系人
     */
    @Column(name = "receiverName")
    private String receivername;

    /**
     * 退货原因
     */
    @Column(name = "returnReasons")
    private String returnreasons;

    /**
     * 问题描述
     */
    @Column(name = "problemDescription")
    private String problemdescription;

    /**
     * 备注
     */
    private String remark;

    /**
     * 寄回地址
     */
    @Column(name = "sendback_address")
    private String sendbackAddress;

    /**
     * 退款金额
     */
    @Column(name = "refuse_money")
    private BigDecimal refuseMoney;

    /**
     * 退款账户
     */
    @Column(name = "refuse_account")
    private String refuseAccount;

    /**
     * 退回积分
     */
    @Column(name = "refuse_integral")
    private BigDecimal refuseIntegral;

    /**
     * 物流公司名
     */
    @Column(name = "logistics_name")
    private String logisticsName;

    /**
     * 物流单号
     */
    @Column(name = "logistics_no")
    private String logisticsNo;

    /**
     * 寄回物流公司名
     */
    @Column(name = "send_logistics_name")
    private String sendLogisticsName;

    /**
     * 补发地址
     */
    @Column(name = "reissue_address")
    private String reissueAddress;

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
     * 凭证照片
     */
    private String photo;

    /**
     * 寄回物流单号
     */
    @Column(name = "send_logistics_no")
    private String sendLogisticsNo;

    /**
     * 获取主键server_no
     *
     * @return server_no - 主键server_no
     */
    public String getServerNo() {
        return serverNo;
    }

    /**
     * 设置主键server_no
     *
     * @param serverNo 主键server_no
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
     * 获取联系电话
     *
     * @return receiverPhone - 联系电话
     */
    public String getReceiverphone() {
        return receiverphone;
    }

    /**
     * 设置联系电话
     *
     * @param receiverphone 联系电话
     */
    public void setReceiverphone(String receiverphone) {
        this.receiverphone = receiverphone;
    }

    /**
     * 获取联系人
     *
     * @return receiverName - 联系人
     */
    public String getReceivername() {
        return receivername;
    }

    /**
     * 设置联系人
     *
     * @param receivername 联系人
     */
    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    /**
     * 获取退货原因
     *
     * @return returnReasons - 退货原因
     */
    public String getReturnreasons() {
        return returnreasons;
    }

    /**
     * 设置退货原因
     *
     * @param returnreasons 退货原因
     */
    public void setReturnreasons(String returnreasons) {
        this.returnreasons = returnreasons;
    }

    /**
     * 获取问题描述
     *
     * @return problemDescription - 问题描述
     */
    public String getProblemdescription() {
        return problemdescription;
    }

    /**
     * 设置问题描述
     *
     * @param problemdescription 问题描述
     */
    public void setProblemdescription(String problemdescription) {
        this.problemdescription = problemdescription;
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
     * 获取寄回地址
     *
     * @return sendback_address - 寄回地址
     */
    public String getSendbackAddress() {
        return sendbackAddress;
    }

    /**
     * 设置寄回地址
     *
     * @param sendbackAddress 寄回地址
     */
    public void setSendbackAddress(String sendbackAddress) {
        this.sendbackAddress = sendbackAddress;
    }

    /**
     * 获取退款金额
     *
     * @return refuse_money - 退款金额
     */
    public BigDecimal getRefuseMoney() {
        return refuseMoney;
    }

    /**
     * 设置退款金额
     *
     * @param refuseMoney 退款金额
     */
    public void setRefuseMoney(BigDecimal refuseMoney) {
        this.refuseMoney = refuseMoney;
    }

    /**
     * 获取退款账户
     *
     * @return refuse_account - 退款账户
     */
    public String getRefuseAccount() {
        return refuseAccount;
    }

    /**
     * 设置退款账户
     *
     * @param refuseAccount 退款账户
     */
    public void setRefuseAccount(String refuseAccount) {
        this.refuseAccount = refuseAccount;
    }

    /**
     * 获取退回积分
     *
     * @return refuse_integral - 退回积分
     */
    public BigDecimal getRefuseIntegral() {
        return refuseIntegral;
    }

    /**
     * 设置退回积分
     *
     * @param refuseIntegral 退回积分
     */
    public void setRefuseIntegral(BigDecimal refuseIntegral) {
        this.refuseIntegral = refuseIntegral;
    }

    /**
     * 获取物流公司名
     *
     * @return logistics_name - 物流公司名
     */
    public String getLogisticsName() {
        return logisticsName;
    }

    /**
     * 设置物流公司名
     *
     * @param logisticsName 物流公司名
     */
    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    /**
     * 获取物流单号
     *
     * @return logistics_no - 物流单号
     */
    public String getLogisticsNo() {
        return logisticsNo;
    }

    /**
     * 设置物流单号
     *
     * @param logisticsNo 物流单号
     */
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    /**
     * 获取寄回物流公司名
     *
     * @return send_logistics_name - 寄回物流公司名
     */
    public String getSendLogisticsName() {
        return sendLogisticsName;
    }

    /**
     * 设置寄回物流公司名
     *
     * @param sendLogisticsName 寄回物流公司名
     */
    public void setSendLogisticsName(String sendLogisticsName) {
        this.sendLogisticsName = sendLogisticsName;
    }

    /**
     * 获取补发地址
     *
     * @return reissue_address - 补发地址
     */
    public String getReissueAddress() {
        return reissueAddress;
    }

    /**
     * 设置补发地址
     *
     * @param reissueAddress 补发地址
     */
    public void setReissueAddress(String reissueAddress) {
        this.reissueAddress = reissueAddress;
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

    /**
     * 获取凭证照片
     *
     * @return photo - 凭证照片
     */
    public String getPhoto() {
        return photo;
    }

    /**
     * 设置凭证照片
     *
     * @param photo 凭证照片
     */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /**
     * 获取寄回物流单号
     *
     * @return send_logistics_no - 寄回物流单号
     */
    public String getSendLogisticsNo() {
        return sendLogisticsNo;
    }

    /**
     * 设置寄回物流单号
     *
     * @param sendLogisticsNo 寄回物流单号
     */
    public void setSendLogisticsNo(String sendLogisticsNo) {
        this.sendLogisticsNo = sendLogisticsNo;
    }
}