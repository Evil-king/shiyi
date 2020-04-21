package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

@Data
public class RefundDetailsDto extends CustomerBaseDto {
    private String orderItemNo;
    private String type;
}
