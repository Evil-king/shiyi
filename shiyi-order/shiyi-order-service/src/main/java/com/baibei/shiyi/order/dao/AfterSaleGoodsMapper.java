package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.common.vo.ApiAfterSalePageListVo;
import com.baibei.shiyi.order.model.AfterSaleGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleGoodsMapper extends MyMapper<AfterSaleGoods> {
    int updateTypeByOrderItemNo(@Param("orderItemNo") String orderItemNo, @Param("type") String type);

    List<ApiAfterSalePageListVo> selectByOrderItemNoTypeIsNotNull(String customerNo);

}