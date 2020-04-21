package com.baibei.shiyi.user.feign.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/11/4 15:47
 * @description:
 */
@Data
public class CustomerListDto extends PageParam {
    /**
     * 用户编号
     */
    private String customerNo;
    /**
     * 手机号
     */
    private String  mobile;
    /**
     * 实名信息
     */
    private String realname;
    /**
     * 直接推荐人
     */
    private String recommenderId;
    /**
     * 注册开始时间
     */
    private String startTime;
    /**
     * 注册结束时间
     */
    private String endTime;
    /**
     * 用户状态
     */
    private String customerStatus;
}
