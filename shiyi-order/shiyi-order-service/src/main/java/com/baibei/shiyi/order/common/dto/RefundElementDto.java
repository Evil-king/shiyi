package com.baibei.shiyi.order.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import lombok.Data;

@Data
public class RefundElementDto extends CustomerBaseDto {
    private String orderItemNo;
}
