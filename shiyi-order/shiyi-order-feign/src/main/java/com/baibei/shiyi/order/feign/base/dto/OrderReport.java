package com.baibei.shiyi.order.feign.base.dto;

import java.io.Serializable;

/**
 * @Classname OrderReport
 * @Description 向清所推送用户订单信息入参dto
 * @Date 2019/12/4 18:54
 * @Created by Longer
 */
public class OrderReport implements Serializable {
    /**
     * 交易日期(YYYYMMDD)
     */
    private Integer initDate;

    /**
     * 交易所代码
     */
    private String exchangeId;

    /**
     * 订单号
     */
    private String tradeOrderNo;

    /**
     * 卖方会员名称
     */
    private String sellerAccountName;

    /**
     * 卖方会员编号(交易所内部的会员编号，唯一标识会员)
     */
    private String sellerMemCode;

    /**
     * 交易所资金账号
     */
    private String sellerExchangeFundAccount;

    /**
     * 卖方经纪会员编号
     */
    private String brokerCode;

    /**
     * 卖方手机号(缺省值)
     */
    private String sellerTelNo;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 买方会员名称
     */
    private String buyerAccountName;

    /**
     * 买方会员编号(交易所内部的会员编号，唯一标识会员)
     */
    private String buyerMemCode;

    /**
     * 买方交易所资金账号
     */
    private String buyerExchangeFundAccount;

    /**
     * 买方经纪会员编码
     */
    private String buyerBrokerCode;

    /**
     * 买方手机号（缺省值）
     */
    private String buyerTelNo;

    /**
     * 买方证件号码（缺省值）
     */
    private String idNo;

    /**
     * 商品数量
     */
    private Double dealQuantity;

    /**
     * 商品成交类型（1-券，2-资金，3-部分券部分资金）
     */
    private String goodsDealType;

    /**
     * 商品单价
     */
    private Long dealPrice;

    /**
     * 商品券的总面额(用券购买的金额)
     */
    private Long dealTicketPrice;

    /**
     * 商品总金额（用资金购买的金额）
     */
    private Long dealfundPrice;

    /**
     * 商品总价值（券+ 金额）
     */
    private Long dealTotalPrice;

    /**
     * 下单时间(HHmmss)
     */
    private Integer dealTime;

    /**
     * 订单状态(1-待发货)
     */
    private String goodsStatus;

    /**
     * 备注信息
     */
    private String remark;

    public Integer getInitDate() {
        return initDate;
    }

    public void setInitDate(Integer initDate) {
        this.initDate = initDate;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getTradeOrderNo() {
        return tradeOrderNo;
    }

    public void setTradeOrderNo(String tradeOrderNo) {
        this.tradeOrderNo = tradeOrderNo;
    }

    public String getSellerAccountName() {
        return sellerAccountName;
    }

    public void setSellerAccountName(String sellerAccountName) {
        this.sellerAccountName = sellerAccountName;
    }

    public String getSellerMemCode() {
        return sellerMemCode;
    }

    public void setSellerMemCode(String sellerMemCode) {
        this.sellerMemCode = sellerMemCode;
    }

    public String getSellerExchangeFundAccount() {
        return sellerExchangeFundAccount;
    }

    public void setSellerExchangeFundAccount(String sellerExchangeFundAccount) {
        this.sellerExchangeFundAccount = sellerExchangeFundAccount;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getSellerTelNo() {
        return sellerTelNo;
    }

    public void setSellerTelNo(String sellerTelNo) {
        this.sellerTelNo = sellerTelNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBuyerAccountName() {
        return buyerAccountName;
    }

    public void setBuyerAccountName(String buyerAccountName) {
        this.buyerAccountName = buyerAccountName;
    }

    public String getBuyerMemCode() {
        return buyerMemCode;
    }

    public void setBuyerMemCode(String buyerMemCode) {
        this.buyerMemCode = buyerMemCode;
    }

    public String getBuyerExchangeFundAccount() {
        return buyerExchangeFundAccount;
    }

    public void setBuyerExchangeFundAccount(String buyerExchangeFundAccount) {
        this.buyerExchangeFundAccount = buyerExchangeFundAccount;
    }

    public String getBuyerTelNo() {
        return buyerTelNo;
    }

    public void setBuyerTelNo(String buyerTelNo) {
        this.buyerTelNo = buyerTelNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public Double getDealQuantity() {
        return dealQuantity;
    }

    public void setDealQuantity(Double dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    public String getGoodsDealType() {
        return goodsDealType;
    }

    public void setGoodsDealType(String goodsDealType) {
        this.goodsDealType = goodsDealType;
    }

    public Long getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Long dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Long getDealTicketPrice() {
        return dealTicketPrice;
    }

    public void setDealTicketPrice(Long dealTicketPrice) {
        this.dealTicketPrice = dealTicketPrice;
    }

    public Long getDealfundPrice() {
        return dealfundPrice;
    }

    public void setDealfundPrice(Long dealfundPrice) {
        this.dealfundPrice = dealfundPrice;
    }

    public Long getDealTotalPrice() {
        return dealTotalPrice;
    }

    public void setDealTotalPrice(Long dealTotalPrice) {
        this.dealTotalPrice = dealTotalPrice;
    }

    public Integer getDealTime() {
        return dealTime;
    }

    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuyerBrokerCode() {
        return buyerBrokerCode;
    }

    public void setBuyerBrokerCode(String buyerBrokerCode) {
        this.buyerBrokerCode = buyerBrokerCode;
    }

    @Override
    public String toString() {
        return "OrderReport{" +
                "initDate=" + initDate +
                ", exchangeId='" + exchangeId + '\'' +
                ", tradeOrderNo='" + tradeOrderNo + '\'' +
                ", sellerAccountName='" + sellerAccountName + '\'' +
                ", sellerMemCode='" + sellerMemCode + '\'' +
                ", sellerExchangeFundAccount='" + sellerExchangeFundAccount + '\'' +
                ", brokerCode='" + brokerCode + '\'' +
                ", sellerTelNo='" + sellerTelNo + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", buyerAccountName='" + buyerAccountName + '\'' +
                ", buyerMemCode='" + buyerMemCode + '\'' +
                ", buyerExchangeFundAccount='" + buyerExchangeFundAccount + '\'' +
                ", buyerTelNo='" + buyerTelNo + '\'' +
                ", idNo='" + idNo + '\'' +
                ", dealQuantity=" + dealQuantity +
                ", goodsDealType='" + goodsDealType + '\'' +
                ", dealPrice=" + dealPrice +
                ", dealTicketPrice=" + dealTicketPrice +
                ", dealfundPrice=" + dealfundPrice +
                ", dealTotalPrice=" + dealTotalPrice +
                ", dealTime=" + dealTime +
                ", goodsStatus='" + goodsStatus + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
