package com.baibei.shiyi.cash.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_fuqing_account_deposit")
public class FuqingAccountDeposit {
    /**
     * 自增长主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 银行流水号
     */
    @Column(name = "serial_no")
    private String serialNo;

    /**
     * 交易所代码
     */
    @Column(name = "exchange_id")
    private String exchangeId;

    /**
     * 交易所资金账号
     */
    @Column(name = "exchange_fund_account")
    private String exchangeFundAccount;

    /**
     * 资金密码
     */
    @Column(name = "fund_password")
    private String fundPassword;

    /**
     * 币种编码
     */
    @Column(name = "money_type")
    private String moneyType;

    /**
     * 银行业务类型(1：普通；2：冲正；3：退款)
     */
    @Column(name = "bisin_type")
    private String bisinType;

    /**
     * 银行产品代码
     */
    @Column(name = "bank_pro_code")
    private String bankProCode;

    /**
     * 银行账号名
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 银行账号
     */
    @Column(name = "bank_account")
    private String bankAccount;

    /**
     * 发生金额
     */
    @Column(name = "occur_amount")
    private BigDecimal occurAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 会员主题类型(1-机构,2-个人)
     */
    @Column(name = "member_main_type")
    private String memberMainType;

    /**
     * 会员全称
     */
    @Column(name = "full_name")
    private String fullName;

    /**
     * 证件类型
     */
    @Column(name = "id_kind")
    private String idKind;

    /**
     * 证件号码
     */
    @Column(name = "id_no")
    private String idNo;

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
     * 业务发生日期
     */
    @Column(name = "init_date")
    private String initDate;

    /**
     * 状态(成功:success,失败:fail)
     */
    private String status;

    /**
     * 福清入金参数
     */
    @Column(name = "fuqing_transfer_req")
    private String fuqingTransferReq;

    /**
     * 订单数据
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 获取自增长主键
     *
     * @return id - 自增长主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增长主键
     *
     * @param id 自增长主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取银行流水号
     *
     * @return serial_no - 银行流水号
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * 设置银行流水号
     *
     * @param serialNo 银行流水号
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * 获取交易所代码
     *
     * @return exchange_id - 交易所代码
     */
    public String getExchangeId() {
        return exchangeId;
    }

    /**
     * 设置交易所代码
     *
     * @param exchangeId 交易所代码
     */
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    /**
     * 获取交易所资金账号
     *
     * @return exchange_fund_account - 交易所资金账号
     */
    public String getExchangeFundAccount() {
        return exchangeFundAccount;
    }

    /**
     * 设置交易所资金账号
     *
     * @param exchangeFundAccount 交易所资金账号
     */
    public void setExchangeFundAccount(String exchangeFundAccount) {
        this.exchangeFundAccount = exchangeFundAccount;
    }

    /**
     * 获取资金密码
     *
     * @return fund_password - 资金密码
     */
    public String getFundPassword() {
        return fundPassword;
    }

    /**
     * 设置资金密码
     *
     * @param fundPassword 资金密码
     */
    public void setFundPassword(String fundPassword) {
        this.fundPassword = fundPassword;
    }

    /**
     * 获取币种编码
     *
     * @return money_type - 币种编码
     */
    public String getMoneyType() {
        return moneyType;
    }

    /**
     * 设置币种编码
     *
     * @param moneyType 币种编码
     */
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    /**
     * 获取银行业务类型(1：普通；2：冲正；3：退款)
     *
     * @return bisin_type - 银行业务类型(1：普通；2：冲正；3：退款)
     */
    public String getBisinType() {
        return bisinType;
    }

    /**
     * 设置银行业务类型(1：普通；2：冲正；3：退款)
     *
     * @param bisinType 银行业务类型(1：普通；2：冲正；3：退款)
     */
    public void setBisinType(String bisinType) {
        this.bisinType = bisinType;
    }

    /**
     * 获取银行产品代码
     *
     * @return bank_pro_code - 银行产品代码
     */
    public String getBankProCode() {
        return bankProCode;
    }

    /**
     * 设置银行产品代码
     *
     * @param bankProCode 银行产品代码
     */
    public void setBankProCode(String bankProCode) {
        this.bankProCode = bankProCode;
    }

    /**
     * 获取银行账号名
     *
     * @return account_name - 银行账号名
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置银行账号名
     *
     * @param accountName 银行账号名
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取银行账号
     *
     * @return bank_account - 银行账号
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 设置银行账号
     *
     * @param bankAccount 银行账号
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 获取发生金额
     *
     * @return occur_amount - 发生金额
     */
    public BigDecimal getOccurAmount() {
        return occurAmount;
    }

    /**
     * 设置发生金额
     *
     * @param occurAmount 发生金额
     */
    public void setOccurAmount(BigDecimal occurAmount) {
        this.occurAmount = occurAmount;
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
     * 获取会员主题类型(1-机构,2-个人)
     *
     * @return member_main_type - 会员主题类型(1-机构,2-个人)
     */
    public String getMemberMainType() {
        return memberMainType;
    }

    /**
     * 设置会员主题类型(1-机构,2-个人)
     *
     * @param memberMainType 会员主题类型(1-机构,2-个人)
     */
    public void setMemberMainType(String memberMainType) {
        this.memberMainType = memberMainType;
    }

    /**
     * 获取会员全称
     *
     * @return full_name - 会员全称
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 设置会员全称
     *
     * @param fullName 会员全称
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 获取证件类型
     *
     * @return id_kind - 证件类型
     */
    public String getIdKind() {
        return idKind;
    }

    /**
     * 设置证件类型
     *
     * @param idKind 证件类型
     */
    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    /**
     * 获取证件号码
     *
     * @return id_no - 证件号码
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * 设置证件号码
     *
     * @param idNo 证件号码
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
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


    public String getInitDate() {
        return initDate;
    }

    public void setInitDate(String initDate) {
        this.initDate = initDate;
    }

    /**
     * 获取状态(成功:success,失败:fail)
     *
     * @return status - 状态(成功:success,失败:fail)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态(成功:success,失败:fail)
     *
     * @param status 状态(成功:success,失败:fail)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取福清入金参数
     *
     * @return fuqing_transfer_req - 福清入金参数
     */
    public String getFuqingTransferReq() {
        return fuqingTransferReq;
    }

    /**
     * 设置福清入金参数
     *
     * @param fuqingTransferReq 福清入金参数
     */
    public void setFuqingTransferReq(String fuqingTransferReq) {
        this.fuqingTransferReq = fuqingTransferReq;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}