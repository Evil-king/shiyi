package com.baibei.shiyi.trade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_transaction_transfer_log")
public class TransferLog {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 执行人
     */
    private String modifier;

    /**
     * 合计数量
     */
    @Column(name = "count_num")
    private Integer countNum;

    /**
     * 合计金额
     */
    @Column(name = "count_amount")
    private BigDecimal countAmount;

    /**
     * 状态 execute-已执行,wait-待执行
     */
    private String status;

    /**
     * 是否包含手续费 0-包含 1-不包含
     */
    @Column(name = "isFee")
    private String isfee;

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
     * 获取文件名称
     *
     * @return name - 文件名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文件名称
     *
     * @param name 文件名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取执行人
     *
     * @return modifier - 执行人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置执行人
     *
     * @param modifier 执行人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    /**
     * 获取合计数量
     *
     * @return count_num - 合计数量
     */
    public Integer getCountNum() {
        return countNum;
    }

    /**
     * 设置合计数量
     *
     * @param countNum 合计数量
     */
    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    /**
     * 获取合计金额
     *
     * @return count_amount - 合计金额
     */
    public BigDecimal getCountAmount() {
        return countAmount;
    }

    /**
     * 设置合计金额
     *
     * @param countAmount 合计金额
     */
    public void setCountAmount(BigDecimal countAmount) {
        this.countAmount = countAmount;
    }

    /**
     * 获取状态 execute-已执行,wait-待执行
     *
     * @return status - 状态 execute-已执行,wait-待执行
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态 execute-已执行,wait-待执行
     *
     * @param status 状态 execute-已执行,wait-待执行
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取是否包含手续费 0-包含 1-不包含
     *
     * @return isFee - 是否包含手续费 0-包含 1-不包含
     */
    public String getIsfee() {
        return isfee;
    }

    /**
     * 设置是否包含手续费 0-包含 1-不包含
     *
     * @param isfee 是否包含手续费 0-包含 1-不包含
     */
    public void setIsfee(String isfee) {
        this.isfee = isfee;
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