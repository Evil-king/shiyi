package com.baibei.shiyi.cash.feign.base.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

@Data
public class BankOrderDto extends PageParam {
    /**
     * 批次
     */
    private String batchNo;

    /**
     * 银行前置流水号
     */
    private String bankSerialNo;

    /**
     * 类型（1：出金，2：入金）
     */
    private String type;

    /**
     * 交易网会员代码
     */
    private String memberNo;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 导出条数限制
     */
    private Integer exportLimit;
}
