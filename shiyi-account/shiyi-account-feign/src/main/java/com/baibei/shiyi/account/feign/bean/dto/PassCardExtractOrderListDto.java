package com.baibei.shiyi.account.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/12 14:54
 * @description:
 */
@Data
public class PassCardExtractOrderListDto extends PageParam {
    private String orderNo;
    private String customerNo;
    private String startTime;
    private String status;
    private String endTime;
}
