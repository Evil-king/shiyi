package com.baibei.shiyi.cash.feign.base.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 签到或者签退
 */
@Data
public class SignInBackDto {

    /**
     * 请求功能标识 1、签到 2、签退
     */
    @NotNull
    @Size(min = 1, max = 1)
    @Pattern(regexp = "1|2")
    private String funcFlag;

    /**
     * 保留域
     */
    @Size(max = 120)
    private String Reserve;

    /**
     * 签约流水号
     */
    private String signNo;

    /**
     * 签约时间
     */
    private Date signDate;

    /**
     * 外部流水号
     */
    private String externalNo;

}
