package com.baibei.shiyi.account.feign.bean.vo;

import lombok.Data;

@Data
public class PABSigningRecordVo {

    /**
     * 交易网流水号
     */
    private String thirdLogNo;

    /**
     * 保留域
     */
    private String reserve;
}
