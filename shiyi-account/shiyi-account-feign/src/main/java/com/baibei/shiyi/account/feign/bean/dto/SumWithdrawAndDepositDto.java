package com.baibei.shiyi.account.feign.bean.dto;

import lombok.Data;

/**
 * @Classname SumWithdrawAndDepositDto
 * @Description 某个时间段内用户的总出金和总入金dto
 * @Date 2019/11/14 21:17
 * @Created by Longer
 */
@Data
public class SumWithdrawAndDepositDto {
    private String startTime;

    private String endTime;
}
