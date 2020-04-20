package com.baibei.shiyi.account.feign.base.admin;

import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 19:35
 * @description:
 */
public interface IAdminRecordBeanBase {
    /**
     * 积分流水（三种类型）
     */
    @RequestMapping("/shiyi/account/recordBean/pageList")
    @ResponseBody
    ApiResult<MyPageInfo<AdminRecordVo>> recordPageList(@RequestBody AdminRecordBeanDto recordDto);

    /**
     * 积分流水（三种类型）导出
     */
    @RequestMapping("/shiyi/account/recordBean/export")
    @ResponseBody
    ApiResult<List<AdminRecordVo>> export(@RequestBody AdminRecordBeanDto recordDto);
}
