package com.baibei.shiyi.admin.modules.system.bean.dto;

import com.baibei.shiyi.common.tool.page.PageParam;
import lombok.Data;

import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/10/15 15:49
 * @description:
 */
@Data
public class UserPageListDto  extends PageParam {
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 账号类型
     */
    private String orgType;
    /**
     * 账号归属,此时入参为id（前期与前端未统一导致）
     */
    private String orgName;
    /**
     * 状态（enable:启用,disable禁用）
     */
    private String userStatus;
    /**
     * 创建开始时间
     */
    private String startTime;
    /**
     * 创建结束时间
     */
    private String endTime;
}
