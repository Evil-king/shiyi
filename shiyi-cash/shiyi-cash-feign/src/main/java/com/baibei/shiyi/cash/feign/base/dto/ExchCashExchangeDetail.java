package com.baibei.shiyi.cash.feign.base.dto;


import java.io.Serializable;

@SuppressWarnings("serial")
public class ExchCashExchangeDetail implements Serializable{
    private Long initDate; //业务发生日期(YYYYMMDD)
    private String exchangeId; //交易所代码
    private String exchangeFundAccount; //交易所资金账号
    private String bankProCode; //银行产品代码
    private String bankAccount; //银行帐号
    private String serialNo; //银行流水流水号
    private String clearSerialNo;//清算中心流水号
    private String sourceFrom; //发起方类型（1：交易所；2：银行）
    private String bisinType; //银行业务类型（1：普通；2：冲正；3：重发；4：调账）
    private String moneyType; //币种
    private String inoutFlag; //出入金标志（0:出金 1:入金）
    private Long occurAmount; //发生金额
    private String remark; //备注
    private String busiDatetime; //业务时间(yyyyMMddHHmmss)
    private String requestId;//交易网流水号
    private String cashFlowStatus;//出入金处理状态(1=处理中；2=处理成功；3=处理失败)

    private Long beginDate;
    private Long endDate;
    private Long pageSize;

    public Long getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }
    public Long getEndDate() {
        return endDate;
    }
    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }
    public Long getPageSize() {
        return pageSize;
    }
    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
    public Long getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }
    private Long currentPage;

    public Long getInitDate() {
        return initDate;
    }
    public void setInitDate(Long initDate) {
        this.initDate = initDate;
    }
    public String getExchangeId() {
        return exchangeId;
    }
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }
    public String getExchangeFundAccount() {
        return exchangeFundAccount;
    }
    public void setExchangeFundAccount(String exchangeFundAccount) {
        this.exchangeFundAccount = exchangeFundAccount;
    }
    public String getBankProCode() {
        return bankProCode;
    }
    public void setBankProCode(String bankProCode) {
        this.bankProCode = bankProCode;
    }
    public String getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getSourceFrom() {
        return sourceFrom;
    }
    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }
    public String getBisinType() {
        return bisinType;
    }
    public void setBisinType(String bisinType) {
        this.bisinType = bisinType;
    }
    public String getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
    public String getInoutFlag() {
        return inoutFlag;
    }
    public void setInoutFlag(String inoutFlag) {
        this.inoutFlag = inoutFlag;
    }
    public Long getOccurAmount() {
        return occurAmount;
    }
    public void setOccurAmount(Long occurAmount) {
        this.occurAmount = occurAmount;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getBusiDatetime() {
        return busiDatetime;
    }
    public void setBusiDatetime(String busiDatetime) {
        this.busiDatetime = busiDatetime;
    }
    public String getClearSerialNo() {
        return clearSerialNo;
    }
    public void setClearSerialNo(String clearSerialNo) {
        this.clearSerialNo = clearSerialNo;
    }

    public String getCashFlowStatus() {
        return cashFlowStatus;
    }

    public void setCashFlowStatus(String cashFlowStatus) {
        this.cashFlowStatus = cashFlowStatus;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}