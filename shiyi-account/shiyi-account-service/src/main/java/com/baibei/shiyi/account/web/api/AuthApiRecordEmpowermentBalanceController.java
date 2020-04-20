package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.service.IRecordEmpowermentBalanceService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/11 15:31
 * @description:
 */

@RestController
@RequestMapping("/auth/api/account/recordEmpowermentBalance")
public class AuthApiRecordEmpowermentBalanceController {
    @Autowired
    private IRecordEmpowermentBalanceService recordEmpowermentBalanceService;

    @RequestMapping("/recordList")
    public ApiResult<MyPageInfo<RecordVo>> recordList(@Validated @RequestBody RecordBeanDto recordDto){
        return ApiResult.success(recordEmpowermentBalanceService.recordList(recordDto));
    }
}
