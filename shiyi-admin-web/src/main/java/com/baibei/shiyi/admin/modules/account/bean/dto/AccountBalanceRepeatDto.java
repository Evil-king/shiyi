package com.baibei.shiyi.admin.modules.account.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import java.util.Date;

@Data
public class AccountBalanceRepeatDto extends PageParam {

    private String batchNo;

    private Date startTime;

    private Date endTime;
}
