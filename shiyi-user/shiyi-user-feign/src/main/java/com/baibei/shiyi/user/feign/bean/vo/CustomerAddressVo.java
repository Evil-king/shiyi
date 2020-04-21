package com.baibei.shiyi.user.feign.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/8/5 17:21
 * @description:
 */
@Data
public class CustomerAddressVo {
    private Long id;
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

    /**
     * 收货详细地址
     */
    private String receivingAddress;

    /**
     * 收货人姓名
     */
    private String receivingName;

    /**
     * 收货手机号
     */
    private String receivingMobile;

    /**
     * 是否默认(1:默认，0：非默认)
     */
    private Boolean defaultAddress;

    /**
     * 邮政编码
     */
    private String postCode;
}
