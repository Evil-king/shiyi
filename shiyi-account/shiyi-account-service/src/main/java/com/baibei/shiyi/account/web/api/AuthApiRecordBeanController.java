package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.service.IRecordBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/10/30 19:15
 * @description:
 */
@RestController
@RequestMapping("/auth/api/account/recordBean")
@Slf4j
public class AuthApiRecordBeanController {
    @Autowired
    private IRecordBeanService recordBeanService;

    @RequestMapping("/recordList")
    public ApiResult<MyPageInfo<RecordVo>> recordList(@Validated @RequestBody RecordBeanDto recordDto){
        return ApiResult.success(recordBeanService.recordList(recordDto));
    }
}
