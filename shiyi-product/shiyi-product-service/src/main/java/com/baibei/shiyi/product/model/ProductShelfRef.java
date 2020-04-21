package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_shelf_ref")
public class ProductShelfRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联商品上架id
     */
    @Column(name = "shelf_id")
    private Long shelfId;

    /**
     * 关联sku_id
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 上架价格
     */
    @Column(name = "shelf_price")
    private BigDecimal shelfPrice;

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
     * 获取关联商品上架id
     *
     * @return shelf_id - 关联商品上架id
     */
    public Long getShelfId() {
        return shelfId;
    }

    /**
     * 设置关联商品上架id
     *
     * @param shelfId 关联商品上架id
     */
    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    /**
     * 获取关联sku_id
     *
     * @return sku_id - 关联sku_id
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置关联sku_id
     *
     * @param skuId 关联sku_id
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * 获取上架价格
     *
     * @return shelf_price - 上架价格
     */
    public BigDecimal getShelfPrice() {
        return shelfPrice;
    }

    /**
     * 设置上架价格
     *
     * @param shelfPrice 上架价格
     */
    public void setShelfPrice(BigDecimal shelfPrice) {
        this.shelfPrice = shelfPrice;
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