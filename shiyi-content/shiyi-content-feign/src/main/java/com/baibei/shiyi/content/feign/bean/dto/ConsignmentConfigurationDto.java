package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsignmentConfigurationDto {
    private BigDecimal consignmentFee;
    private BigDecimal consignmentFees;
    private String startTime;
    private String endTime;
}
