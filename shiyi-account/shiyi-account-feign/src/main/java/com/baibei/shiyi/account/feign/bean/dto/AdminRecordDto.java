package com.baibei.shiyi.account.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/1 17:13
 * @description:
 */
@Data
public class AdminRecordDto extends PageParam {
    /**
     * 流水号
     */
    private String recordNo;
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 收支类型
     */
    private String retype;
    /**
     * 收支项目
     */
    private String tradeType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 关联订单号
     */
    private String orderNo;
}
