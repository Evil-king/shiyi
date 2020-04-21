package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_shelf_bean_ref")
public class ShelfBeanRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     */
    @Column(name = "bean_type")
    private String beanType;

    /**
     * 上架id
     */
    @Column(name = "shelf_id")
    private Long shelfId;

    /**
     * 单位（percent=百分比；integral=积分）
     */
    private String unit;

    /**
     * 值
     */
    private BigDecimal value;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    private Byte flag;

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
     * 获取积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     *
     * @return bean_type - 积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     */
    public String getBeanType() {
        return beanType;
    }

    /**
     * 设置积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     *
     * @param beanType 积分类型（consumption=消费积分；exchange=兑换积分；shiyi=屹家无忧）
     */
    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    /**
     * 获取上架id
     *
     * @return shelf_id - 上架id
     */
    public Long getShelfId() {
        return shelfId;
    }

    /**
     * 设置上架id
     *
     * @param shelfId 上架id
     */
    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    /**
     * 获取单位（percent=百分比；integral=积分）
     *
     * @return unit - 单位（percent=百分比；integral=积分）
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位（percent=百分比；integral=积分）
     *
     * @param unit 单位（percent=百分比；integral=积分）
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取值
     *
     * @return value - 值
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return flag
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * @param flag
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}