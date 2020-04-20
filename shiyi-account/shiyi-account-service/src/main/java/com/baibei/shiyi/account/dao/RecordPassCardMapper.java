package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.model.RecordPassCard;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface RecordPassCardMapper extends MyMapper<RecordPassCard> {
    List<RecordVo> getList(RecordDto recordDto);
}