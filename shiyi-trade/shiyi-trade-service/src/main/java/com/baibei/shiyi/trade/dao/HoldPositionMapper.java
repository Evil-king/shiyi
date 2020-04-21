package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.model.HoldPosition;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface HoldPositionMapper extends MyMapper<HoldPosition> {

    TotalMarketValueVo getTotalMarketValue(@Param("customerNo") String customerNo,
                                           @Param("lastPrice") BigDecimal lastPrice,
                                           @Param("productTradeNo") String productTradeNo);

    HoldDetailListVo getPageList(@Param("customerNo") String customerNo,
                                       @Param("lastPrice") BigDecimal lastPrice,
                                        @Param("productTradeNo") String productTradeNo);

    List<HoldPositionVo> findPageList(HoldPositionDto holdPositionDto);


    Integer sumCanSellCount(@Param("productTradeNo") String productTradeNo);

}