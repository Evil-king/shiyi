package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.RecordBean;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.math.BigDecimal;
import java.util.List;

public interface RecordBeanMapper extends MyMapper<RecordBean> {
    List<RecordVo> recordList(RecordBeanDto recordDto);

    List<AdminRecordVo> recordPageList(AdminRecordBeanDto recordDto);

    BigDecimal getDailyIncrement(String customerNo);
}