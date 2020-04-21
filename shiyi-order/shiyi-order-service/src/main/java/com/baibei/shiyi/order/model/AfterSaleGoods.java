package com.baibei.shiyi.order.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_aftersale_goods")
public class AfterSaleGoods {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal amount;

    /**
     * 货号
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 属性
     */
    @Column(name = "skuProperty")
    private String skuproperty;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 关联的商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 服务单号
     */
    @Column(name = "server_no")
    private String serverNo;

    /**
     * 子订单编号
     */
    @Column(name = "order_item_no")
    private String orderItemNo;

    /**
     *  商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    @Column(name = "shelf_type")
    private String shelfType;

    /**
     * 售后类型,return-退货,exchange-换货
     */
    private String type;

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
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商品图片
     *
     * @return image - 商品图片
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置商品图片
     *
     * @param image 商品图片
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 获取商品名称
     *
     * @return name - 商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name 商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取价格
     *
     * @return amount - 价格
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置价格
     *
     * @param amount 价格
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * 获取属性
     *
     * @return skuProperty - 属性
     */
    public String getSkuproperty() {
        return skuproperty;
    }

    /**
     * 设置属性
     *
     * @param skuproperty 属性
     */
    public void setSkuproperty(String skuproperty) {
        this.skuproperty = skuproperty;
    }

    /**
     * 获取数量
     *
     * @return quantity - 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取关联的商品id
     *
     * @return product_id - 关联的商品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置关联的商品id
     *
     * @param productId 关联的商品id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取服务单号
     *
     * @return server_no - 服务单号
     */
    public String getServerNo() {
        return serverNo;
    }

    /**
     * 设置服务单号
     *
     * @param serverNo 服务单号
     */
    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

    /**
     * 获取子订单编号
     *
     * @return order_item_no - 子订单编号
     */
    public String getOrderItemNo() {
        return orderItemNo;
    }

    /**
     * 设置子订单编号
     *
     * @param orderItemNo 子订单编号
     */
    public void setOrderItemNo(String orderItemNo) {
        this.orderItemNo = orderItemNo;
    }

    /**
     * 获取 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     *
     * @return shelf_type -  商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    public String getShelfType() {
        return shelfType;
    }

    /**
     * 设置 商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     *
     * @param shelfType  商品类型（send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品）
     */
    public void setShelfType(String shelfType) {
        this.shelfType = shelfType;
    }

    /**
     * 获取售后类型,return-退货,exchange-换货
     *
     * @return type - 售后类型,return-退货,exchange-换货
     */
    public String getType() {
        return type;
    }

    /**
     * 设置售后类型,return-退货,exchange-换货
     *
     * @param type 售后类型,return-退货,exchange-换货
     */
    public void setType(String type) {
        this.type = type;
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