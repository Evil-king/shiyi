package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.dto.DealOrderQueryDTO;
import com.baibei.shiyi.trade.common.dto.MyDealOorderListDto;
import com.baibei.shiyi.trade.common.vo.DealOrderHistoryVo;
import com.baibei.shiyi.trade.common.vo.MyDealOrderListVo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.model.DealOrder;

import java.util.List;

public interface DealOrderMapper extends MyMapper<DealOrder> {

    List<MyDealOrderListVo> dealOrderList(MyDealOorderListDto myDealOorderListDto);

    List<DealOrderVo> myPageList(DealOrderDto dealOrderDto);

    List<DealOrderHistoryVo> customerHistoryList(DealOrderQueryDTO dealOrderQueryDTO);

}