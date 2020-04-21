package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.model.TransferLog;

import java.util.List;

public interface TransferLogMapper extends MyMapper<TransferLog> {

    List<TransferLogVo> pageListLog(TransferLogDto transferLogDto);
}