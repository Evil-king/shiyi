package com.baibei.shiyi.pingan.service;

import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;

public interface IFilePlannedSpeedService {

    /**
     * 交易网查看银行清算与对账文件的进度
     *
     * @param filePlannedSpeedDto
     * @return
     */
    FilePlannedSpeedVo filePlannedSpeed(FilePlannedSpeedDto filePlannedSpeedDto);
}
