package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

@Data
public class ExchangeDetailsDto extends CustomerBaseDto {
    private String orderItemNo;
    private String type;
}
