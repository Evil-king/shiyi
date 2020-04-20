package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.model.SigningRecord;
import com.baibei.shiyi.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SigningRecordMapper extends MyMapper<SigningRecord> {

    /**
     * 查询当天用户是否签约
     *
     * @return
     */
    List<SigningRecord> findTodaySigning(@Param("customerNo") String customerNo);
}