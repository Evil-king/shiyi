package com.baibei.shiyi.order.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.order.model.AfterSaleOrderDetails;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleOrderDetailsMapper extends MyMapper<AfterSaleOrderDetails> {

    AfterSaleOrderDetails selectByParams(String serverNo);

    int updateByOrderItemNoAndSendLogistics(@Param("orderItemNo") String orderItemNo,@Param("sendLogisticsNo") String sendLogisticsNo,
                                            @Param("sendLogisticsName")    String sendLogisticsName);
}