package com.baibei.shiyi.pingan.feign.client.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "${shiyi-pingan:shiyi-pingan}", path = "/api/pingan")
public interface IApiPABFilePlannedSpeedFeign {

    /**
     * 查看文件进度
     *
     * @param filePlannedSpeedDto
     * @return
     */
    @PostMapping(path = "/filePlannedSpeed")
    ApiResult<FilePlannedSpeedVo> filePlannedSpeed(@Validated @RequestBody FilePlannedSpeedDto filePlannedSpeedDto);
}