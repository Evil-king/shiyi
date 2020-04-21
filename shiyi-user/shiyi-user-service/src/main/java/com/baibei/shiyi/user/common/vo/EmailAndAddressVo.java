package com.baibei.shiyi.user.common.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/12/4 10:59
 * @description:
 */
@Data
public class EmailAndAddressVo {
    private String email;
    /**
     * 区
     */
    private String area;

    /**
     * 市
     */
    private String city;

    /**
     * 省
     */
    private String province;
    private String address;
    /**
     * 银行名
     */
    private String bankName;
    /**
     * 支行名
     */
    private String branchName;

}
