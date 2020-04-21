package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideVo;
import com.baibei.shiyi.trade.model.ProductPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductPicMapper extends MyMapper<ProductPic> {

    List<TradeProductSlideVo> selectPic(@Param("type") String type,@Param("productTradeNo") String productTradeNo);

    int deleteByTradeProductNo(@Param("productTradeNo") String productTradeNo);
}