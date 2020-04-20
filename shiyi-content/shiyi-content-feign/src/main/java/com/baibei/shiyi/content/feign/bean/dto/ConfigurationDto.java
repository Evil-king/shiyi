package com.baibei.shiyi.content.feign.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConfigurationDto {
   private List<WithdrawConfigurationDto> withdrawConfigurationDto;
   private List<SettlementConfigurationDto> settlementConfigurationDto;
}
