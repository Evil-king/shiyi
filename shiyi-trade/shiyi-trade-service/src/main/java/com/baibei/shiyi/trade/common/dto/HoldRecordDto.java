package com.baibei.shiyi.trade.common.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import lombok.Data;

@Data
public class HoldRecordDto extends CustomerBaseAndPageDto {
    private String startTime;
    private String endTime;
}
