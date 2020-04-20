package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.RecordMoney;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface RecordMoneyMapper extends MyMapper<RecordMoney> {
    BigDecimal findTotalChangeAmountByTradeType(@Param("customerNo") String customerNo, @Param("code") String code);

    List<RecordVo> recordList(RecordDto recordDto);

    List<AdminRecordVo> AdminRecordList(AdminRecordDto recordDto);

    BigDecimal findTotalByRetype(@Param("customerNo")String customerNo,@Param("retype") String in);
}