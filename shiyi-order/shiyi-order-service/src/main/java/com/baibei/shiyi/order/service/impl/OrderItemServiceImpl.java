package com.baibei.shiyi.order.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.MallOrderStatusEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.NumberUtil;
import com.baibei.shiyi.common.tool.utils.ShelfTypeUtil;
import com.baibei.shiyi.common.tool.utils.SkuPropertyUtil;
import com.baibei.shiyi.order.common.dto.MyOrderDto;
import com.baibei.shiyi.order.common.vo.KeyValue;
import com.baibei.shiyi.order.common.vo.MyOrderVo;
import com.baibei.shiyi.order.common.vo.ProductNameImgVo;
import com.baibei.shiyi.order.dao.OrderItemMapper;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.*;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderItem服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderItemServiceImpl extends AbstractService<OrderItem> implements IOrderItemService {

    @Autowired
    private OrderItemMapper tblOrdOrderItemMapper;
    @Autowired
    private IOrderSettingService orderSettingService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderProductService orderProductService;
    @Autowired
    private IOrderRefundService orderRefundService;

    @Override
    public MyPageInfo<MyOrderVo> myOrderList(MyOrderDto myOrderDto) {
        if (StringUtils.isEmpty(myOrderDto.getCustomerNo())) {
            throw new IllegalArgumentException("customerNo can not be null");
        }
        Condition condition = new Condition(OrderItem.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (!StringUtils.isEmpty(myOrderDto.getStatus())) {
            criteria.andEqualTo("status", myOrderDto.getStatus());
        }
        criteria.andEqualTo("customerNo", myOrderDto.getCustomerNo());
        MyPageInfo<OrderItem> myPageInfo = pageList(condition, PageParam.buildWithDefaultSort(myOrderDto.getCurrentPage(), myOrderDto.getPageSize()));
        if (CollectionUtils.isEmpty(myPageInfo.getList())) {
            return PageUtil.getEmptyPageInfo();
        }
        List<MyOrderVo> result = new LinkedList<>();
        List<OrderItem> list = myPageInfo.getList();
        for (OrderItem item : list) {
            List<KeyValue> payList = new ArrayList<>();
            MyOrderVo vo = new MyOrderVo();
            vo.setOrderNo(item.getOrderItemNo());
            vo.setStatus(item.getStatus());
            vo.setStatusText(MallOrderStatusEnum.getDesc(item.getStatus()));
            vo.setCreateTime(item.getCreateTime());
            vo.setProductCount(1);
            List<ProductNameImgVo> productList = new ArrayList<>();
            ProductNameImgVo productNameImgVo = new ProductNameImgVo();
            productNameImgVo.setProductName(item.getProductName());
            productNameImgVo.setProductImg(item.getProductImg());
            productNameImgVo.setQuantity(item.getQuantity());
            productNameImgVo.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(item.getSkuProperty()));
            productList.add(productNameImgVo);
            vo.setProductList(productList);
            KeyValue kv = new KeyValue();
            kv.setKey(ShelfTypeUtil.getDesc(item.getShelfType()));
            kv.setValue(NumberUtil.roundFloor(item.getActualAmount(), 2).toPlainString());
            payList.add(kv);
            vo.setPayList(payList);
            result.add(vo);
        }
        return PageUtil.transform(myPageInfo, result);
    }

    @Override
    public OrderItem findByOrderItemNo(String orderItemNo) {
        Condition condition = new Condition(OrderItem.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orderItemNo", orderItemNo);
        List<OrderItem> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        Condition condition = new Condition(OrderItem.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orderId", orderId);
        List<OrderItem> list = findByCondition(condition);
        return list;
    }

    @Override
    public List<AdminOrderDetailsVo> findByAdminOrderId(Long orderId) {
        List<OrderItem> orderItems = this.findByOrderId(orderId);
        List<AdminOrderDetailsVo> orderDetailsVoList = BeanUtil.copyProperties(orderItems, AdminOrderDetailsVo.class);
        return orderDetailsVoList;
    }

    @Override
    public List<AdminOrderDetailsVo> findByOrderItemId(Long orderItemId) {
        List<OrderItem> orderItems = this.findByIds(String.valueOf(orderItemId));
        List<AdminOrderDetailsVo> orderDetailsVoList = BeanUtil.copyProperties(orderItems, AdminOrderDetailsVo.class);
        return orderDetailsVoList;
    }

    @Override
    public ApiResult autoDelivery() {
        List<OrderItem> orderItemList = tblOrdOrderItemMapper.findConfirmOverTimeOrder(new HashMap<>());
        for (OrderItem orderItem : orderItemList) {
            orderItem.setCompletedTime(new Date());
            orderItem.setStatus(MallOrderStatusEnum.COMPLETED.getCode());
            orderItem.setModifyTime(new Date());
            update(orderItem);
        }
        return ApiResult.success();
    }

    @Override
    public List<AdminOrderItemVo> orderAndOrderItemPageList(AdminOrderDto orderDto) {
        return tblOrdOrderItemMapper.orderAndOrderItemPageList(orderDto);
    }

    @Override
    public AdminOrderVo getByIdToOrderItem(long orderItemId) {
        //获取商品信息
        OrderItem orderItem = this.findById(orderItemId);
        if (StringUtils.isEmpty(orderItemId)) {
            log.info("获取商品信息orderNo[{}]为null", orderItemId);
            throw new ServiceException(String.format("子订单%s不存在", orderItemId));
        }
        Order order = orderService.findById(orderItem.getOrderId());
        AdminOrderVo orderVo = BeanUtil.copyProperties(orderItem, AdminOrderVo.class);
        orderVo.setOrderItemStatus(orderItem.getStatus());
        orderVo.setRemark(order.getRemark());
        //子订单详情
        orderVo.setOrderDetailsVoList(findByOrderItemId(orderItemId));
        //退款信息
        orderVo.setOrderRefundVoList(orderRefundService.findByOrderId(orderItem.getOrderId()));
        //收货人信息
        orderVo.setReceiverName(order.getReceiverName());
        orderVo.setReceiverPhone(order.getReceiverPhone());
        orderVo.setReceiverDetailAddress(order.getReceiverProvince()+order.getReceiverCity()+order.getReceiverRegion()+order.getReceiverDetailAddress());
        //子订单基本信息
        orderVo.setOrderItemNo(orderItem.getOrderItemNo());
        orderVo.setCreateTime(orderItem.getCreateTime());
        orderVo.setPayTime(orderItem.getCreateTime());
        orderVo.setCustomerNo(orderItem.getCustomerNo());
        if("money".equals(orderItem.getPayWay())){
            orderVo.setMoneyTotalAmount(orderItem.getActualAmount());
            if(!StringUtils.isEmpty(orderItem.getSendIntegralJson())){
                JSONArray jsonArray =JSONArray.parseArray(orderItem.getSendIntegralJson());
                for(int i=0;i<jsonArray.size();i++){
                    JSONObject  jsonObject = jsonArray.getJSONObject(i);
                    if("shiyi".equals(jsonObject.getString("key"))){
                        orderVo.setShiyiIntegration(jsonObject.getString("value"));
                    }
                    if("consumption".equals(jsonObject.getString("key"))){
                        orderVo.setConsumptionIntegration(jsonObject.getString("value"));
                    }
                    if("exchange".equals(jsonObject.getString("key"))){
                        orderVo.setExchangeIntegration(jsonObject.getString("value"));
                    }
                }
            }
        }
        if("consumption".equals(orderItem.getPayWay())){
            orderVo.setConsumptionTotalAmount(orderItem.getActualAmount());
        }
        //子订单商品
        List<AdminOrderProductVo> adminOrderProductVoList = orderProductService.findAdminByOrderId(orderItem.getOrderId());
        orderVo.setOrderProductVoList(adminOrderProductVoList);
        return orderVo;
    }

    @Override
    public AdminOrderSummaryVo orderItemCount(AdminOrderDto orderDto) {
        AdminOrderSummaryVo adminOrderSummaryVo = new AdminOrderSummaryVo();
        Condition condition = new Condition(OrderItem.class);
        condition.createCriteria().andEqualTo("flag", new Byte(Constants.Flag.VALID));
        List<OrderItem> ordersCount = this.findByCondition(condition);
        adminOrderSummaryVo.setOrderCount(ordersCount.size());
        adminOrderSummaryVo.setOrderCompleted(getOrderItemStatusCount(Constants.MallOrderStatus.COMPLETED, orderDto));
        adminOrderSummaryVo.setOrderDeliveryed(getOrderItemStatusCount(Constants.MallOrderStatus.DELIVERYED, orderDto));
        adminOrderSummaryVo.setOrderUndelivery(getOrderItemStatusCount(Constants.MallOrderStatus.UNDELIVERY, orderDto));
        return adminOrderSummaryVo;
    }


    @Override
    public Integer getOrderItemStatusCount(String status, AdminOrderDto orderDto) {
        Condition condition = new Condition(OrderItem.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status", status);
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        if (!StringUtils.isEmpty(orderDto.getOrderNo())) {
            criteria.andEqualTo("orderNo", orderDto.getOrderNo());
        }
        if (!StringUtils.isEmpty(orderDto.getCustomerNo())) {
            criteria.andEqualTo("customerNo", orderDto.getCustomerNo());
        }
        if (!StringUtils.isEmpty(orderDto.getStatus())) {
            criteria.andEqualTo("status", orderDto.getStatus());
        }
        if (!StringUtils.isEmpty(orderDto.getPayWay())) {
            criteria.andEqualTo("payWay", orderDto.getPayWay());
        }
        if (!StringUtils.isEmpty(orderDto.getOrderItemNo())) {
            criteria.andEqualTo("orderItemNo", orderDto.getOrderItemNo());
        }
        return this.tblOrdOrderItemMapper.selectCountByCondition(condition);
    }
}
