package com.baibei.shiyi.pingan.service;

import com.baibei.shiyi.pingan.feign.base.dto.BeginCleanDto;
import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;

public interface IBeginCleanService {

    /**
     * 调用1003接口发起清算
     *
     * @param dto
     * @return
     */
    void beginClean(BeginCleanDto dto);
}
