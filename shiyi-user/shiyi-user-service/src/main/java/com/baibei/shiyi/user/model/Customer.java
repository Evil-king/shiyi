package com.baibei.shiyi.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "customer_no")
    private String customerNo;
    /**
     * 所属代理编码（三层代理才有）
     */
    @Column(name = "org_code")
    private Long orgCode;
    /**
     * 所属市级代理编码
     */
    @Column(name = "city_agent_code")
    private Long cityAgentCode;
    @Column(name="mem_code_clear")
    private String memCodeClear;

    @Column(name = "bank_client_no")
    private String bankClientNo;
    @Column(name = "fund_account_clear")
    private String fundAccountClear;
    @Column(name = "register_source")
    private String registerSource;

    public String getRegisterSource() {
        return registerSource;
    }

    public void setRegisterSource(String registerSource) {
        this.registerSource = registerSource;
    }
    @Column(name = "is_legal_person")
    private String isLegalPerson;

    public String getIsLegalPerson() {
        return isLegalPerson;
    }

    public void setIsLegalPerson(String isLegalPerson) {
        this.isLegalPerson = isLegalPerson;
    }

    /**
     * 密码
     */
    private String password;
    /**
     * 是否业务系统签约(0未签约 1已签约)
     */
    private String signing;
    /**
     * 是否第一次购销
     */
    private String isSign;

    /**
     * 实名认证（1=已实名认证；0=未实名认证）
     */
    @Column(name = "realname_verification")
    private String realnameVerification;

    /**
     * 开户时间
     */
    @Column(name = "signing_time")
    private Date signingTime;

    public Date getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(Date signingTime) {
        this.signingTime = signingTime;
    }

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 手机归属地
     */
    @Column(name = "mobile_location")
    private String mobileLocation;

    /**
     * 直接推荐人ID
     */
    @Column(name = "recommender_id")
    private String recommenderId;

    /**
     * 直属归属
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 1,：普通用户 2：代理商
     */
    @Column(name = "customer_type")
    private Byte customerType;

    /**
     * 100:正常101：限制商城登录102：限制交易登录等等
     */
    @Column(name = "customer_status")
    private String customerStatus;

    /**
     * 加密时的盐值
     */
    private String salt;

    /**
     * 二维码链接地址
     */
    private String qrcode;

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
     * 状态（1：正常；0：已删除）
     */
    private Byte flag;

    public Long getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(Long orgCode) {
        this.orgCode = orgCode;
    }


    public String getMemCodeClear() {
        return memCodeClear;
    }

    public void setMemCodeClear(String memCodeClear) {
        this.memCodeClear = memCodeClear;
    }



    public Long getCityAgentCode() {
        return cityAgentCode;
    }

    public void setCityAgentCode(Long cityAgentCode) {
        this.cityAgentCode = cityAgentCode;
    }

    public String getBankClientNo() {
        return bankClientNo;
    }

    public void setBankClientNo(String bankClientNo) {
        this.bankClientNo = bankClientNo;
    }

    public String getFundAccountClear() {
        return fundAccountClear;
    }

    public void setFundAccountClear(String fundAccountClear) {
        this.fundAccountClear = fundAccountClear;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String getSigning() {
        return signing;
    }

    public void setSigning(String signing) {
        this.signing = signing;
    }

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
     * 获取用户名
     *
     * @return customer_no - 用户名
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户名
     *
     * @param customerNo 用户名
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取手机归属地
     *
     * @return mobile_location - 手机归属地
     */
    public String getMobileLocation() {
        return mobileLocation;
    }

    /**
     * 设置手机归属地
     *
     * @param mobileLocation 手机归属地
     */
    public void setMobileLocation(String mobileLocation) {
        this.mobileLocation = mobileLocation;
    }

    /**
     * 获取直接推荐人ID
     *
     * @return recommender_id - 直接推荐人ID
     */
    public String getRecommenderId() {
        return recommenderId;
    }

    /**
     * 设置直接推荐人ID
     *
     * @param recommenderId 直接推荐人ID
     */
    public void setRecommenderId(String recommenderId) {
        this.recommenderId = recommenderId;
    }

    /**
     * 获取直属归属
     *
     * @return org_id - 直属归属
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * 设置直属归属
     *
     * @param orgId 直属归属
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取1,：普通用户 2：代理商
     *
     * @return customer_type - 1,：普通用户 2：代理商
     */
    public Byte getCustomerType() {
        return customerType;
    }

    /**
     * 设置1,：普通用户 2：代理商
     *
     * @param customerType 1,：普通用户 2：代理商
     */
    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    /**
     * 获取100:正常101：限制商城登录102：限制交易登录等等
     *
     * @return login_status - 100:正常101：限制商城登录102：限制交易登录等等
     */
    public String getCustomerStatus() {
        return customerStatus;
    }

    /**
     * 设置100:正常101：限制商城登录102：限制交易登录等等
     *
     * @param customerStatus 100:正常101：限制商城登录102：限制交易登录等等
     */
    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * 获取加密时的盐值
     *
     * @return salt - 加密时的盐值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置加密时的盐值
     *
     * @param salt 加密时的盐值
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取二维码链接地址
     *
     * @return qrcode - 二维码链接地址
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置二维码链接地址
     *
     * @param qrcode 二维码链接地址
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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
     * 获取状态（1：正常；0：已删除）
     *
     * @return flag - 状态（1：正常；0：已删除）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：正常；0：已删除）
     *
     * @param flag 状态（1：正常；0：已删除）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getRealnameVerification() {
        return realnameVerification;
    }

    public void setRealnameVerification(String realnameVerification) {
        this.realnameVerification = realnameVerification;
    }
}