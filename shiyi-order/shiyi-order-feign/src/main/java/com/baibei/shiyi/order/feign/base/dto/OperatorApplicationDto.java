package com.baibei.shiyi.order.feign.base.dto;

import lombok.Data;

@Data
public class OperatorApplicationDto {
    private String orderItemNo;
    private String serverNo;
    private String status;//success fail
    private String confirmAmount;
    private String sendBackAddress;
    private String remark;
}
