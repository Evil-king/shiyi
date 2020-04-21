package com.baibei.shiyi.user.common.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/31 16:51
 * @description:
 */
@Data
public class CustomerPageListDto extends PageParam {
    private String customerNo;
    private String mobile;
    private String realName;
    private String recommenderId;
    private String startTime;
    private String endTime;
    private String status;
}
