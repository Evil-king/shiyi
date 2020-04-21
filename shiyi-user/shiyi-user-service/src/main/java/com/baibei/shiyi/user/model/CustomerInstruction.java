package com.baibei.shiyi.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_instruction")
public class CustomerInstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码（如果编码为0，则该说明书为起始说明书）
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
     * 状态（1：正常；0：已删除）
     */
    private Byte flag;

    /**
     * 说明书内容
     */
    private String text;

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
     * 获取交易商编码（如果编码为0，则该说明书为起始说明书）
     *
     * @return customer_no - 交易商编码（如果编码为0，则该说明书为起始说明书）
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码（如果编码为0，则该说明书为起始说明书）
     *
     * @param customerNo 交易商编码（如果编码为0，则该说明书为起始说明书）
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

    /**
     * 获取说明书内容
     *
     * @return text - 说明书内容
     */
    public String getText() {
        return text;
    }

    /**
     * 设置说明书内容
     *
     * @param text 说明书内容
     */
    public void setText(String text) {
        this.text = text;
    }
}