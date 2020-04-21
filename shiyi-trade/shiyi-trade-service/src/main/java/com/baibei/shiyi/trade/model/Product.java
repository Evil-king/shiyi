package com.baibei.shiyi.trade.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_product")
public class Product {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 商品icon
     */
    @Column(name = "product_icon")
    private String productIcon;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 发行价
     */
    @Column(name = "issue_price")
    private BigDecimal issuePrice;

    /**
     * 仓单兑换价
     */
    @Column(name = "exchange_price")
    private BigDecimal exchangePrice;

    /**
     * 最小交易单位
     */
    @Column(name = "min_trade")
    private Integer minTrade;

    /**
     * 最大卖出量
     */
    @Column(name = "max_trade")
    private Integer maxTrade;

    /**
     * 最高报价
     */
    @Column(name = "highest_quoted_price")
    private BigDecimal highestQuotedPrice;

    /**
     * 最低报价
     */
    @Column(name = "lowest_quoted_price")
    private BigDecimal lowestQuotedPrice;

    /**
     * 交易状态，wait=待上市；trading=交易中；exit=退市
     */
    @Column(name = "trade_status")
    private String tradeStatus;

    /**
     * 上市日期
     */
    @Column(name = "market_time")
    private Date marketTime;

    /**
     * 单位
     */
    private String unit;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 执行人
     */
    private String modifier;

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
     * 是否删除(1:正常，0:删除)
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
     * 获取商品icon
     *
     * @return product_icon - 商品icon
     */
    public String getProductIcon() {
        return productIcon;
    }

    /**
     * 设置商品icon
     *
     * @param productIcon 商品icon
     */
    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    /**
     * 获取商品交易编码
     *
     * @return product_trade_no - 商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置商品交易编码
     *
     * @param productTradeNo 商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取发行价
     *
     * @return issue_price - 发行价
     */
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }

    /**
     * 设置发行价
     *
     * @param issuePrice 发行价
     */
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }

    /**
     * 获取仓单兑换价
     *
     * @return exchange_price - 仓单兑换价
     */
    public BigDecimal getExchangePrice() {
        return exchangePrice;
    }

    /**
     * 设置仓单兑换价
     *
     * @param exchangePrice 仓单兑换价
     */
    public void setExchangePrice(BigDecimal exchangePrice) {
        this.exchangePrice = exchangePrice;
    }

    /**
     * 获取最小交易单位
     *
     * @return min_trade - 最小交易单位
     */
    public Integer getMinTrade() {
        return minTrade;
    }

    /**
     * 设置最小交易单位
     *
     * @param minTrade 最小交易单位
     */
    public void setMinTrade(Integer minTrade) {
        this.minTrade = minTrade;
    }

    /**
     * 获取最大卖出量
     *
     * @return max_trade - 最大卖出量
     */
    public Integer getMaxTrade() {
        return maxTrade;
    }

    /**
     * 设置最大卖出量
     *
     * @param maxTrade 最大卖出量
     */
    public void setMaxTrade(Integer maxTrade) {
        this.maxTrade = maxTrade;
    }

    /**
     * 获取最高报价
     *
     * @return highest_quoted_price - 最高报价
     */
    public BigDecimal getHighestQuotedPrice() {
        return highestQuotedPrice;
    }

    /**
     * 设置最高报价
     *
     * @param highestQuotedPrice 最高报价
     */
    public void setHighestQuotedPrice(BigDecimal highestQuotedPrice) {
        this.highestQuotedPrice = highestQuotedPrice;
    }

    /**
     * 获取最低报价
     *
     * @return lowest_quoted_price - 最低报价
     */
    public BigDecimal getLowestQuotedPrice() {
        return lowestQuotedPrice;
    }

    /**
     * 设置最低报价
     *
     * @param lowestQuotedPrice 最低报价
     */
    public void setLowestQuotedPrice(BigDecimal lowestQuotedPrice) {
        this.lowestQuotedPrice = lowestQuotedPrice;
    }

    /**
     * 获取交易状态，wait=待上市；trading=交易中；exit=退市
     *
     * @return trade_status - 交易状态，wait=待上市；trading=交易中；exit=退市
     */
    public String getTradeStatus() {
        return tradeStatus;
    }

    /**
     * 设置交易状态，wait=待上市；trading=交易中；exit=退市
     *
     * @param tradeStatus 交易状态，wait=待上市；trading=交易中；exit=退市
     */
    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * 获取上市日期
     *
     * @return market_time - 上市日期
     */
    public Date getMarketTime() {
        return marketTime;
    }

    /**
     * 设置上市日期
     *
     * @param marketTime 上市日期
     */
    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
    }

    /**
     * 获取单位
     *
     * @return unit - 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
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
     * 获取执行人
     *
     * @return modifier - 执行人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置执行人
     *
     * @param modifier 执行人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
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