package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.model.TransferLogRecord;

import java.util.List;

public interface TransferLogRecordMapper extends MyMapper<TransferLogRecord> {
    List<TransferPageListVo> listPage(TransferPageListDto transferPageListDto);

}