package com.baibei.shiyi.pingan.feign.base.vo;

import lombok.Data;

/**
 * 平安交易网查询银行清算与对账文件的进度返回参数
 */
@Data
public class FilePlannedSpeedVo {

    /**
     * 批量次数
     */
    private Integer recordCount;

    /**
     * 处理结果标识
     */
    private String resultFlag;

}
