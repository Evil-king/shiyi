package com.baibei.shiyi.common.tool.api;
import java.io.Serializable;

/**
 * 功能说明: 响应基类<br>
 * 注意事项: <br>
 * 系统版本: v1.0<br>
 * 开发人员: <br>
 * 开发时间: <br>
 */
@SuppressWarnings("serial")
public class BaseResp implements Serializable {
    //定义接口返回参数
    private int errorNo = 0;
    private String errorInfo = "操作成功!";
    private String serialNo= "";
    private String tradeSerialNo = "";
    private String custId = "";
    private long outPoundage=0;

    public int getErrorNo() {
        return errorNo;
    }

    public void setErrorNo(int errorNo) {
        this.errorNo = errorNo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTradeSerialNo() {
        return tradeSerialNo;
    }

    public void setTradeSerialNo(String tradeSerialNo) {
        this.tradeSerialNo = tradeSerialNo;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public long getOutPoundage() {
        return outPoundage;
    }

    public void setOutPoundage(long outPoundage) {
        this.outPoundage = outPoundage;
    }

    /**
     * 判断是否出错<br>
     * true - 出错<br>
     * false - 未出错
     *
     * @return b
     */
    public boolean isError() {
        return this.errorNo != 0;
    }

    /**
     * 判断是否成功<br>
     * true - 成功<br>
     * false - 未成功
     *
     * @return b
     */
    public boolean isSuccess() {
        return !this.isError();
    }

}