package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.service.IRecordMoneyService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/10/30 10:54
 * @description:
 */
@RestController
@Slf4j
@RequestMapping("/auth/api/account/recordMoney")
public class AuthApiRecordMoneyController {
    @Autowired
    private IRecordMoneyService recordMoneyService;

    @PostMapping("/recordList")
    public ApiResult<MyPageInfo<RecordVo>> recordList(@Validated @RequestBody RecordDto recordDto){
        return ApiResult.success(recordMoneyService.recordList(recordDto));
    }
}
