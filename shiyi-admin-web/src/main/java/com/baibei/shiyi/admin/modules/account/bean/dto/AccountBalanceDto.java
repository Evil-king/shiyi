package com.baibei.shiyi.admin.modules.account.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
public class AccountBalanceDto extends PageParam {

    private Long id;

    private String phone;

    private Date startTime;

    private Date endTime;

    private String balanceType;

    private String status;

    private String customerNo;

    private BigDecimal balance;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 批量执行Id
     */
    private Set<String> ids;

}
