package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.service.IRecordPassCardService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/11 15:25
 * @description:
 */
@RestController
@RequestMapping("/auth/api/account/recordPassCard")
public class AuthApiRecordPassCardController {
    @Autowired
    private IRecordPassCardService recordPassCardService;

    @RequestMapping("/getList")
    public ApiResult<MyPageInfo<RecordVo>> getList(@RequestBody @Validated RecordDto recordDto){
        return ApiResult.success(recordPassCardService.getList(recordDto));
    }

}
