package com.baibei.shiyi.order.feign.base.dto;

/**
 * @Classname OrderPay
 * @Description 交易所向清算中心更新用户订单状态信息（确认收货，退款，撤销）入参dto
 * @Date 2019/12/06 16:35
 * @Created by Longer
 */
public class OrderPay {
    /**
     * 交易所代码
     */
    private String exchangeId;

    /**
     * 交易日期(YYYYMMDD)
     */
    private Integer initDate;

    /**
     * 订单号
     */
    private String tradeOrderNo;

    /**
     * 交易金额（分）
     */
    private Long tradeAmount;

    /**
     * 交易流水号
     */
    private String tradeNo;

    /**
     * 交易完成时间(yyyyMMddHHmmss)
     */
    private String finishTime;

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Integer getInitDate() {
        return initDate;
    }

    public void setInitDate(Integer initDate) {
        this.initDate = initDate;
    }

    public String getTradeOrderNo() {
        return tradeOrderNo;
    }

    public void setTradeOrderNo(String tradeOrderNo) {
        this.tradeOrderNo = tradeOrderNo;
    }

    public Long getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Long tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "OrderPay{" +
                "exchangeId='" + exchangeId + '\'' +
                ", initDate=" + initDate +
                ", tradeOrderNo='" + tradeOrderNo + '\'' +
                ", tradeAmount=" + tradeAmount +
                ", tradeNo='" + tradeNo + '\'' +
                ", finishTime='" + finishTime + '\'' +
                '}';
    }
}
