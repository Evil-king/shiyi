package com.baibei.shiyi.cash.feign.base.dto;


import com.baibei.shiyi.common.tool.api.BaseReq;

/**
 * 功能说明：交易所发起的出入金响应请求类<br>
 * 注意事项：<br>
 * 系统版本：version 1.0<br>
 * 开发人员：chengls13783<br>
 * 开发时间：2015年11月16日<br>
 *
 */
@SuppressWarnings("serial")
public class FundCashReq extends BaseReq {

    private String type;

    /**业务流水号
     * Y 32*/
    private String requestId;

    /**交易日
     * Y 8*/
    private Long initDate;

    /**交易所代码
     * Y 30*/
    private String exchangeId;

    /**交易所资金账号
     * Y 20*/
    private String exchangeFundAccount;

    /**资金密码
     * N 20*/
    private String fundPassword;

    /**币种编码
     * Y 3*/
    private String moneyType;

    /**业务类型
     * Y 2*/
    private String bisinType;

    /**银行产品编码
     * Y 20*/
    private String bankProCode;

    /**银行账户名
     * Y 64*/
    private String accountName;

    /**银行账号
     * Y 32*/
    private String bankAccount;

    /**发生金额
     * Y*/
    private Long occurAmount;

    /**备注
     * N 255*/
    private String remark;

    /**业务时间
     * Y*/
    private Long busiDatetime;

//	/**银行账户类型
//	 * N 1 民生必填*/
//	private String bankAccountType;
//
    /**会员全称
     * N 200 农行、工行、平安必填*/
    private String fullName;
    //C1
    private String memberType;//客户类型

    /**证件类型
     * N 4 出金（平安、招行必填），入金（平安、民生、招行必填）*/
    private String 	idKind;

    /**证件号码
     * N 32 平安、民生、招行必填*/
    private String 	idNo;

    /**银行账户密码
     * N 20 出金(农行、中兴、兴业)，入金（农行、民生、兴业必填）*/
    private String bankPassword;

    /**-------------额外添加---------------------*/
    /**交易来源*/
    private String sourceFrom;

    /**商户号*/
    private String merchantNo;


    /**出入金标识*/
    private String cashType;



    /**------------银行出入金参数strart-------------------**/
    /**银证流水*/
    private String  bankBillNo;

    /**交易编码*/
    private String transCode;

    /**手续费**/
    private Long outPoundage;

    /**核算账号的绑卡账号（银行汇总账号）非必传，有则判断，没有则忽略*/
    private String settleBankAccount;
    /**------------银行出入金参数end-------------------**/

    private Long fetchBalance; //发生前可取金额

    private String uKeyStr;//uKey证书

    private String exOrderNo;//账单号 农行电子商务必填

    private String thirdPayFlag;//三方支付入金标识

    private Long inPoundage;//入金手续费

    /**
     * @return the requestId
     */
    public String getRequestId() {
        return requestId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Long getOutPoundage() {
        return outPoundage;
    }

    public void setOutPoundage(Long outPoundage) {
        this.outPoundage = outPoundage;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the initDate
     */
    public Long getInitDate() {
        return initDate;
    }

    /**
     * @param initDate the initDate to set
     */
    public void setInitDate(Long initDate) {
        this.initDate = initDate;
    }

    /**
     * @return the exchangeId
     */
    public String getExchangeId() {
        return exchangeId;
    }

    /**
     * @param exchangeId the exchangeId to set
     */
    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    /**
     * @return the fundAccount
     */
    public String getExchangeFundAccount() {
        return exchangeFundAccount;
    }

    /**
     * @param fundAccount the fundAccount to set
     */
    public void setExchangeFundAccount(String exchangeFundAccount) {
        this.exchangeFundAccount = exchangeFundAccount;
    }

    /**
     * @return the fundPassword
     */
    public String getFundPassword() {
        return fundPassword;
    }

    /**
     * @param fundPassword the fundPassword to set
     */
    public void setFundPassword(String fundPassword) {
        this.fundPassword = fundPassword;
    }

    /**
     * @return the moneyType
     */
    public String getMoneyType() {
        return moneyType;
    }

    /**
     * @param moneyType the moneyType to set
     */
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    /**
     * @return the bisinType
     */
    public String getBisinType() {
        return bisinType;
    }

    /**
     * @param bisinType the bisinType to set
     */
    public void setBisinType(String bisinType) {
        this.bisinType = bisinType;
    }

    /**
     * @return the bankProductCode
     */
    public String getBankProCode() {
        return bankProCode;
    }

    /**
     * @param bankProductCode the bankProductCode to set
     */
    public void setBankProCode(String bankProCode) {
        this.bankProCode = bankProCode;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the bankAccount
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * @return the occurBalance
     */
    public Long getOccurAmount() {
        return occurAmount;
    }

    /**
     * @param occurBalance the occurBalance to set
     */
    public void setOccurAmount(Long occurAmount) {
        this.occurAmount = occurAmount;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the busiDatetime
     */
    public Long getBusiDatetime() {
        return busiDatetime;
    }

    /**
     * @param busiDatetime the busiDatetime to set
     */
    public void setBusiDatetime(Long busiDatetime) {
        this.busiDatetime = busiDatetime;
    }

    /**
     * @return the sourceFrom
     */
    public String getSourceFrom() {
        return sourceFrom;
    }

    /**
     * @param sourceFrom the sourceFrom to set
     */
    public void setSourceFrom(String sourceFrom) {
        this.sourceFrom = sourceFrom;
    }

    /**
     * @return the idKind
     */
    public String getIdKind() {
        return idKind;
    }

    /**
     * @param idKind the idKind to set
     */
    public void setIdKind(String idKind) {
        this.idKind = idKind;
    }

    /**
     * @return the idNo
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * @param idNo the idNo to set
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

//	/**
//	 * @return the bankAccountType
//	 */
//	public String getBankAccountType() {
//		return bankAccountType;
//	}
//
//	/**
//	 * @param bankAccountType the bankAccountType to set
//	 */
//	public void setBankAccountType(String bankAccountType) {
//		this.bankAccountType = bankAccountType;
//	}

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the bankPassword
     */
    public String getBankPassword() {
        return bankPassword;
    }

    /**
     * @param bankPassword the bankPassword to set
     */
    public void setBankPassword(String bankPassword) {
        this.bankPassword = bankPassword;
    }

    /**
     * @return the merchantNo
     */
    public String getMerchantNo() {
        return merchantNo;
    }

    /**
     * @param merchantNo the merchantNo to set
     */
    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    /**
     * @return the cashType
     */
    public String getCashType() {
        return cashType;
    }

    /**
     * @param cashType the cashType to set
     */
    public void setCashType(String cashType) {
        this.cashType = cashType;
    }

    /**
     * @return the bankBillNo
     */
    public String getBankBillNo() {
        return bankBillNo;
    }

    /**
     * @param bankBillNo the bankBillNo to set
     */
    public void setBankBillNo(String bankBillNo) {
        this.bankBillNo = bankBillNo;
    }

    /**
     * @return the transCode
     */
    public String getTransCode() {
        return transCode;
    }

    /**
     * @param transCode the transCode to set
     */
    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    /***出金用**/
//	public String getLargeBankId(){
//		return "";
//	}
//
//	public String getUnionBankId() {
//		return "";
//	}
//
//	public String getCrossFlag() {
//		return "";
//	}
//
//	public String getOutAcctIdBankName() {
//		return "";
//	}

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the settleBankAccount
     */
    public String getSettleBankAccount() {
        return settleBankAccount;
    }

    /**
     * @param settleBankAccount the settleBankAccount to set
     */
    public void setSettleBankAccount(String settleBankAccount) {
        this.settleBankAccount = settleBankAccount;
    }

    public String getThirdPayFlag() {
        return thirdPayFlag;
    }

    public void setThirdPayFlag(String thirdPayFlag) {
        this.thirdPayFlag = thirdPayFlag;
    }

    public Long getInPoundage() {
        return inPoundage;
    }

    public void setInPoundage(Long inPoundage) {
        this.inPoundage = inPoundage;
    }

    public Long getFetchBalance() {
        return fetchBalance;
    }

    public void setFetchBalance(Long fetchBalance) {
        this.fetchBalance = fetchBalance;
    }

    public String getuKeyStr() {
        return uKeyStr;
    }

    public void setuKeyStr(String uKeyStr) {
        this.uKeyStr = uKeyStr;
    }

    public String getExOrderNo() {
        return exOrderNo;
    }

    public void setExOrderNo(String exOrderNo) {
        this.exOrderNo = exOrderNo;
    }


}

