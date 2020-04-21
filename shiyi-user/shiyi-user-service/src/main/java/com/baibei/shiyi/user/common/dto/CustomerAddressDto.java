package com.baibei.shiyi.user.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/8/12 14:08
 * @description:
 */
@Data
public class CustomerAddressDto extends CustomerNoDto {
    /**
     * 修改时用到
     */
    private Long id;
    /**
     * 详细收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String receivingAddress;
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String receivingName;
    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    private String receivingMobile;
    /**
     * 区
     */
    @NotBlank(message = "区名不能为空")
    private String area;

    /**
     * 市
     */
    @NotBlank(message = "市名不能为空")
    private String city;

    /**
     * 省
     */
    @NotBlank(message = "省名不能为空")
    private String province;
    /**
     * 默认地址
     */
    @NotNull(message = "是否为默认地址不能为空")
    private Boolean defaultAddress;
}
