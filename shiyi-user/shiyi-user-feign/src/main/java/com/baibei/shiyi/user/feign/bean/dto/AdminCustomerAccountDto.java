package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/1 14:05
 * @description:
 */
@Data
public class AdminCustomerAccountDto extends PageParam {
    private String customerNo;
    private String mobile;
    private String recommenderId;
}
