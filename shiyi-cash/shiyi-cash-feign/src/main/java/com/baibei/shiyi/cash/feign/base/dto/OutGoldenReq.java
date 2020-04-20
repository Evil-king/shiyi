package com.baibei.shiyi.cash.feign.base.dto;

/**
 * 功能说明：出金请求参数<br>
 * 注意事项：<br>
 * 系统版本：version 1.0<br>
 * 开发人员：chengls13783<br>
 * 开发时间：2015年11月9日<br>
 *
 */
@SuppressWarnings("serial")
public class OutGoldenReq extends FundCashReq {


    /** 会员主体类型
     *C 1*/
    private String memberMainType;

    public String getMemberMainType() {
        return memberMainType;
    }
    public void setMemberMainType(String memberMainType) {
        this.memberMainType = memberMainType;
    }

    //	/**大额行号
//	 * N 20*/
    private String largeBankId;
    //
//	/**联行号
//	 * N 20*/
    private String unionBankId;
//
    /**是否跨行
     * Y 1*/
    private String crossFlag;
    //
//	/**出金账号开户行名
//	 * N 120 出金(平安必填)*/
    private String outAcctIdBankName;


    //	@Override
    public String getLargeBankId() {
        return largeBankId;
    }
    //
//	/**
//	 * @param largeBankId the largeBankId to set
//	 */
    public void setLargeBankId(String largeBankId) {
        this.largeBankId = largeBankId;
    }
    //
//	@Override
    public String getUnionBankId() {
        return unionBankId;
    }
    //
//	/**
//	 * @param unionBankId the unionBankId to set
//	 */
    public void setUnionBankId(String unionBankId) {
        this.unionBankId = unionBankId;
    }
    //
//	@Override
    public String getCrossFlag() {
        return crossFlag;
    }

    /**
     * @param crossFlag the crossFlag to set
     */
    public void setCrossFlag(String crossFlag) {
        this.crossFlag = crossFlag;
    }
    //
//	@Override
    public String getOutAcctIdBankName() {
        return outAcctIdBankName;
    }
    //
    public void setOutAcctIdBankName(String outAcctIdBankName) {
        this.outAcctIdBankName = outAcctIdBankName;
    }
}
