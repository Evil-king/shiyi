package com.baibei.shiyi.order.common.dto;

import lombok.Data;

/**
 * 确认收货入参
 */
@Data
public class ConfirmReceiptDto {
    private String serverNo;
    private String type;
    private String customerNo;
    private String logisticsName;
    private String logisticsNo;
}
