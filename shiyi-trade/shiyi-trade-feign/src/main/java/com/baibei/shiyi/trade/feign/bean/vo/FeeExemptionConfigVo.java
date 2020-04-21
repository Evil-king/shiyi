package com.baibei.shiyi.trade.feign.bean.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 手续费豁免名单
 */
@Data
public class FeeExemptionConfigVo {

    private Long id;

    private String customerNo;

    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
