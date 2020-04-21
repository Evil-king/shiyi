package com.baibei.shiyi.pingan.feign.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 平安银行查询银行清算与对账文件的进度
 */
@Data
public class FilePlannedSpeedDto {

    /**
     * 批量任务标识
     * 1：清算 2：余额对账 4：出入金流水 5：开销户流水对账
     */
    @NotNull
    @Pattern(regexp = "1|2|4|5")
    private String funcFlag;

    /**
     * 起始日期
     */
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date beginDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyyMMdd", timezone = "GMT+8")
    private Date endDate;

    /**
     * 保留域
     */
    private String Reserve;

    /**
     * 资金汇总账号
     */
    private String supAcctId;

}
