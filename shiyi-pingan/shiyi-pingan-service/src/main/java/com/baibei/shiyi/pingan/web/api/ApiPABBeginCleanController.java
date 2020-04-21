package com.baibei.shiyi.pingan.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.BeginCleanDto;
import com.baibei.shiyi.pingan.feign.client.api.IApiPABBeginCleanFeign;
import com.baibei.shiyi.pingan.service.IBeginCleanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查看清结算文件的进度
 */
@RestController
@RequestMapping("/api/pingan")
public class ApiPABBeginCleanController implements IApiPABBeginCleanFeign {
    @Autowired
    private IBeginCleanService beginCleanService;

    @Override
    public ApiResult beginClean(@Validated @RequestBody BeginCleanDto beginCleanDto) {
        beginCleanService.beginClean(beginCleanDto);
        return ApiResult.success();
    }
}
