package com.baibei.shiyi.product.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Table(name = "tbl_pro_product_shelf")
public class ProductShelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 上架编码
     */
    @Column(name = "shelf_no")
    private String shelfNo;

    @Column(name = "spu_no")
    private String spuNo;

    @Column(name = "product_id")
    private Long productId;

    /**
     * 关联前端类目id
     */
   /* @Column(name = "category_id")
    private Long categoryId;*/

    /**
     * 商品上架名称
     */
    @Column(name = "product_shelf_name")
    private String productShelfName;

    /**
     * 划线价
     */
    @Column(name = "line_price")
    private BigDecimal linePrice;

    /**
     * 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
    private String module;

    /**
     * 配售权
     */
    private BigDecimal plan;

    /**
     * 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    private String unit;

    /**
     * 积分最多抵扣值
     */
    private BigDecimal maxdetuch;

    /**
     * 关联机构Id
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    private String source;

    /**
     * 分润规则
     */
    @Column(name = "separet_benefit")
    private String separetBenefit;

    /**
     * 运费模板（free=包邮）
     */
    @Column(name = "freight_type")
    private String freightType;

    /**
     * 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    @Column(name = "integral_type")
    private String integralType;

    /**
     * 上下架（shelt=上架，unshelf=下架）
     */
    private String status;

    /**
     * 商品类型(send_integral=赠送积分商品；consume_ingtegral=消费积分商品；transfer_product=交割商品)
     */
    @Column(name = "shelf_type")
    private String shelfType;

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
     * 上架时间
     */
    private Date shelfTime;

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
     * 获取上架编码
     *
     * @return shelf_no - 上架编码
     */
    public String getShelfNo() {
        return shelfNo;
    }

    /**
     * 设置上架编码
     *
     * @param shelfNo 上架编码
     */
    public void setShelfNo(String shelfNo) {
        this.shelfNo = shelfNo;
    }


    /**
     * 获取商品上架名称
     *
     * @return product_shelf_name - 商品上架名称
     */
    public String getProductShelfName() {
        return productShelfName;
    }

    /**
     * 设置商品上架名称
     *
     * @param productShelfName 商品上架名称
     */
    public void setProductShelfName(String productShelfName) {
        this.productShelfName = productShelfName;
    }

    /**
     * 获取划线价
     *
     * @return line_price - 划线价
     */
    public BigDecimal getLinePrice() {
        return linePrice;
    }

    /**
     * 设置划线价
     *
     * @param linePrice 划线价
     */
    public void setLinePrice(BigDecimal linePrice) {
        this.linePrice = linePrice;
    }

    /**
     * 获取商品仓（integralmodule=积分舱，firstmodule=头等舱）
     *
     * @return module - 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
    public String getModule() {
        return module;
    }

    /**
     * 设置商品仓（integralmodule=积分舱，firstmodule=头等舱）
     *
     * @param module 商品仓（integralmodule=积分舱，firstmodule=头等舱）
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * 获取配售权
     *
     * @return plan - 配售权
     */
    public BigDecimal getPlan() {
        return plan;
    }

    /**
     * 设置配售权
     *
     * @param plan 配售权
     */
    public void setPlan(BigDecimal plan) {
        this.plan = plan;
    }

    /**
     * 获取积分最多抵扣单位(percent=百分比，rmb=人民币)
     *
     * @return unit - 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置积分最多抵扣单位(percent=百分比，rmb=人民币)
     *
     * @param unit 积分最多抵扣单位(percent=百分比，rmb=人民币)
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取积分最多抵扣值
     *
     * @return maxdetuch - 积分最多抵扣值
     */
    public BigDecimal getMaxdetuch() {
        return maxdetuch;
    }

    /**
     * 设置积分最多抵扣值
     *
     * @param maxdetuch 积分最多抵扣值
     */
    public void setMaxdetuch(BigDecimal maxdetuch) {
        this.maxdetuch = maxdetuch;
    }

    /**
     * 获取关联机构Id
     *
     * @return org_id - 关联机构Id
     */
    public Long getOrgId() {
        return orgId;
    }

    /**
     * 设置关联机构Id
     *
     * @param orgId 关联机构Id
     */
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     *
     * @return source - 来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     *
     * @param source 来源（integralmodule=积分舱，firstmodule=头等舱，upmodule=升仓，extendmodule=传承仓，sharemodule=共享仓）
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取分润规则
     *
     * @return separet_benefit - 分润规则
     */
    public String getSeparetBenefit() {
        return separetBenefit;
    }

    /**
     * 设置分润规则
     *
     * @param separetBenefit 分润规则
     */
    public void setSeparetBenefit(String separetBenefit) {
        this.separetBenefit = separetBenefit;
    }

    /**
     * 获取运费模板（free=包邮）
     *
     * @return freight_type - 运费模板（free=包邮）
     */
    public String getFreightType() {
        return freightType;
    }

    /**
     * 设置运费模板（free=包邮）
     *
     * @param freightType 运费模板（free=包邮）
     */
    public void setFreightType(String freightType) {
        this.freightType = freightType;
    }

    /**
     * 获取积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     *
     * @return integral_type - 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    public String getIntegralType() {
        return integralType;
    }

    /**
     * 设置积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     *
     * @param integralType 积分类型（deliveryintegral=提货积分，comsumeintegral=消费积分，yijiabao=屹家保）
     */
    public void setIntegralType(String integralType) {
        this.integralType = integralType;
    }

    /**
     * 获取上下架（shelt=上架，unshelf=下架）
     *
     * @return status - 上下架（shelt=上架，unshelf=下架）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置上下架（shelt=上架，unshelf=下架）
     *
     * @param status 上下架（shelt=上架，unshelf=下架）
     */
    public void setStatus(String status) {
        this.status = status;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSpuNo() {
        return spuNo;
    }

    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
    }

    public Date getShelfTime() {
        return shelfTime;
    }

    public void setShelfTime(Date shelfTime) {
        this.shelfTime = shelfTime;
    }

    public String getShelfType() {
        return shelfType;
    }

    public void setShelfType(String shelfType) {
        this.shelfType = shelfType;
    }
}