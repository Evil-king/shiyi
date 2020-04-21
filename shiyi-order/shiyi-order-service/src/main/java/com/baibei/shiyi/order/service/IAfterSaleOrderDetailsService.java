package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.order.model.AfterSaleOrderDetails;

import java.math.BigDecimal;


/**
 * @author: wenqing
 * @date: 2019/10/15 10:16:48
 * @description: AfterSaleOrderDetails服务接口
 */
public interface IAfterSaleOrderDetailsService extends Service<AfterSaleOrderDetails> {

    AfterSaleOrderDetails createAfterSaleOrderDetails(String orderItemNo, String serverNo);

    int insertData(AfterSaleOrderDetails afterSaleOrderDetails);

    int updateAfterSaleOrderDetails(AfterSaleOrderDetails afterSaleOrderDetails);

    AfterSaleOrderDetails selectByOrderItemNo(String orderItemNo);

    int updateAfterSaleOrderDetails(String serverNo, BigDecimal confirmAmount, String sendBackAddress, String remark);

    AfterSaleOrderDetails selectByServerNo(String serverNo);

    int updateByServerNoAndLogistics(String serverNo, String logisticsName, String logisticsNo);

    int updateByOrderItemNoAndSendLogistics(String orderItemNo, String sendLogisticsNo, String sendLogisticsName);
}
