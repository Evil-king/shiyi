package com.baibei.shiyi.cash.feign.base.dto;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DepositWithDrawDto extends CustomerBaseAndPageDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}
