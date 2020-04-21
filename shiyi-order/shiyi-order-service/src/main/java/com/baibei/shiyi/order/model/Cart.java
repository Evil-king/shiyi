package com.baibei.shiyi.order.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ord_cart")
public class Cart {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联客户编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 关联商品上架ID
     */
    @Column(name = "shelf_id")
    private Long shelfId;

    /**
     * 关联商品规格ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 商品主图
     */
    @Column(name = "product_img")
    private String productImg;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品sku规格(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    @Column(name = "sku_property")
    private String skuProperty;

    /**
     * 加入购物车时的价格
     */
    private BigDecimal amount;

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
     * 获取关联客户编码
     *
     * @return customer_no - 关联客户编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置关联客户编码
     *
     * @param customerNo 关联客户编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取关联商品上架ID
     *
     * @return shelf_id - 关联商品上架ID
     */
    public Long getShelfId() {
        return shelfId;
    }

    /**
     * 设置关联商品上架ID
     *
     * @param shelfId 关联商品上架ID
     */
    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    /**
     * 获取关联商品规格ID
     *
     * @return sku_id - 关联商品规格ID
     */
    public Long getSkuId() {
        return skuId;
    }

    /**
     * 设置关联商品规格ID
     *
     * @param skuId 关联商品规格ID
     */
    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    /**
     * 获取商品主图
     *
     * @return product_img - 商品主图
     */
    public String getProductImg() {
        return productImg;
    }

    /**
     * 设置商品主图
     *
     * @param productImg 商品主图
     */
    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    /**
     * 获取商品名称
     *
     * @return product_name - 商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置商品名称
     *
     * @param productName 商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取购买数量
     *
     * @return quantity - 购买数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置购买数量
     *
     * @param quantity 购买数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取商品sku规格(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     *
     * @return sku_property - 商品sku规格(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    public String getSkuProperty() {
        return skuProperty;
    }

    /**
     * 设置商品sku规格(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     *
     * @param skuProperty 商品sku规格(json格式。如：[{"颜色":"红色"},{"尺码":"25"}]
     */
    public void setSkuProperty(String skuProperty) {
        this.skuProperty = skuProperty;
    }

    /**
     * 获取加入购物车时的价格
     *
     * @return amount - 加入购物车时的价格
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置加入购物车时的价格
     *
     * @param amount 加入购物车时的价格
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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