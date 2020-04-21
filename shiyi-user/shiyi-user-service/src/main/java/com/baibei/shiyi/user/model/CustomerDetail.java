package com.baibei.shiyi.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_detail")
public class CustomerDetail {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private Boolean age;

    /**
     * 姓名
     */
    private String realname;

    /**
     * 地址
     */
    private String address;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 银行卡号
     */
    @Column(name = "bank_card")
    private String bankCard;
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch_name")
    private String branchName;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * 银行卡预留手机号
     */
    @Column(name = "bank_mobile")
    private String bankMobile;

    /**
     * 区
     */
    private String area;

    /**
     * 市
     */
    private String city;

    /**
     * 省
     */
    private String province;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 头像图片地址
     */
    @Column(name = "user_picture")
    private String userPicture;

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
     * 删除状态（1：有效0：无效）
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
     * 获取性别
     *
     * @return sex - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取年龄
     *
     * @return age - 年龄
     */
    public Boolean getAge() {
        return age;
    }

    /**
     * 设置年龄
     *
     * @param age 年龄
     */
    public void setAge(Boolean age) {
        this.age = age;
    }

    /**
     * 获取姓名
     *
     * @return realname - 姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置姓名
     *
     * @param realname 姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取身份证号
     *
     * @return idcard - 身份证号
     */
    public String getIdcard() {
        return idcard;
    }

    /**
     * 设置身份证号
     *
     * @param idcard 身份证号
     */
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    /**
     * 获取头像图片地址
     *
     * @return user_picture - 头像图片地址
     */
    public String getUserPicture() {
        return userPicture;
    }

    /**
     * 设置头像图片地址
     *
     * @param userPicture 头像图片地址
     */
    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
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
     * 获取删除状态（1：有效0：无效）
     *
     * @return flag - 删除状态（1：有效0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置删除状态（1：有效0：无效）
     *
     * @param flag 删除状态（1：有效0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankMobile() {
        return bankMobile;
    }

    public void setBankMobile(String bankMobile) {
        this.bankMobile = bankMobile;
    }
}