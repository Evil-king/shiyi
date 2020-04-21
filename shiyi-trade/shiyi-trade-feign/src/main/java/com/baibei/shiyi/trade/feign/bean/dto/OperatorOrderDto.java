package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.Data;

@Data
public class OperatorOrderDto {
    private String customerNo;

    private String direction;//add subtraction

    private String resource;

    private int count;
}
