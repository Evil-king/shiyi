package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    /**
     * 商品一句话描述
     */
    @Column(name = "product_desc")
    private String productDesc;

    /**
     * 货号
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 关联后台类目id
     */
    @Column(name = "type_id")
    private Long typeId;

    /**
     * 关联品牌id
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 品牌名称（冗余字段）
     */
    @Column(name = "brand_title")
    private String brandTitle;

    /**
     * 商品主图
     */
    @Column(name = "product_img")
    private String productImg;

    /**
     * 外部ID
     */
    @Column(name = "out_no")
    private String outNo;

    /**
     * 产品参数（json格式）
     */
    private String parameter;

    /**
     * 已选sku，用户前端数据回显
     */
    @Column(name = "selected_sku")
    private String selectedSku;

    /**
     * 基础销量
     */
    @Column(name = "common_sell_count")
    private BigDecimal commonSellCount;
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
     * @return product_name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取商品一句话描述
     *
     * @return product_desc - 商品一句话描述
     */
    public String getProductDesc() {
        return productDesc;
    }

    /**
     * 设置商品一句话描述
     *
     * @param productDesc 商品一句话描述
     */
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * 获取货号
     *
     * @return spu_no - 货号
     */
    public String getSpuNo() {
        return spuNo;
    }

    /**
     * 设置货号
     *
     * @param spuNo 货号
     */
    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    /**
     * 获取关联后台类目id
     *
     * @return type_id - 关联后台类目id
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * 设置关联后台类目id
     *
     * @param typeId 关联后台类目id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取关联品牌id
     *
     * @return brand_id - 关联品牌id
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * 设置关联品牌id
     *
     * @param brandId 关联品牌id
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * 获取品牌名称（冗余字段）
     *
     * @return brand_title - 品牌名称（冗余字段）
     */
    public String getBrandTitle() {
        return brandTitle;
    }

    /**
     * 设置品牌名称（冗余字段）
     *
     * @param brandTitle 品牌名称（冗余字段）
     */
    public void setBrandTitle(String brandTitle) {
        this.brandTitle = brandTitle;
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
     * 获取外部ID
     *
     * @return out_no - 外部ID
     */
    public String getOutNo() {
        return outNo;
    }

    /**
     * 设置外部ID
     *
     * @param outNo 外部ID
     */
    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    /**
     * 获取产品参数（json格式）
     *
     * @return parameter - 产品参数（json格式）
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * 设置产品参数（json格式）
     *
     * @param parameter 产品参数（json格式）
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
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

    public String getSelectedSku() {
        return selectedSku;
    }

    public void setSelectedSku(String selectedSku) {
        this.selectedSku = selectedSku;
    }

    public BigDecimal getCommonSellCount() {
        return commonSellCount;
    }

    public void setCommonSellCount(BigDecimal commonSellCount) {
        this.commonSellCount = commonSellCount;
    }
}