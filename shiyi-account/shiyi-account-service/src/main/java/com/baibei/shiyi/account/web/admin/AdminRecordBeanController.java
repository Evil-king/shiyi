package com.baibei.shiyi.account.web.admin;

import com.baibei.shiyi.account.feign.base.admin.IAdminCustomerBeanBase;
import com.baibei.shiyi.account.feign.base.admin.IAdminRecordBeanBase;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.account.service.IRecordBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 16:14
 * @description:
 */
@RestController
public class AdminRecordBeanController implements IAdminRecordBeanBase {
    @Autowired
    private IRecordBeanService recordBeanService;

    @Override
    public ApiResult<MyPageInfo<AdminRecordVo>> recordPageList(@RequestBody  @Validated AdminRecordBeanDto recordDto) {
        return ApiResult.success(recordBeanService.recordPageList(recordDto));
    }

    @Override
    public ApiResult<List<AdminRecordVo>> export(@RequestBody AdminRecordBeanDto recordDto) {
        return ApiResult.success(recordBeanService.exportRecordList(recordDto));
    }
}
