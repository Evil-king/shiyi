package com.baibei.shiyi.trade.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class OperatorHoldOrderDto {

    private List<OperatorOrderDto> operatorDtoList;
}
