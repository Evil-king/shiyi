package com.baibei.shiyi.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundType;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.CustomerBeanFeign;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.MallOrderStatusEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.ReviewStatusEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.common.tool.page.PageUtil;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.order.common.dto.MyOrderDetailsDto;
import com.baibei.shiyi.order.common.dto.MyOrderDto;
import com.baibei.shiyi.order.common.vo.*;
import com.baibei.shiyi.order.dao.OrderMapper;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderDetailsVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderItemVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderProductVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSummaryVo;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderVo;
import com.baibei.shiyi.order.model.*;
import com.baibei.shiyi.order.service.*;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.client.admin.IProductStockFeign;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.feign.client.CustomerAddressFeign;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: Order服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderServiceImpl extends AbstractService<Order> implements IOrderService {
    @Autowired
    private OrderMapper tblOrdOrderMapper;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private IOrderProductService orderProductService;

    @Autowired
    private IOrderRefundService orderRefundService;

    @Autowired
    private IProductStockFeign productStockFeign;

    @Autowired
    private IAfterSaleOrderService afterSaleOrderService;

    @Autowired
    private CustomerAddressFeign customerAddressFeign;
    @Autowired
    private IOrderSettingService orderSettingService;

    @Autowired
    private AccountFeign mallAccountFeign;

    @Autowired
    private CustomerBeanFeign customerBeanFeign;

    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementTopic;

    @Value("${shiyi.refund}")
    private String refund;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public Order createOrder(String customerNo, String remark, Long addressId, OrderSetting orderSetting) {
        ApiResult<CustomerAddressVo> addressApiResult = customerAddressFeign.getCustomerAddressById(addressId);
        if (addressApiResult.hasFail() || addressApiResult.getData() == null) {
            log.info("获取客户地址信息失败，addressApiResult={}", addressApiResult.toString());
            throw new ServiceException("获取客户地址信息失败");
        }
        CustomerAddressVo addressVo = addressApiResult.getData();
        Order order = new Order();
        order.setId(IdWorker.getId());
        String orderNo = NoUtil.getMallOrderNo();
        order.setOrderNo(orderNo);
        order.setCustomerNo(customerNo);
        order.setReceiverName(addressVo.getReceivingName());
        order.setReceiverPhone(addressVo.getReceivingMobile());
        order.setReceiverProvince(addressVo.getProvince());
        order.setReceiverCity(addressVo.getCity());
        order.setReceiverRegion(addressVo.getArea());
        order.setReceiverDetailAddress(addressVo.getReceivingAddress());
        order.setFreightAmount(BigDecimal.ZERO);
        order.setStatus(Constants.MallOrderStatus.WAIT);
        order.setAutoCancelTime(DateUtil.addMinute(orderSetting.getNormalOrderOvertime()));
        order.setRemark(remark);
        order.setCreateTime(new Date());
        order.setModifyTime(new Date());
        save(order);
        return order;
    }

    @Override
    public Order findByOrderNo(String orderNo) {
        Condition condition = new Condition(Order.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orderNo", orderNo);
        List<Order> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public boolean updateOrder(String orderNo, String status, Order order) {
        Condition condition = new Condition(Order.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orderNo", orderNo);
        criteria.andEqualTo("status", status);
        return updateByConditionSelective(order, condition);
    }

    @Override
    public MyPageInfo<MyOrderVo> myOrderList(MyOrderDto myOrderDto) {
        // 查询全部
        if ("all".equals(myOrderDto.getStatus())) {
            return pageListForAll(myOrderDto, null, null);
            // 待付款
        } else if (Constants.MallOrderStatus.WAIT.equals(myOrderDto.getStatus())) {
            return pageListForWait(myOrderDto);
            // 已取消
        } else if (Constants.MallOrderStatus.CANCEL.equals(myOrderDto.getStatus())) {
            return pageListForAll(myOrderDto, Constants.MallOrderStatus.CANCEL, Constants.MallOrderStatus.CANCEL);
        } else {
            return orderItemService.myOrderList(myOrderDto);
        }
    }

    @Override
    public MyOrderDetailsVo orderDetails(MyOrderDetailsDto myOrderDetailsDto) {
        // 先从子订单查询，查询不到再从主订单查询
        MyOrderDetailsVo result = queryFromOrderItem(myOrderDetailsDto.getOrderNo());
        if (result == null) {
            result = queryFromOrder(myOrderDetailsDto.getOrderNo());
        }
        return result;
    }

    @Override
    public MyPageInfo<AdminOrderVo> adminPageList(AdminOrderDto orderDto) {
        PageHelper.startPage(orderDto.getCurrentPage(), orderDto.getPageSize());
        List<AdminOrderVo> pageList = this.tblOrdOrderMapper.orderPageList(orderDto);
        MyPageInfo<AdminOrderVo> orderVoMyPageInfo = new MyPageInfo<>(pageList);
        return orderVoMyPageInfo;
    }

    @Override
    public MyPageInfo<AdminOrderItemVo> orderAndOrderItemPageList(AdminOrderDto orderDto) {
        PageHelper.startPage(orderDto.getCurrentPage(), orderDto.getPageSize());
        List<AdminOrderItemVo> pageList = orderItemService.orderAndOrderItemPageList(orderDto);
        MyPageInfo<AdminOrderItemVo> orderVoMyPageInfo = new MyPageInfo<>(pageList);
        return orderVoMyPageInfo;
    }

    @Override
    public AdminOrderSummaryVo orderCount(AdminOrderDto orderDto) {
        AdminOrderSummaryVo adminOrderSummaryVo = new AdminOrderSummaryVo();
        Condition condition = new Condition(Order.class);
        condition.createCriteria().andEqualTo("flag", new Byte(Constants.Flag.VALID));
        List<Order> ordersCount = this.findByCondition(condition);
        adminOrderSummaryVo.setOrderCount(ordersCount.size());
        adminOrderSummaryVo.setOrderWait(getOrderStatusCount(Constants.MallOrderStatus.WAIT, orderDto));
        adminOrderSummaryVo.setOrderCompleted(getOrderStatusCount(Constants.MallOrderStatus.COMPLETED, orderDto));
        adminOrderSummaryVo.setOrderDeliveryed(getOrderStatusCount(Constants.MallOrderStatus.DELIVERYED, orderDto));
        adminOrderSummaryVo.setOrderUndelivery(getOrderStatusCount(Constants.MallOrderStatus.UNDELIVERY, orderDto));
        adminOrderSummaryVo.setOrderCancel(getOrderStatusCount(Constants.MallOrderStatus.CANCEL, orderDto));
        return adminOrderSummaryVo;
    }


    @Override
    public AdminOrderVo getById(Long Id) {
        Order order = this.findById(Id);
        if (order == null || order.getId() == null) {
            log.info("获取商品信息Id[{}]为null", Id);
            throw new ServiceException(String.format("主订单%s不存在", Id));
        }
        AdminOrderVo orderVo = BeanUtil.copyProperties(order, AdminOrderVo.class);
        orderVo.setReceiverDetailAddress(order.getReceiverProvince() + order.getReceiverCity() + order.getReceiverRegion() + order.getReceiverDetailAddress());
        List<AdminOrderProductVo> adminOrderProductVoList = orderProductService.findAdminByOrderId(order.getId());
        adminOrderProductVoList.stream().forEach(result -> {
            if ("consumption".equals(result.getPayWay())) {
                orderVo.setConsumptionTotalAmount(result.getTotalAmount());
            }
            if ("money".equals(result.getPayWay())) {
                orderVo.setMoneyTotalAmount(result.getTotalAmount());
            }
        });
        orderVo.setOrderProductVoList(adminOrderProductVoList);//设置订单商品详情
//        List<AdminOrderDetailsVo> orderDetailsVoList = this.orderItemService.findByAdminOrderId(order.getId()); //订单子订单
//        if (!orderDetailsVoList.isEmpty()) { //根据订单详情进行汇总和获取退款信息
//            orderVo.setOrderDetailsVoList(orderDetailsVoList);
//            orderVo.setOrderRefundVoList(this.orderRefundService.findByOrderId(order.getId()));//订单退款信息
//            orderVo.setIntegrationAmountList(this.integrationCount(orderDetailsVoList)); //获取不同的积分汇总信息
//        }
        //获取不同的积分抵扣统计
        return orderVo;
    }

    /**
     * 统计不同类型统计积分金额
     *
     * @param orderDetailsVoList
     * @return
     */
    public List<Map<String, Object>> integrationCount(List<AdminOrderDetailsVo> orderDetailsVoList) {
        List<Map<String, Object>> integralTypeVoList = new ArrayList<>();
        // stop1 过滤积分类型为空的数据
        List<AdminOrderDetailsVo> integralTypeList = orderDetailsVoList.stream().filter(integralType -> !StringUtils.isEmpty(integralType.getIntegralType())).collect(Collectors.toList());
        // stop2 根据积分类型分组
        Map<String, List<AdminOrderDetailsVo>> orderDetailsGroup = integralTypeList.stream().
                collect(Collectors.groupingBy(AdminOrderDetailsVo::getIntegralType));
        // stop3 添加积分到map中
        for (Map.Entry<String, List<AdminOrderDetailsVo>> entry : orderDetailsGroup.entrySet()) {
            Map<String, Object> integralCountMap = new HashMap<>();
            BigDecimal integrationAmount = entry.getValue().stream().map(result -> result.getIntegrationAmount()).reduce(BigDecimal::add).get();
            log.info("当前积分类型为{},总积分抵扣余额为{}", entry.getKey(), integrationAmount);
            integralCountMap.put("integralName", Constants.ProductIntegralType.getMapTypeText().get(entry.getKey()));
            integralCountMap.put("integralAmount", integrationAmount);
            integralTypeVoList.add(integralCountMap);
        }
        return integralTypeVoList;
    }


    /**
     * 添加物流信息(并收货)
     *
     * @param orderDto
     */
    @Override
    public void addLogistics(AdminOrderDto orderDto) {
        log.info("添加物流信息{}开始", JSONObject.toJSONString(orderDto));
        OrderItem orderItem = this.orderItemService.findByOrderItemNo(orderDto.getOrderItemNo());
        if (orderItem == null) {
            throw new ServiceException(String.format("获取子订单%s不存在", orderDto.getOrderItemNo()));
        }
        if (orderItem.getStatus().equals(Constants.MallOrderItemStatus.UNDELIVERY) || orderItem.getStatus().equals(Constants.MallOrderItemStatus.DELIVERYED)) {
            // stop1 如果用户待收货则变成收货
            if (orderItem.getRefundStatus() != null) {
                if (orderItem.getRefundStatus().equals(Constants.RefundStatus.APPLY_REFUND)
                        || orderItem.getRefundStatus().equals(Constants.RefundStatus.REFUNDED)) {
                    throw new ServiceException("商品处于退款申请和已经退款,不能发货");
                }
            }
            if (orderItem.getStatus().equals(Constants.MallOrderItemStatus.UNDELIVERY)) {
                orderItem.setDeliveryTime(new Date());
                orderItem.setStatus(Constants.MallOrderItemStatus.DELIVERYED);
            }
            OrderSetting orderSetting = orderSettingService.findBy();
            if (orderSetting != null) {
                orderItem.setAutoDeliveryTime(DateUtil.addDay(orderSetting.getConfirmOvertime()));
            }
            orderItem.setDeliveryCompany(orderDto.getDeliveryCompany());
            orderItem.setDeliveryNo(orderDto.getDeliveryNo());
            orderItem.setModifyTime(new Date());
            this.orderItemService.update(orderItem);
            // stop 2 设置主订单已经收货
            if (isOrderItemStatus(orderItem.getOrderId())) {
                Order order = this.findById(orderItem.getOrderId());
                log.info("修改主订单的信息为{}", JSONObject.toJSONString(order));
                order.setStatus(Constants.MallOrderStatus.DELIVERYED);
                order.setModifyTime(new Date());
                this.update(order);
            }
        } else {
            throw new ServiceException(String.format("待收货，已发货才能编辑物流信息"));
        }
    }

    /**
     * 判断主订单有为未收货的子订单
     */
    public Boolean isOrderItemStatus(Long orderId) {
        List<OrderItem> orderList = this.orderItemService.findByOrderId(orderId);
        if (orderList.isEmpty()) {
            return false;
        }
        // stop 1 判断子订单有一笔待发货
        Long count = orderList.stream().filter(result -> result.getStatus().equals(Constants.MallOrderItemStatus.UNDELIVERY)).count();
        log.info("判断当前子订单未收货的订单数量为{}", count);
        if (count > 0) {
            return false;
        }
        return true;
    }


    @Override
    public void closeItemOrder(AdminOrderDto orderDto) {
        OrderItem orderItem = this.orderItemService.findByOrderItemNo(orderDto.getOrderItemNo());
        if (orderItem == null) {
            throw new ServiceException(String.format("关闭子订单失败,子订单编号%s不存在", orderDto.getOrderItemNo()));
        }

        // 获取子订单是否有退款申请
        List<OrderRefund> orderRefundList = this.orderRefundService.findByOrderItemIdByRefundStatus(orderItem.getId(), Constants.RefundStatus.APPLY_REFUND);
        if (orderRefundList.size() > 0) {
            throw new ServiceException("当前子订单已经申请退款,暂时无法关闭");
        }
        // 如果子订单是未付款
        if (orderItem.getStatus().equals(Constants.MallOrderItemStatus.UNDELIVERY)
                || orderItem.getStatus().equals(Constants.MallOrderItemStatus.DELIVERYED)) {

            if (orderItem.getRefundStatus() != null) {
                //退款状态是已经申请退款
                if (orderItem.getRefundStatus().equals(Constants.RefundStatus.REFUNDED)
                        || orderItem.getRefundStatus().equals(Constants.RefundStatus.APPLY_REFUND)) {
                    throw new ServiceException("当前子订单退款状态不支持");
                }
            }
            // stop 1、修改订单为退款申请，并不退还订单金额和积分
            orderItem.setModifyTime(new Date());
            orderItem.setRefundStatus(Constants.MallOrderItemStatus.APPLY_REFUND); //退款状态,退款申请
            this.orderItemService.update(orderItem);
            // stop 2 增加订单退款申请记录
            OrderRefund orderRefund = new OrderRefund();
            orderRefund.setId(IdWorker.getId());
            orderRefund.setReviewStatus(ReviewStatusEnum.WAIT.getStatus()); //待审核
            orderRefund.setOrderItemId(orderItem.getId());
            orderRefund.setRefundAmount(orderItem.getActualAmount());
            orderRefund.setReason(orderDto.getReason()); //退款原因
            orderRefund.setRefundStatus(Constants.RefundStatus.APPLY_REFUND); //退款处理中
            orderRefund.setFlag(new Byte(Constants.Flag.VALID));
            orderRefundService.save(orderRefund);
        } else {
            throw new ServiceException("当前子订单状态为待发货或者待收货才能关闭");
        }
    }


    @Override
    public void itemRefundAgree(AdminOrderDto orderDto) {
        OrderRefund orderRefund = this.orderRefundService.findById(orderDto.getRefundId());

        OrderItem orderItem = this.orderItemService.findById(orderRefund.getOrderItemId());
        if (orderItem == null) {
            throw new ServiceException(String.format("关闭子订单失败,子订单%s不存在", orderDto.getOrderItemNo()));
        }
        // stop 1 验证订单的状态
        if (!orderItem.getRefundStatus().equals(Constants.MallOrderItemStatus.APPLY_REFUND)) {
            throw new ServiceException("当前子订单不是退款申请");
        }
        // stop2 修改订单状态
        orderItem.setStatus(Constants.MallOrderItemStatus.CANCEL);//修改订单已经取消
        orderItem.setRefundStatus(Constants.RefundStatus.REFUNDED); // 设置子订单已经退款
        orderItem.setModifyTime(new Date());  // 设置修改时间
        orderItem.setCancelTime(new Date()); //设置取消时间
        this.orderItemService.update(orderItem);

        // stop3 修改退款订单的记录
        orderRefund.setModifyTime(new Date());
        orderRefund.setReviewStatus(ReviewStatusEnum.PASSED.getStatus()); //审核通过
        orderRefund.setReviewTime(new Date()); //退款审核时间
        this.orderRefundService.update(orderRefund);


        //stop 4 退积分和退款
        ApiResult refundResult = ApiResult.error();
        if (orderItem.getPayWay().equals(Constants.PayWay.CONSUMPTION)) { // 退积分
            ChangeCustomerBeanDto changeCustomerBeanDto = new ChangeCustomerBeanDto();
            changeCustomerBeanDto.setChangeAmount(orderItem.getActualAmount());
            changeCustomerBeanDto.setReType(Constants.Retype.IN);
            changeCustomerBeanDto.setTradeType(RecordBeanTradeTypeEnum.CONSUMPTION_RETURN_INCOME.getCode());
            changeCustomerBeanDto.setCustomerNo(orderItem.getCustomerNo());
            changeCustomerBeanDto.setOrderNo(orderItem.getOrderItemNo());
            changeCustomerBeanDto.setCustomerBeanType(Constants.BeanType.CONSUMPTION);
            refundResult = customerBeanFeign.changeAmount(changeCustomerBeanDto);
            log.info("进行退款,退款码为{},退款消息为{},消息体为{}", refundResult.getCode(), refundResult.getMsg(), JSONObject.toJSONString(changeCustomerBeanDto));
        } else if (orderItem.getPayWay().equals(Constants.PayWay.MONEY)) { // 退现金
            List<ChangeAmountDto> changeAmountDtos = Lists.newArrayList();
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(orderItem.getCustomerNo());
            changeAmountDto.setOrderNo(orderItem.getOrderItemNo());
            changeAmountDto.setReType(Constants.Retype.IN);
            changeAmountDto.setChangeAmount(orderItem.getActualAmount());
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.REFUND_INCOME.getCode());
            changeAmountDtos.add(changeAmountDto);
            //扣减指定退款账户的钱
            ChangeAmountDto changeAmountDto1 = new ChangeAmountDto();
            changeAmountDto1.setCustomerNo(refund);
            changeAmountDto1.setOrderNo(orderItem.getOrderItemNo());
            changeAmountDto1.setReType(Constants.Retype.OUT);
            changeAmountDto1.setChangeAmount(orderItem.getActualAmount());
            changeAmountDto1.setTradeType(String.valueOf(TradeMoneyTradeTypeEnum.REFUND_PAY.getCode()));
            changeAmountDtos.add(changeAmountDto1);
            refundResult = mallAccountFeign.changeMoneyList(changeAmountDtos);
            log.info("进行退款,退款码为{},退款消息为{},消息体为{}", refundResult.getCode(), refundResult.getMsg(), JSONObject.toJSONString(changeAmountDto));
        } else if (orderItem.getPayWay().equals(Constants.BeanType.MALLACCOUNT)) { //新的商城交易账号
            List<ChangeMultipleFundDto> changeMultipleFundDto = new ArrayList<>();
            // stop 客户退款,增加余额
            ChangeMultipleFundDto customerAccount = new ChangeMultipleFundDto();
            customerAccount.setCustomerNo(orderItem.getCustomerNo());
            customerAccount.setOrderNo(orderItem.getOrderItemNo());
            // stop 增加客户退款类型
            List<ChangeMultipleFundType> changeMultipleFundTypes = new ArrayList<>();
            ChangeMultipleFundType changeMultipleFundType = new ChangeMultipleFundType();
            changeMultipleFundType.setChangeAmount(orderItem.getActualAmount());
            changeMultipleFundType.setFundType(Constants.BeanType.MALLACCOUNT);
            changeMultipleFundType.setRetype(Constants.Retype.IN);
            changeMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_RETURN_INCOME.getCode());
            changeMultipleFundTypes.add(changeMultipleFundType);
            customerAccount.setChangeMultipleFundTypeList(changeMultipleFundTypes);
            changeMultipleFundDto.add(customerAccount);

            // 减去退款账号
            ChangeMultipleFundDto refundAccount = new ChangeMultipleFundDto();
            refundAccount.setCustomerNo(refund);
            refundAccount.setOrderNo(orderItem.getOrderItemNo());
            // stop 退款账号
            List<ChangeMultipleFundType> refundAccountFundTypes = new ArrayList<>();
            ChangeMultipleFundType refundMultipleFundType = new ChangeMultipleFundType();
            refundMultipleFundType.setFundType(Constants.BeanType.MALLACCOUNT);
            refundMultipleFundType.setChangeAmount(orderItem.getActualAmount());
            refundMultipleFundType.setRetype(Constants.Retype.OUT);
            refundMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_RETURN_PAY.getCode());
            refundAccountFundTypes.add(refundMultipleFundType);
            refundAccount.setChangeMultipleFundTypeList(refundAccountFundTypes);
            changeMultipleFundDto.add(refundAccount);
            refundResult=mallAccountFeign.changeMultipleFund(changeMultipleFundDto);
        }
        // 补偿机制,这笔退款失败,然后记录这笔数据,去跑定时任务进行金额退款
        if (refundResult.hasFail()) {
            log.info("当前退款失败的code为{},msg 为{}", refundResult.getCode(), refundResult.getMsg());
            throw new ServiceException("退款失败");
        } else {
            orderRefund.setRefundStatus(Constants.RefundStatus.REFUNDED); //更改退款结果
            this.orderRefundService.update(orderRefund); //如果退款成功,应该发送一条消息
            // 现金支付发送
            if (Constants.PayWay.MONEY.equals(orderItem.getPayWay())) {
                sendSettlementCleanMsg(orderItem);
            }
        }
        // stop 5 退库存
        ChangeStockDto changeStockDto = new ChangeStockDto();
        changeStockDto.setShelfId(orderItem.getShelfId());
        changeStockDto.setChangeCount(new BigDecimal(orderItem.getQuantity()));
        changeStockDto.setOrderNo(orderItem.getOrderItemNo());
        changeStockDto.setRetype(Constants.Retype.IN);//新增库存
        changeStockDto.setOperatorNo(orderItem.getCustomerNo());//用户编码
        changeStockDto.setSkuId(orderItem.getSkuId());
        changeStockDto.setChangeType(Constants.ProductStockChangeType.TRADE);//交易

        // 如果退库存失败,会存在什么问题,库存没变或者失败，人为补偿
        ApiResult changeStock = productStockFeign.changeStock(changeStockDto);
        log.info("当前退库存的结果为{},消息为{},消息体为{}", changeStock.getCode(), changeStock.getMsg(), JSONObject.toJSONString(changeStockDto));

        // stop 6 修改主订单状态
        String orderStatus = getOrderItemCancelOrCompleted(orderItem.getOrderId());
        if (!StringUtils.isEmpty(orderStatus)) {
            Order order = this.findById(orderItem.getOrderId());
            order.setStatus(orderStatus);
            order.setModifyTime(new Date());
            if (orderStatus.equals(Constants.MallOrderItemStatus.CANCEL)) {
                order.setCancelTime(new Date());
            }
            this.update(order);
        }
    }

    /**
     * 发送消息给清结算
     *
     * @param orderItem
     * @return
     */
    private void sendSettlementCleanMsg(OrderItem orderItem) {
        List<SettlementMetadataMsg> settlementMetadataMsgArrayList = new ArrayList<>();
        Date nowDate = new Date();
        SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
        customerMsg.setCustomerNo(orderItem.getCustomerNo());
        customerMsg.setAmount(orderItem.getActualAmount());
        customerMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        customerMsg.setRetype(Constants.Retype.IN);
        customerMsg.setOrderNo(orderItem.getOrderItemNo());
        customerMsg.setApplicationName(applicationName);
        customerMsg.setEventTime(nowDate);
        settlementMetadataMsgArrayList.add(customerMsg);

        SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
        distributorMsg.setCustomerNo(refund); // 退款账号
        distributorMsg.setAmount(orderItem.getActualAmount());
        distributorMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        distributorMsg.setOrderNo(orderItem.getOrderItemNo());
        distributorMsg.setRetype(Constants.Retype.OUT);
        distributorMsg.setApplicationName(applicationName);
        distributorMsg.setEventTime(nowDate);
        settlementMetadataMsgArrayList.add(distributorMsg);
        rocketMQUtil.sendMsg(settlementTopic, JacksonUtil.beanToJson(settlementMetadataMsgArrayList), orderItem.getOrderItemNo());
        log.info("订单{}发送资金清算消息成功，orderNo={}", orderItem.getOrderItemNo());
    }

    /**
     * 获取交易类型
     *
     * @param payWay
     * @return
     */
    private String genBeanType(String payWay) {
        switch (payWay) {
            case Constants.BeanType.SHIYI: //世屹积分
                return Constants.BeanType.SHIYI;
            case Constants.BeanType.CONSUMPTION: // 消费积分
                return Constants.BeanType.CONSUMPTION;
            case Constants.BeanType.EXCHANGE: // 兑换积分
                return Constants.BeanType.EXCHANGE;
            case Constants.BeanType.MONEY: //现金
                return Constants.BeanType.MONEY;
            default:
                throw new ServiceException("积分类型不支持");
        }
    }

    /**
     * 获取子订单已经完成或取消的状态
     *
     * @return
     */
    public String getOrderItemCancelOrCompleted(Long orderId) {
        List<OrderItem> orderList = this.orderItemService.findByOrderId(orderId);
        if (orderList.isEmpty()) {
            return null;
        }
        // stop1 查询子订单是否有未收货和已经收货
        Long count = orderList.stream().filter(result -> {
            if (result.getStatus().equals(Constants.MallOrderItemStatus.UNDELIVERY)
                    || result.getStatus().equals(Constants.MallOrderItemStatus.DELIVERYED)) {
                return true;
            }
            return false;
        }).count();

        if (count > 0) {
            return null;
        }
        // stop2 查询订单是否有完成的订单
        Long completedCount = orderList.stream().filter(result -> result.getStatus().equals(Constants.MallOrderItemStatus.COMPLETED)).count();
        log.info("获取所有子订单状态完成的状态数量为{}", completedCount);
        if (completedCount > 0) {
            return Constants.MallOrderItemStatus.COMPLETED;
        }
        // stop 返回取消状态
        return Constants.MallOrderItemStatus.CANCEL;
    }


    /**
     * 退款驳回
     *
     * @param orderDto
     */
    @Override
    public void itemRefundReject(AdminOrderDto orderDto) {
        // 1、获取退款状态
        OrderRefund orderRefund = this.orderRefundService.findById(orderDto.getRefundId());
        if (orderRefund == null) {
            throw new ServiceException(String.format("退款Id%s不存在", orderDto.getRefundId()));
        }
        OrderItem orderItem = this.orderItemService.findById(orderRefund.getOrderItemId());
        if (orderItem == null) {
            throw new ServiceException(String.format("关闭子订单失败,子订单%s不存在", orderDto.getOrderItemNo()));
        }
        if (!orderItem.getRefundStatus().equals(Constants.MallOrderItemStatus.APPLY_REFUND)) {
            throw new ServiceException("当前子订单不是退款申请");
        }

        // stop 修改子订单
        orderItem.setModifyTime(new Date());
        orderItem.setRefundStatus(Constants.RefundStatus.REJECT_REFUND); //退款驳回
        this.orderItemService.update(orderItem);

        // stop 修改退款订单
        orderRefund.setModifyTime(new Date());
        orderRefund.setReviewStatus(ReviewStatusEnum.REJECTED.getStatus());//审核状态
        orderRefund.setRefundStatus(Constants.RefundStatus.REJECT_REFUND); //退款状态
        orderRefund.setReviewTime(new Date()); //审核时间
        this.orderRefundService.update(orderRefund);
    }

    @Override
    public Integer getOrderStatusCount(String status, AdminOrderDto orderDto) {
        Condition condition = new Condition(Order.class);
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
        return this.tblOrdOrderMapper.selectCountByCondition(condition);
    }

    @Override
    public void closeOrder(AdminOrderDto orderDto) {
        log.info("当前关闭订单的信息为{}", JSONObject.toJSONString(orderDto));
        Order order = this.findByOrderNo(orderDto.getOrderNo());
        if (!order.getStatus().equals(Constants.MallOrderStatus.WAIT)) {
            throw new ServiceException("关闭订单失败,待支付才能关闭订单");
        }
        order.setCancelTime(new Date());
        order.setCancelReason(orderDto.getReason());
        order.setStatus(Constants.MallOrderStatus.CANCEL);
        order.setModifyTime(new Date());
        this.update(order);
    }

    @Override
    public List<AdminOrderVo> findByOrderList(AdminOrderDto orderDto) {
        return this.tblOrdOrderMapper.orderPageList(orderDto);
    }

    @Override
    public List<AdminOrderItemVo> findByOrderItemListExport(AdminOrderDto orderDto) {
        return orderItemService.orderAndOrderItemPageList(orderDto);
    }

    /**
     * 从订单主表查询订单详情
     *
     * @param orderNo
     * @return
     */
    private MyOrderDetailsVo queryFromOrder(String orderNo) {
        Order order = findByOrderNo(orderNo);
        if (order == null) {
            log.info("订单不存在，orderNo={}", orderNo);
            return null;
        }
        MyOrderDetailsVo result = BeanUtil.copyProperties(order, MyOrderDetailsVo.class);
        StringBuffer sb = new StringBuffer();
        sb.append(order.getReceiverProvince())
                .append(order.getReceiverCity())
                .append(order.getReceiverRegion())
                .append(order.getReceiverDetailAddress());
        result.setDetailsAddress(sb.toString());
        result.setStatusText(MallOrderStatusEnum.getDesc(order.getStatus()));
        List<OrderProduct> orderProductList = orderProductService.findByOrderId(order.getId());
        List<MyOrderProductDetailsVo> productList = new ArrayList<>();
        List<KeyValue> payList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProductList) {
            MyOrderProductDetailsVo product = new MyOrderProductDetailsVo();
            product.setProductName(orderProduct.getProductName());
            product.setProductImg(orderProduct.getProductImg());
            product.setAmount(orderProduct.getAmount());
            product.setQuantity(orderProduct.getQuantity());
            product.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(orderProduct.getSkuProperty()));
            product.setShelfId(orderProduct.getShelfId());
            product.setSkuId(orderProduct.getSkuId());
            product.setShelfType(orderProduct.getShelfType());
            productList.add(product);
            KeyValue kv = new KeyValue();
            kv.setKey(ShelfTypeUtil.getDesc(orderProduct.getShelfType()));
            kv.setValue(NumberUtil.roundFloor(orderProduct.getTotalAmount(), 2).toPlainString());
            payList.add(kv);
        }
        result.setPayList(payList);
        result.setAutoCancelTime(order.getAutoCancelTime().getTime() - System.currentTimeMillis());
        result.setProductList(productList);
        result.setFreightAmount(order.getFreightAmount());
        return result;
    }

    /**
     * 从订单明细表查询订单详情
     *
     * @param orderNo
     * @return
     */
    private MyOrderDetailsVo queryFromOrderItem(String orderNo) {
        OrderItem orderItem = orderItemService.findByOrderItemNo(orderNo);
        if (orderItem == null) {
            log.info("订单明细不存在，orderItemNo={}", orderNo);
            return null;
        }
        Order order = findById(orderItem.getOrderId());
        if (order == null) {
            log.info("订单不存在，orderId={}", orderItem.getOrderId());
            return null;
        }
        MyOrderDetailsVo result = new MyOrderDetailsVo();
        String status = afterSaleOrderService.selectByOrderItemNoToStatus(orderItem.getOrderItemNo(), orderItem.getCustomerNo());
        if ("init".equals(status)) {
            result.setAfterSaleOrderStatus(status);
            result.setAfterSaleOrderStatusRemark("申请售后");
        } else if ("waiting".equals(status) || "revoked".equals(status)) {
            result.setAfterSaleOrderStatus(status);
            result.setAfterSaleOrderStatusRemark("已申请售后");
        } else if ("success".equals(status) || "doing".equals(status)) {
            result.setAfterSaleOrderStatus(status);
            result.setAfterSaleOrderStatusRemark("审核通过");
        } else if ("fail".equals(status)) {
            result.setAfterSaleOrderStatus(status);
            result.setAfterSaleOrderStatusRemark("审核不通过");
        }

        // 主订单中获取的信息
        StringBuffer sb = new StringBuffer();
        sb.append(order.getReceiverProvince())
                .append(order.getReceiverCity())
                .append(order.getReceiverRegion())
                .append(order.getReceiverDetailAddress());
        result.setPayWay(orderItem.getPayWay());
        result.setDetailsAddress(sb.toString());
        result.setReceiverName(order.getReceiverName());
        result.setReceiverPhone(order.getReceiverPhone());
        result.setCreateTime(order.getCreateTime());    // 下单时间
        result.setPayTime(orderItem.getCreateTime());  // 支付时间
        result.setCustomerNo(order.getCustomerNo());
        result.setFreightAmount(order.getFreightAmount());
        result.setRemark(order.getRemark());
        // 订单商品信息
        MyOrderProductDetailsVo product = new MyOrderProductDetailsVo();
        product.setProductName(orderItem.getProductName());
        product.setProductImg(orderItem.getProductImg());
        product.setAmount(orderItem.getAmount());
        product.setQuantity(orderItem.getQuantity());
        product.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(orderItem.getSkuProperty()));
        product.setShelfId(orderItem.getShelfId());
        product.setSkuId(orderItem.getSkuId());
        product.setShelfType(orderItem.getShelfType());
        if ("send_integral".equals(orderItem.getShelfType())) {
            product.setShelfTypeDesc("赠送积分商品");
        }
        if ("consume_ingtegral".equals(orderItem.getShelfType())) {
            product.setShelfTypeDesc("消费积分商品");
        }
        if ("transfer_product".equals(orderItem.getShelfType())) {
            product.setShelfTypeDesc("交割商品");
        }
        if (!StringUtils.isEmpty(orderItem.getSendIntegralJson())) {
            List<KeyValue> keyValueList = JSON.parseArray(orderItem.getSendIntegralJson(), KeyValue.class);
            BigDecimal totalIntegral = BigDecimal.ZERO;
            for (KeyValue item : keyValueList) {
                totalIntegral = totalIntegral.add(new BigDecimal(item.getValue()));
            }
            product.setSendIntegral(totalIntegral);
        }
        result.setStatus(orderItem.getStatus());
        List<MyOrderProductDetailsVo> productList = new ArrayList<>();
        productList.add(product);
        result.setProductList(productList);
        // 订单详情信息
        result.setStatusText(MallOrderStatusEnum.getDesc(orderItem.getStatus()));
        result.setDeliveryCompany(orderItem.getDeliveryCompany());
        result.setDeliveryNo(orderItem.getDeliveryNo());
        result.setCompletedTime(orderItem.getCompletedTime());
        result.setTotalAmount(orderItem.getAmount().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        result.setActualAmount(orderItem.getActualAmount());
        result.setOrderNo(orderItem.getOrderItemNo());
        result.setCancelTime(orderItem.getCancelTime());
        result.setRefundStatus(orderItem.getRefundStatus());
        result.setRefundStatusText(StringUtils.isEmpty(orderItem.getRefundStatus()) ? "" : getRefundText(orderItem.getRefundStatus()));
        List<KeyValue> payList = new ArrayList<>();
        KeyValue kv = new KeyValue();
        kv.setKey(ShelfTypeUtil.getDesc(orderItem.getShelfType()));
        kv.setValue(NumberUtil.roundFloor(orderItem.getActualAmount(), 2).toPlainString());
        payList.add(kv);
        result.setPayList(payList);
        return result;
    }

    private String getRefundText(String refundStatus) {
        switch (refundStatus) {
            case Constants.RefundStatus.APPLY_REFUND:
                return "审核中";
            case Constants.RefundStatus.REJECT_REFUND:
                return "审核拒绝";
            case Constants.RefundStatus.REFUNDED:
                return "已退款";
            default:
                return "";
        }
    }


    /**
     * 待付款的分页列表查询
     *
     * @param myOrderDto
     * @return
     */
    private MyPageInfo<MyOrderVo> pageListForWait(MyOrderDto myOrderDto) {
        Condition condition = new Condition(Order.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("status", myOrderDto.getStatus());
        criteria.andEqualTo("customerNo", myOrderDto.getCustomerNo());
        MyPageInfo<Order> myPageInfo = pageList(condition, PageParam.buildWithDefaultSort(myOrderDto.getCurrentPage(), myOrderDto.getPageSize()));
        List<MyOrderVo> result = new LinkedList<>();
        List<Order> list = myPageInfo.getList();
        for (Order item : list) {
            List<ProductNameImgVo> productList = new ArrayList<>();
            List<KeyValue> payList = new ArrayList<>();
            MyOrderVo vo = new MyOrderVo();
            vo.setOrderNo(item.getOrderNo());
            vo.setStatus(item.getStatus());
            vo.setStatusText(MallOrderStatusEnum.getDesc(item.getStatus()));
            vo.setCreateTime(item.getCreateTime());
            List<OrderProduct> orderProductList = orderProductService.findByOrderId(item.getId());
            for (OrderProduct orderProduct : orderProductList) {
                productList.add(buildProductNameImgVo(orderProduct.getProductName(), orderProduct.getProductImg(),
                        orderProduct.getSkuProperty(), orderProduct.getQuantity()));
                vo.setProductCount(vo.getProductCount() + orderProduct.getQuantity());
                KeyValue kv = new KeyValue();
                kv.setKey(ShelfTypeUtil.getDesc(orderProduct.getShelfType()));
                kv.setValue(NumberUtil.roundFloor(orderProduct.getTotalAmount(), 2).toPlainString());
                payList.add(kv);
            }
            vo.setPayList(payList);
            vo.setProductList(productList);
            result.add(vo);
        }
        return PageUtil.transform(myPageInfo, result);
    }

    /**
     * 查询全部订单、取消订单，需要联合主订单+订单明细做分页查询
     *
     * @param myOrderDto
     * @return
     */
    private MyPageInfo<MyOrderVo> pageListForAll(MyOrderDto myOrderDto, String orderStatus, String orderItemStatus) {
        PageHelper.startPage(myOrderDto.getCurrentPage(), myOrderDto.getPageSize());
        List<MyOrderTempVo> list = tblOrdOrderMapper.unionAllList(myOrderDto.getCustomerNo(), orderStatus, orderItemStatus);
        MyPageInfo<MyOrderTempVo> myPageInfo = new MyPageInfo<>(list);
        List<MyOrderVo> resultList = new LinkedList<>();
        for (MyOrderTempVo vo : list) {
            MyOrderVo myOrderVo = BeanUtil.copyProperties(vo, MyOrderVo.class);
            myOrderVo.setStatusText(MallOrderStatusEnum.getDesc(vo.getStatus()));
            List<ProductNameImgVo> productList = new ArrayList<>();
            List<KeyValue> payList = new ArrayList<>();
            // 主订单
            if ("order".equals(vo.getType())) {
                List<OrderProduct> orderProductList = orderProductService.findByOrderId(vo.getId());
                for (OrderProduct orderProduct : orderProductList) {
                    productList.add(buildProductNameImgVo(orderProduct.getProductName(), orderProduct.getProductImg(),
                            orderProduct.getSkuProperty(), orderProduct.getQuantity()));
                    myOrderVo.setProductCount(myOrderVo.getProductCount() + orderProduct.getQuantity());
                    KeyValue kv = new KeyValue();
                    kv.setKey(ShelfTypeUtil.getDesc(orderProduct.getShelfType()));
                    kv.setValue(NumberUtil.roundFloor(orderProduct.getTotalAmount(), 2).toPlainString());
                    payList.add(kv);
                    myOrderVo.setPayList(payList);
                }
                // 订单明细
            } else {
                myOrderVo.setProductCount(1);
                productList.add(buildProductNameImgVo(vo.getProductName(), vo.getProductImg(), vo.getSkuProperty(), vo.getQuantity()));
                KeyValue kv = new KeyValue();
                kv.setKey(ShelfTypeUtil.getDesc(vo.getShelfType()));
                kv.setValue(NumberUtil.roundFloor(vo.getActualAmount(), 2).toPlainString());
                payList.add(kv);
                myOrderVo.setPayList(payList);
            }
            myOrderVo.setProductList(productList);
            resultList.add(myOrderVo);
        }
        MyPageInfo<MyOrderVo> result = PageUtil.transform(myPageInfo, resultList);
        return result;
    }

    private ProductNameImgVo buildProductNameImgVo(String productName, String productImg, String skuProperty, Integer quantity) {
        ProductNameImgVo productNameImgVo = new ProductNameImgVo();
        productNameImgVo.setProductName(productName);
        productNameImgVo.setProductImg(productImg);
        productNameImgVo.setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(skuProperty));
        productNameImgVo.setQuantity(quantity);
        return productNameImgVo;
    }


}
