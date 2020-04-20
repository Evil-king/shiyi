package com.baibei.shiyi.account.dao;

import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.model.PassCardExtractOrder;
import com.baibei.shiyi.common.core.mybatis.MyMapper;

import java.util.List;

public interface PassCardExtractOrderMapper extends MyMapper<PassCardExtractOrder> {
    List<PassCardExtractOrderListVo> getPageList(PassCardExtractOrderListDto passCardExtractOrderListDto);

    List<Long> findNotAuditOrder(Integer amount);

    Integer selectUnfailCount(String customerNo);
}