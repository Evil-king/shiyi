package com.baibei.shiyi.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_risk_trade")
public class RiskTrade {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易是否开启，1=开启交易；0=关闭交易
     */
    @Column(name = "open_flag")
    private Byte openFlag;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 生效时间
     */
    @Column(name = "effect_time")
    private Date effectTime;

    /**
     * 失效时间
     */
    @Column(name = "failure_time")
    private Date failureTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否删除(1:正常，0:删除)
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
     * 获取交易是否开启，1=开启交易；0=关闭交易
     *
     * @return open_flag - 交易是否开启，1=开启交易；0=关闭交易
     */
    public Byte getOpenFlag() {
        return openFlag;
    }

    /**
     * 设置交易是否开启，1=开启交易；0=关闭交易
     *
     * @param openFlag 交易是否开启，1=开启交易；0=关闭交易
     */
    public void setOpenFlag(Byte openFlag) {
        this.openFlag = openFlag;
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
     * 获取生效时间
     *
     * @return effect_time - 生效时间
     */
    public Date getEffectTime() {
        return effectTime;
    }

    /**
     * 设置生效时间
     *
     * @param effectTime 生效时间
     */
    public void setEffectTime(Date effectTime) {
        this.effectTime = effectTime;
    }

    /**
     * 获取失效时间
     *
     * @return failure_time - 失效时间
     */
    public Date getFailureTime() {
        return failureTime;
    }

    /**
     * 设置失效时间
     *
     * @param failureTime 失效时间
     */
    public void setFailureTime(Date failureTime) {
        this.failureTime = failureTime;
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
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}