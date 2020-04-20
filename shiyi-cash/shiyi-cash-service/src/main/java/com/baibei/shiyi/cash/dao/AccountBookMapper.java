package com.baibei.shiyi.cash.dao;

import com.baibei.shiyi.cash.feign.base.dto.Apply1010PagelistDto;
import com.baibei.shiyi.cash.feign.base.vo.Apply1010PagelistVo;
import com.baibei.shiyi.cash.model.AccountBook;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface AccountBookMapper extends MyMapper<AccountBook> {
    void clear();

    List<Apply1010PagelistVo> pageList(Apply1010PagelistDto apply1010PagelistDto);

}