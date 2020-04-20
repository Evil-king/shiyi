package com.baibei.shiyi.common.tool.api;


import java.io.Serializable;

/**
 * 功能说明: 请求基类<br>
 * 注意事项: <br>
 * 系统版本: v1.0<br>
 * 开发人员: <br>
 * 开发时间: <br>
 */
@SuppressWarnings("serial")
public class BaseReq implements Serializable {

    private String menuId;
    private String functionId;
    private String exchangeId;
    private String bankProCode;
    private String operatorNo;
    private String gtid;
    private String version;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getFunctionId() {
        return functionId;
    }

    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getBankProCode() {
        return bankProCode;
    }

    public void setBankProCode(String bankProCode) {
        this.bankProCode = bankProCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGtid() {
        return gtid;
    }

    public void setGtid(String gtid) {
        this.gtid = gtid;
    }

}
