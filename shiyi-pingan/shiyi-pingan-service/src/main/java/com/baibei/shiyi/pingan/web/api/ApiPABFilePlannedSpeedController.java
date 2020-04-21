package com.baibei.shiyi.pingan.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;
import com.baibei.shiyi.pingan.feign.client.api.IApiPABFilePlannedSpeedFeign;
import com.baibei.shiyi.pingan.service.IFilePlannedSpeedService;
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
public class ApiPABFilePlannedSpeedController implements IApiPABFilePlannedSpeedFeign {

    @Autowired
    private IFilePlannedSpeedService filePlannedSpeedService;

    @Override
    public ApiResult<FilePlannedSpeedVo> filePlannedSpeed(@Validated @RequestBody FilePlannedSpeedDto filePlannedSpeedDto) {
        FilePlannedSpeedVo filePlannedSpeedVo = filePlannedSpeedService.filePlannedSpeed(filePlannedSpeedDto);
        return ApiResult.success(filePlannedSpeedVo);
    }
}
