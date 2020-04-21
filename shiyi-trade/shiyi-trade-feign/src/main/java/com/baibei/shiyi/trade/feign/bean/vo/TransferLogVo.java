package com.baibei.shiyi.trade.feign.bean.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransferLogVo {

    private long id;

    private String name;

    private String createTime;

    private String creator;

    private String countNum;

    private String countAmount;

    private String status;

    private String modifier;

    private String isFee;//是否收取手续费 1=收 0=不收

    private String modifyTime;//执行时间
}
