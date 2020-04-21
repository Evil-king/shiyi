package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.feign.base.dto.AfterSalePageListDto;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.model.AfterSaleOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleOrderMapper extends MyMapper<AfterSaleOrder> {

    List<AfterSaleOrderVo> pageList(AfterSalePageListDto afterSalePageListDto);

    int updateByParamsRefunded(@Param("serverNo") String serverNo,@Param("status") String status);

    int updateByParamsReissued(@Param("serverNo") String serverNo,@Param("status") String status);

    AfterSaleOrder  selectByOrderItemNoToWaiting(String orderItemNo);

    int whetherToOpenAfterSaleOrder(@Param("orderItemNo") String orderItemNo,@Param("customerNo") String customerNo);

    String selectByOrderItemNoToStatus(@Param("orderItemNo") String orderItemNo, @Param("customerNo") String customerNo);

    AfterSaleOrder selectByOrderItemNoToStatusObject(String orderItemNo);

    List<AfterSaleOrderVo> exportData(AfterSalePageListDto afterSalePageListDto);

}