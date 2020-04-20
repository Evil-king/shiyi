package com.baibei.shiyi.account.web.admin;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.feign.base.admin.IAdminRecordMoneyBase;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.service.IRecordMoneyService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/1 18:02
 * @description:
 */
@RestController
public class AdminRecordMoneyController implements IAdminRecordMoneyBase {
    @Autowired
    private IRecordMoneyService recordMoneyService;

    @Override
    public ApiResult<MyPageInfo<AdminRecordVo>> pageList(@RequestBody AdminRecordDto recordDto) {
            return ApiResult.success(recordMoneyService.AdminRecordList(recordDto));
    }

    @Override
    public ApiResult<List<AdminRecordVo>> list(@RequestBody  AdminRecordDto recordDto) {
        return ApiResult.success(recordMoneyService.list(recordDto));
    }
}
