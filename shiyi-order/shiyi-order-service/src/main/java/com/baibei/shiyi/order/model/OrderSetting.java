package com.baibei.shiyi.order.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ord_setting")
public class OrderSetting {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 秒杀订单超时关闭时间(分)
     */
    @Column(name = "flash_order_overtime")
    private Integer flashOrderOvertime;

    /**
     * 正常订单超时时间(分)
     */
    @Column(name = "normal_order_overtime")
    private Integer normalOrderOvertime;

    /**
     * 发货后自动确认收货时间（天）
     */
    @Column(name = "confirm_overtime")
    private Integer confirmOvertime;

    /**
     * 自动完成交易时间，不能申请售后（天）
     */
    @Column(name = "finish_overtime")
    private Integer finishOvertime;

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
     * 获取秒杀订单超时关闭时间(分)
     *
     * @return flash_order_overtime - 秒杀订单超时关闭时间(分)
     */
    public Integer getFlashOrderOvertime() {
        return flashOrderOvertime;
    }

    /**
     * 设置秒杀订单超时关闭时间(分)
     *
     * @param flashOrderOvertime 秒杀订单超时关闭时间(分)
     */
    public void setFlashOrderOvertime(Integer flashOrderOvertime) {
        this.flashOrderOvertime = flashOrderOvertime;
    }

    /**
     * 获取正常订单超时时间(分)
     *
     * @return normal_order_overtime - 正常订单超时时间(分)
     */
    public Integer getNormalOrderOvertime() {
        return normalOrderOvertime;
    }

    /**
     * 设置正常订单超时时间(分)
     *
     * @param normalOrderOvertime 正常订单超时时间(分)
     */
    public void setNormalOrderOvertime(Integer normalOrderOvertime) {
        this.normalOrderOvertime = normalOrderOvertime;
    }

    /**
     * 获取发货后自动确认收货时间（天）
     *
     * @return confirm_overtime - 发货后自动确认收货时间（天）
     */
    public Integer getConfirmOvertime() {
        return confirmOvertime;
    }

    /**
     * 设置发货后自动确认收货时间（天）
     *
     * @param confirmOvertime 发货后自动确认收货时间（天）
     */
    public void setConfirmOvertime(Integer confirmOvertime) {
        this.confirmOvertime = confirmOvertime;
    }

    /**
     * 获取自动完成交易时间，不能申请售后（天）
     *
     * @return finish_overtime - 自动完成交易时间，不能申请售后（天）
     */
    public Integer getFinishOvertime() {
        return finishOvertime;
    }

    /**
     * 设置自动完成交易时间，不能申请售后（天）
     *
     * @param finishOvertime 自动完成交易时间，不能申请售后（天）
     */
    public void setFinishOvertime(Integer finishOvertime) {
        this.finishOvertime = finishOvertime;
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