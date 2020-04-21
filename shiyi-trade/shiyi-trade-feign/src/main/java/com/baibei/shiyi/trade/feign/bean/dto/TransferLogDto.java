package com.baibei.shiyi.trade.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TransferLogDto extends PageParam {

    private String name;

    private String status;

    private String startTime;

    private String endTime;
}
