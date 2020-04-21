package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @Classname RevokeDto
 * @Description 撤单
 * @Date 2019/6/6 14:18
 * @Created by Longer
 */
@Data
public class RevokeDto extends CustomerBaseDto {

    private String productTradeNo;
}
