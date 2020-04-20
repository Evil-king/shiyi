package com.baibei.shiyi.account.common.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/8/1 13:51
 * @description:
 */
@Data
public class RecordVo {
    /**
     * 交易类型
     */
    private String tradeType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 变动数额
     */
    private BigDecimal changeAmount;

    private String recordNo;
}
