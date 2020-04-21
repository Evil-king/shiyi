package com.baibei.shiyi.pingan.feign.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 出入金流水对账以及会员开销户流水匹配
 */
@Data
public class ReconciliationMatchingDto {

    /**
     * 功能标识 1:出入金流水对账 2:会员开销流水
     */
    @Size(max = 1, min = 1)
    @NotNull
    private String funcFlag;

    /**
     * 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginDateTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDateTime;

    /**
     * 保留域
     */
    private String reserve;

}
