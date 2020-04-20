package com.baibei.shiyi.cash.feign.base.vo;

import lombok.Data;

@Data
public class PABDepositVo {

    /**
     * 交易网流水号
     */
    private String thirdLogNo;

    /**
     * 保留域
     */
    private String reserve;
}
