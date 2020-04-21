package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.common.vo.AfterSaleDetailsVo;
import com.baibei.shiyi.order.common.vo.ApiAfterSalePageListVo;
import com.baibei.shiyi.order.feign.base.dto.AfterSalePageListDto;
import com.baibei.shiyi.order.feign.base.dto.ConfirmReceiptDto;
import com.baibei.shiyi.order.feign.base.dto.OperatorApplicationDto;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.model.AfterSaleOrder;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;

import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/10/15 10:16:48
 * @description: AfterSaleOrder服务接口
 */
public interface IAfterSaleOrderService extends Service<AfterSaleOrder> {

    /**
     * 列表数据
     *
     * @param afterSalePageListDto
     * @return
     */
    MyPageInfo<AfterSaleOrderVo> pageListData(AfterSalePageListDto afterSalePageListDto);

    /**
     * 同步售后订单(init状态)
     *
     * @param byOrderItemNo
     * @param byOrderNo
     */
    ApiResult createOrder(OrderItem byOrderItemNo, Order byOrderNo);

    /**
     * Api端申请退款/申请换货(将init改为waiting)
     *
     * @param submitApplicationDto
     * @return
     */
    String submitApplication(SubmitApplicationDto submitApplicationDto);

    /**
     * api取消申请(将waiting改为revoked)
     *
     * @param apiCancelApplicationDto
     * @return
     */
    ApiResult cancelApplication(ApiCancelApplicationDto apiCancelApplicationDto);

    /**
     * 售后订单申请同意/驳回操作(将状态waiting改为success或者fail)
     *
     * @param operatorApplicationDto
     * @return
     */
    ApiResult operatorApplication(OperatorApplicationDto operatorApplicationDto);


    /**
     * api寄货(将success状态改为doing)
     * @param shippingDto
     * @return
     */
    ApiResult shipping(ShippingDto shippingDto);


    /**
     * 确认收货(将状态doing改为refunded或reissued)
     *
     * @param confirmReceiptDto
     * @return
     */
    ApiResult confirmReceipt(ConfirmReceiptDto confirmReceiptDto);


    /**
     * 导出列表数据
     *
     * @param afterSalePageListDto
     * @return
     */
    ApiResult<List<AfterSaleOrderVo>> exportData(AfterSalePageListDto afterSalePageListDto);

    /**
     * 售后订单详情
     *
     * @param orderItemNo
     * @return
     */
    AfterSaleDetailsVo details(String orderItemNo);


    /**
     * api售后列表
     *
     * @param apiAfterSalePageListDto
     */
    MyPageInfo<ApiAfterSalePageListVo> apiPageList(ApiAfterSalePageListDto apiAfterSalePageListDto);

    boolean whetherToOpenAfterSaleOrder(String orderItemNo, String customerNo);

    /**
     * api换货/api退款-详情
     *
     * @param apiAfterSaleDetailsDto
     * @return
     */
    ApiResult apiAfterSaleDetails(ApiAfterSaleDetailsDto apiAfterSaleDetailsDto);

    /**
     * 查询用户售后订单状态
     * @param orderItemNo
     * @param customerNo
     * @return
     */
    String selectByOrderItemNoToStatus(String orderItemNo,String customerNo);

}
