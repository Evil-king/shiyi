package com.baibei.shiyi.trade.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.model.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends MyMapper<Product> {

    Product findEffective(@Param("productTradeNo") String productTradeNo);

    List<TradeProductListVo> listPage(TradeProductDto tradeProductDto);

    List<Product> modifyStatus();

    List<Product> findEffectiveList();

}