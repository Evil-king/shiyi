package com.baibei.shiyi.order.feign.base.dto;

/**
 * @Classname OrderUpdate
 * @Description 交易所向清算中心更新用户订单状态信息（确认收货，退款，撤销）入参dto
 * @Date 2019/12/4 19:26
 * @Created by Longer
 */
public class OrderUpdate {
    /**
     * 交易所代码
     */
    private String exchangeId;

    /**
     * 订单号
     */
    private String tradeOrderNo;

    /**
     * 订单状态(1-待发货，2-撤销，3-确认收货，4-退款)
     */
    private String goodsStatus;

    /**
     * 发生时间(HHmmss)
     */
    private Integer occurTime;

    /**
     * 交易日期(YYYYMMDD)（确认收款，退款，撤销日期）
     */
    private Integer initDate;

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

    public String getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(String goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Integer getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Integer occurTime) {
        this.occurTime = occurTime;
    }

    public Integer getInitDate() {
        return initDate;
    }

    public void setInitDate(Integer initDate) {
        this.initDate = initDate;
    }

    @Override
    public String toString() {
        return "OrderUpdate{" +
                "exchangeId='" + exchangeId + '\'' +
                ", tradeOrderNo='" + tradeOrderNo + '\'' +
                ", goodsStatus='" + goodsStatus + '\'' +
                ", occurTime=" + occurTime +
                ", initDate=" + initDate +
                '}';
    }
}
