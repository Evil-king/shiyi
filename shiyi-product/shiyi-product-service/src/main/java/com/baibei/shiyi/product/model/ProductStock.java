package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_stock")
public class ProductStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联商品sku
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 关联商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品货号
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 库存
     */
    private BigDecimal stock;

    /**
     * 库存单位
     */
    private String unit;

    /**
     * 销量
     */
    @Column(name = "sell_count")
    private BigDecimal sellCount;

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
     * 获取关联商品sku
     *
     * @return sku_id - 关联商品sku
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置关联商品sku
     *
     * @param skuId 关联商品sku
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * 获取关联商品id
     *
     * @return product_id - 关联商品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置关联商品id
     *
     * @param productId 关联商品id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取库存
     *
     * @return stock - 库存
     */
    public BigDecimal getStock() {
        return stock;
    }

    /**
     * 设置库存
     *
     * @param stock 库存
     */
    public void setStock(BigDecimal stock) {
        this.stock = stock;
    }

    /**
     * 获取库存单位
     *
     * @return unit - 库存单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置库存单位
     *
     * @param unit 库存单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取销量
     *
     * @return sell_count - 销量
     */
    public BigDecimal getSellCount() {
        return sellCount;
    }

    /**
     * 设置销量
     *
     * @param sellCount 销量
     */
    public void setSellCount(BigDecimal sellCount) {
        this.sellCount = sellCount;
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

    public String getSpuNo() {
        return spuNo;
    }

    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }
}