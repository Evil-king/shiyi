package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.dto.HoldRecordDto;
import com.baibei.shiyi.trade.common.vo.HoldRecordVo;
import com.baibei.shiyi.trade.model.HoldRecord;

import java.util.List;

public interface HoldRecordMapper extends MyMapper<HoldRecord> {

    List<HoldRecordVo> customerHistoryList(HoldRecordDto holdRecordDto);
}