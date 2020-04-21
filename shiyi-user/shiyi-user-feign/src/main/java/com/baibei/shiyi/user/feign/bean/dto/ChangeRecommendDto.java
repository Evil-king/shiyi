package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/11/5 15:38
 * @description:
 */
@Data
public class ChangeRecommendDto extends CustomerNoDto {
    @NotNull(message = "")
    private String newRecommendId;

    private Long operatorId;
}
