package com.baibei.shiyi.user.common.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/12/3 18:37
 * @description:
 */
@Data
public class OpenAccountDto extends CustomerNoDto {
    @NotBlank(message = "请输入正确的邮箱地址")
    private String email;
    /**
     * 区
     */
    @NotBlank(message = "请输入详细地址")
    private String area;

    /**
     * 市
     */
    @NotBlank(message = "请输入详细地址")
    private String city;

    /**
     * 省
     */
    @NotBlank(message = "请输入详细地址")
    private String province;
    @NotBlank(message = "请输入详细地址")
    private String address;
    @NotBlank(message = "姓名不能为空")
    private String realname;
    @NotBlank(message = "身份证号不能为空")
    private String idcardNo;
    @NotBlank(message = "银行卡号不能为空")
    private String bankCardNo;
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    private String bankNode;
    /**
     * 支行名
     */
    @NotBlank(message = "请选择开户支行")
    private String branchName;
    private String bankTotalNode;
    /**
     * 银行总行名称
     */
    @NotBlank(message = "银行名称不能为空")
    private String bankName;
}
