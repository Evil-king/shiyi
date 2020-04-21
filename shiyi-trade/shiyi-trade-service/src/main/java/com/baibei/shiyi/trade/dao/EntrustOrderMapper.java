package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.dto.PcEntrustListAndCustomerDto;
import com.baibei.shiyi.trade.common.vo.*;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.model.EntrustOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EntrustOrderMapper extends MyMapper<EntrustOrder> {
    List<EntrustOrderListVo> entrustOrderList(Map<String, Object> param);

    List<MyEntrustOrderVo> myEntrustList(String customerNo);

    List<MyEntrustOrderVo> myPcEntrustList(@Param("customerNo") String customerNo,
                                           @Param("productTradeNo") String productTradeNo,
                                           @Param("direction") String direction);

    MyEntrustDetailsVo myEntrustDetails(@Param("entrustId") long entrustId, @Param("customerNo") String customerNo);

    List<RevokeListVo> revokeList(String customerNo);

    List<CustomerEntrustOrderListVo> mypageList(CustomerEntrustOrderListDto customerEntrustOrderListDto);

    List<MyEntrustOrderVo> myPcEntrustListAndCustomerNo(PcEntrustListAndCustomerDto pcEntrustListAndCustomerDto);

}