package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_sku")
public class ProductSku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品货号
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 规格编码
     */
    @Column(name = "sku_no")
    private String skuNo;

    /**
     * 商品sku(json格式。如：{"颜色":"红色","尺寸","30"}
     */
    @Column(name = "sku_property")
    private String skuProperty;

    /**
     * 平台价
     */
    @Column(name = "platform_price")
    private BigDecimal platformPrice;

    /**
     * 排序
     */
    private Integer seq;

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
     * @return product_id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取规格编码
     *
     * @return sku_no - 规格编码
     */
    public String getSkuNo() {
        return skuNo;
    }

    /**
     * 设置规格编码
     *
     * @param skuNo 规格编码
     */
    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    /**
     * 获取商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     *
     * @return sku_property - 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    public String getSkuProperty() {
        return skuProperty;
    }

    /**
     * 设置商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     *
     * @param skuProperty 商品sku(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    public void setSkuProperty(String skuProperty) {
        this.skuProperty = skuProperty;
    }

    /**
     * 获取平台价
     *
     * @return platform_price - 平台价
     */
    public BigDecimal getPlatformPrice() {
        return platformPrice;
    }

    /**
     * 设置平台价
     *
     * @param platformPrice 平台价
     */
    public void setPlatformPrice(BigDecimal platformPrice) {
        this.platformPrice = platformPrice;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
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