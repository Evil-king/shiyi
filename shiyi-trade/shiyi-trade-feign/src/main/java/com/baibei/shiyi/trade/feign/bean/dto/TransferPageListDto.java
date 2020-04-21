package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransferPageListDto extends PageParam {
    private long transferLogId;

    private String serialNumber;

    private String inCustomerNo;

    private String outCustomerNo;

    private String status;//success fail

    private String type;//wait,execute
}
