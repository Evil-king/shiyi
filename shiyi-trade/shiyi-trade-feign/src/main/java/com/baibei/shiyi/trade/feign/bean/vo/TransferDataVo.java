package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;

@Data
public class TransferDataVo {

    private int originCount;

    private int successCount;

    private int failCount;
}
