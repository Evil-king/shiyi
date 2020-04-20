package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.model.RecordEmpowermentBalance;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface RecordEmpowermentBalanceMapper extends MyMapper<RecordEmpowermentBalance> {
    List<RecordVo> recordList(RecordBeanDto recordDto);
}