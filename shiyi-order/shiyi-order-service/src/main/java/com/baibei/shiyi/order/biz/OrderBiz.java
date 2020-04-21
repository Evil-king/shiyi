package com.baibei.shiyi.order.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.base.admin.IAdminCustomerBeanBase;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundType;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.common.tool.validator2.FluentValidator;
import com.baibei.shiyi.common.tool.validator2.ValidatorContext;
import com.baibei.shiyi.order.biz.validator.OrderPayOrderValidator;
import com.baibei.shiyi.order.biz.validator.OrderSubmitBalanceValidator;
import com.baibei.shiyi.order.biz.validator.OrderSubmitCustomerValidator;
import com.baibei.shiyi.order.biz.validator.OrderSubmitProductValidator;
import com.baibei.shiyi.order.common.dto.OrderPayDto;
import com.baibei.shiyi.order.common.dto.OrderPayInfoDto;
import com.baibei.shiyi.order.common.dto.OrderSubmitDto;
import com.baibei.shiyi.order.common.dto.ProductDto;
import com.baibei.shiyi.order.common.vo.KeyValue;
import com.baibei.shiyi.order.common.vo.OrderPayInfoVo;
import com.baibei.shiyi.order.common.vo.OrderPayVo;
import com.baibei.shiyi.order.common.vo.OrderSubmitVo;
import com.baibei.shiyi.order.feign.base.dto.OrderPay;
import com.baibei.shiyi.order.feign.base.dto.OrderReport;
import com.baibei.shiyi.order.feign.base.dto.OrderUpdate;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.model.OrderProduct;
import com.baibei.shiyi.order.model.OrderSetting;
import com.baibei.shiyi.order.service.*;
import com.baibei.shiyi.order.transactional.TransactionalCallbackService;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.message.ChangeSellCountMessage;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.client.shiyi.ProductFeign;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.baibei.shiyi.user.feign.bean.vo.CustomerAddressVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerAddressFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/2 14:49
 * @description: 订单业务逻辑
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class OrderBiz {
    @Value("${rocketmq.cancelOrder.topics}")
    private String cancelOrderTopics;
    @Value("${rocketmq.splitOrder.topics}")
    private String splitOrderTopics;
    @Value("${rocketmq.changeSellCount.topics}")
    private String changeSellCountTopics;
    @Value("${pay.handler.list}")
    private String payHandlerList;
    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;
    @Value("${shiyi.mall.operator}")
    private String mallOperator;
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${rocketmq.orderReport.topics}")
    private String orderReportTopics;
    @Value("${rocketmq.orderPay.topics}")
    private String orderPayTopics;
    @Value("${rocketmq.orderUpdate.topics}")
    private String orderUpdateTopics;

    @Autowired
    private ProductFeign productFeign;
    @Autowired
    private CustomerAddressFeign customerAddressFeign;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderProductService orderProductService;
    @Autowired
    private IOrderSettingService orderSettingService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private IOrderItemService orderItemService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private TransactionalCallbackService transactionalCallbackService;
    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private IAdminCustomerBeanBase customerBeanBase;
    @Autowired
    private OrderSubmitProductValidator productValidator;
    @Autowired
    private OrderSubmitCustomerValidator customerValidator;
    @Autowired
    private OrderSubmitBalanceValidator balanceValidator;
    @Autowired
    private OrderPayOrderValidator orderPayOrderValidator;

    /**
     * 提交订单页面数据获取
     *
     * @param payInfoDto
     * @return
     */
    public ApiResult<OrderPayInfoVo> payInfo(OrderPayInfoDto payInfoDto) {
        if (CollectionUtils.isEmpty(payInfoDto.getProductList())) {
            return ApiResult.badParam("商品信息为空");
        }
        OrderPayInfoVo vo = new OrderPayInfoVo();
        List<KeyValue> kvList = new ArrayList<>();
        ApiResult<CustomerBeanVo> beanBalanceResult = customerBeanBase.getBeanBalance(payInfoDto.getCustomerNo());
        if (beanBalanceResult.hasFail()) {
            log.info("获取用户账户信息失败，apiResult={}", beanBalanceResult.toString());
            throw new ServiceException("获取用户账户信息失败");
        }
        CustomerBeanVo customerBeanVo = beanBalanceResult.getData();
        vo.setMallBalance(StringUtils.isEmpty(customerBeanVo) ? BigDecimal.ZERO : customerBeanVo.getMallAccountBalance());
        // 商品的基本信息
        List<ShelfRefDto> shelfProductDtoList = BeanUtil.copyProperties(payInfoDto.getProductList(), ShelfRefDto.class);
        ApiResult<List<BaseShelfVo>> productApiResult = productFeign.getBatchShelfProductInfo(shelfProductDtoList);
        if (productApiResult.hasFail() || productApiResult.getData() == null) {
            log.info("获取商品信息失败，apiResult={}", productApiResult.toString());
            return ApiResult.badParam("获取商品信息失败");
        }
        List<BaseShelfVo> shelfVoList = productApiResult.getData();
        for (BaseShelfVo shelfVo : shelfVoList) {
            for (ProductDto productDto : payInfoDto.getProductList()) {
                if (shelfVo.getShelfId().equals(productDto.getShelfId()) && shelfVo.getSkuId().equals(productDto.getSkuId())) {
                    KeyValue kv = new KeyValue();
                    kv.setKey(ShelfTypeUtil.getDesc(shelfVo.getShelfType()));
                    kv.setValue(NumberUtil.roundFloor(shelfVo.getShelfPrice().multiply(productDto.getNum()), 2).toPlainString());
                    kvList.add(kv);
                }
            }
        }
        vo.setPayList(KeyValue.merge(kvList));
        for (KeyValue kv : vo.getPayList()) {
            if ("现金".equals(kv.getKey())) {
                if (new BigDecimal(kv.getValue()).compareTo(vo.getMallBalance()) > 0) {
                    vo.setShowMallBalance("hide");
                } else {
                    vo.setShowMallBalance("show");
                }
                kv.setValue("¥" + kv.getValue());
            }
        }
        // 客户收货地址
        ApiResult<CustomerAddressVo> customerAddressVoApiResult = customerAddressFeign.getDefaultAddressByNo(payInfoDto.getCustomerNo());
        if (customerAddressVoApiResult.hasSuccess()) {
            CustomerAddressVo customerAddressVo = customerAddressVoApiResult.getData();
            if (customerAddressVo != null) {
                vo.setReceivingName(customerAddressVo.getReceivingName());
                vo.setReceivingMobile(customerAddressVo.getReceivingMobile());
                StringBuffer sb = new StringBuffer();
                sb.append(customerAddressVo.getProvince())
                        .append(customerAddressVo.getCity())
                        .append(customerAddressVo.getArea())
                        .append(customerAddressVo.getReceivingAddress());
                vo.setDetailsAddress(sb.toString());
                vo.setAddressId(customerAddressVo.getId());
            }
        }
        // 运费计算
        BigDecimal freightAmount = BigDecimal.ZERO;
        vo.setFreightAmount(freightAmount);
        return ApiResult.success(vo);
    }


    /**
     * 提交订单
     *
     * @param orderSubmitDto
     * @return
     */
    public ApiResult<OrderSubmitVo> submit(OrderSubmitDto orderSubmitDto) {
        log.info("开始提交订单，dto={}", JSON.toJSON(orderSubmitDto));
        long start = System.currentTimeMillis();
        if (!Constants.BeanType.MONEY.equals(orderSubmitDto.getPayWay()) && !Constants.BeanType.MALLACCOUNT.equals(orderSubmitDto.getPayWay())) {
            throw new ServiceException("非法支付类型");
        }
        // 业务校验
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(orderSubmitDto, productValidator)
                .on(orderSubmitDto, customerValidator)
                .on(orderSubmitDto, balanceValidator)
                .doValidate();
        ValidatorContext validatorContext = fluentValidator.getContext();
        List<BaseShelfVo> baseShelfVoList = validatorContext.getClazz("baseShelfVoList");
        if (CollectionUtils.isEmpty(baseShelfVoList)) {
            throw new ServiceException("商品信息为空");
        }
        CustomerVo customerVo = validatorContext.getClazz("customer");
        if (customerVo == null) {
            throw new ServiceException("客户信息为空");
        }
        OrderSetting orderSetting = orderSettingService.findBy();
        if (orderSetting == null || orderSetting.getNormalOrderOvertime() == null) {
            log.info("订单设置信息不存在");
            throw new ServiceException("订单设置信息为空");
        }
        // 创建待付款订单
        Order order = orderService.createOrder(orderSubmitDto.getCustomerNo(), orderSubmitDto.getRemark(), orderSubmitDto.getAddressId(), orderSetting);
        // 创建商品订单信息
        orderProductService.create(order.getId(), orderSubmitDto.getProductList(), baseShelfVoList, orderSubmitDto.getPayWay());
        // 如果是从购物车提交，则需要删除购物车商品
        if (!StringUtils.isEmpty(orderSubmitDto.getCardIds())) {
            cartService.deleteByIds(orderSubmitDto.getCardIds());
        }
        // 扣减库存
        ApiResult stockResult = deductStock(orderSubmitDto, order.getOrderNo());
        if (stockResult.hasFail()) {
            log.info("订单{}扣减库存失败，stockResult={}", order.getOrderNo(), stockResult.toString());
            throw new ServiceException("扣减库存失败");
        }
        // 订单事务提交后执行逻辑
        afterOrderSubmit(order, orderSetting, baseShelfVoList, customerVo, orderSubmitDto.getPayWay());
        // 返回结果
        OrderSubmitVo result = new OrderSubmitVo();
        result.setOrderNo(order.getOrderNo());
        result.setNotice(MessageFormat.format("请在{0}分钟内确认支付，过期将取消订单", orderSetting.getNormalOrderOvertime()));
        log.info("提交订单结束，dto={}，耗时={}", JSON.toJSON(orderSubmitDto), System.currentTimeMillis() - start);
        return ApiResult.success(result);
    }

    /**
     * 提交订单的事务提交后
     *
     * @param order
     * @param orderSetting
     * @param baseShelfVoList
     */
    private void afterOrderSubmit(Order order, OrderSetting orderSetting, List<BaseShelfVo> baseShelfVoList, CustomerVo customerVo, String payWay) {
        // 事务提交后执行逻辑
        transactionalCallbackService.execute(() -> {
            // 发送订单超时延迟消息
            Integer delayLevel = rocketMQUtil.getDelayLevelBySecond(orderSetting.getNormalOrderOvertime() * 60);
            rocketMQUtil.sendMsg(cancelOrderTopics, order.getOrderNo(), order.getOrderNo(), delayLevel);
            // 异步向福清报送订单信息
            if (!StringUtils.isEmpty(customerVo.getSigning()) && "1".equals(customerVo.getSigning()) && Constants.BeanType.MONEY.equals(payWay)) {
                BaseShelfVo baseShelfVo = baseShelfVoList.get(0);
                OrderReport msg = new OrderReport();
                msg.setTradeOrderNo(order.getOrderNo());
                msg.setSellerMemCode(mallOperator);
                msg.setSellerExchangeFundAccount(mallOperator);
                msg.setProductCode(baseShelfVo.getSpuNo());
                msg.setProductName(baseShelfVo.getProductName());
                msg.setBuyerMemCode(order.getCustomerNo());
                msg.setBuyerExchangeFundAccount(order.getCustomerNo());
                msg.setDealQuantity(Double.valueOf(baseShelfVoList.size()));
                msg.setGoodsDealType("2");
                msg.setDealPrice(baseShelfVo.getShelfPrice().longValue() * 100);
                msg.setDealTicketPrice(0L);
                msg.setDealTotalPrice(baseShelfVo.getShelfPrice().longValue() * 100);
                msg.setDealfundPrice(baseShelfVo.getShelfPrice().longValue() * 100);
                msg.setDealTime(Integer.valueOf(DateUtil.HHmmssNoLine.get().format(new Date())));
                msg.setGoodsStatus("1");
                rocketMQUtil.sendMsg(orderReportTopics, JacksonUtil.beanToJson(msg), order.getOrderNo());
            }
        });
    }

    /**
     * 扣减库存
     *
     * @param orderSubmitDto
     * @param orderNo
     * @return
     */
    private ApiResult deductStock(OrderSubmitDto orderSubmitDto, String orderNo) {
        List<ChangeStockDto> changeStockDtoList = new ArrayList<>();
        List<ProductDto> productDtoList = orderSubmitDto.getProductList();
        for (ProductDto dto : productDtoList) {
            ChangeStockDto changeStockDto = new ChangeStockDto();
            changeStockDto.setShelfId(dto.getShelfId());
            changeStockDto.setSkuId(dto.getSkuId());
            changeStockDto.setChangeCount(dto.getNum());
            changeStockDto.setOrderNo(orderNo);
            changeStockDto.setOperatorNo(orderSubmitDto.getCustomerNo());
            changeStockDto.setChangeType(Constants.ProductStockChangeType.TRADE);
            changeStockDto.setRetype(Constants.Retype.OUT);
            changeStockDto.setChangeSellCountFlag(false);
            changeStockDtoList.add(changeStockDto);
        }
        ApiResult stockResult = productFeign.batchChangeStock(changeStockDtoList);
        return stockResult;
    }


    /**
     * 订单支付
     *
     * @param orderPayDto
     * @return
     */
    public ApiResult<OrderPayVo> pay(OrderPayDto orderPayDto) {
        log.info("开始订单支付，orderPayDto={}", JSON.toJSONString(orderPayDto));
        long start = System.currentTimeMillis();
        // 业务校验
        FluentValidator fluentValidator = FluentValidator.checkAll()
                .on(orderPayDto.getOrderNo(), orderPayOrderValidator)
                .doValidate();
        ValidatorContext validatorContext = fluentValidator.getContext();
        Order order = validatorContext.getClazz("order");
        if (order == null) {
            throw new ServiceException("订单信息为空");
        }
        List<OrderProduct> orderProductList = orderProductService.findByOrderId(order.getId());
        if (CollectionUtils.isEmpty(orderProductList)) {
            throw new ServiceException("订单商品列表为空");
        }
        List<ChangeMultipleFundDto> changeMultipleFundDtoList = new ArrayList<>();
        // 扣除客户余额、消费积分
        BigDecimal moneyTotalAmount = BigDecimal.ZERO;
        //商城账户余额
        BigDecimal mallAccountTotalAmount = BigDecimal.ZERO;
        ChangeMultipleFundDto customerChangeMultipleFundDto = new ChangeMultipleFundDto();
        customerChangeMultipleFundDto.setCustomerNo(order.getCustomerNo());
        customerChangeMultipleFundDto.setOrderNo(order.getOrderNo());
        List<ChangeMultipleFundType> changeMultipleFundTypeList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProductList) {
            ChangeMultipleFundType fundType = new ChangeMultipleFundType();
            fundType.setFundType(orderProduct.getPayWay());
            fundType.setRetype(Constants.Retype.OUT);
            fundType.setChangeAmount(orderProduct.getTotalAmount());

            if (Constants.BeanType.MONEY.equals(orderProduct.getPayWay())) {
                moneyTotalAmount = moneyTotalAmount.add(orderProduct.getTotalAmount());
            }
            if (Constants.BeanType.MALLACCOUNT.equals(orderProduct.getPayWay())) {
                mallAccountTotalAmount = mallAccountTotalAmount.add(orderProduct.getTotalAmount());
            }
            if (Constants.BeanType.MONEY.equals(orderProduct.getPayWay())) {
                fundType.setTradeType(TradeMoneyTradeTypeEnum.CONSUMPTION_PAY.getCode());
            } else if (Constants.BeanType.CONSUMPTION.equals(orderProduct.getPayWay())) {
                fundType.setTradeType(RecordBeanTradeTypeEnum.CONSUMPTION_CONSUME_PAY.getCode());
            } else if (Constants.BeanType.MALLACCOUNT.equals(orderProduct.getPayWay())) {
                fundType.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_COMSUME_PAY.getCode());
            }
            changeMultipleFundTypeList.add(fundType);
        }
        customerChangeMultipleFundDto.setChangeMultipleFundTypeList(changeMultipleFundTypeList);
        changeMultipleFundDtoList.add(customerChangeMultipleFundDto);
        // 新增商城运营方余额
        if (moneyTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
            ChangeMultipleFundDto distributorChangeMultipleFundDto = new ChangeMultipleFundDto();
            distributorChangeMultipleFundDto.setCustomerNo(mallOperator);
            distributorChangeMultipleFundDto.setOrderNo(order.getOrderNo());
            List<ChangeMultipleFundType> distributorChangeMultipleFundTypeList = new ArrayList<>();
            ChangeMultipleFundType fundType = new ChangeMultipleFundType();
            fundType.setFundType(Constants.BeanType.MONEY);
            fundType.setRetype(Constants.Retype.IN);
            fundType.setChangeAmount(moneyTotalAmount);
            fundType.setTradeType(TradeMoneyTradeTypeEnum.SALE_INCOME.getCode());
            distributorChangeMultipleFundTypeList.add(fundType);
            distributorChangeMultipleFundDto.setChangeMultipleFundTypeList(distributorChangeMultipleFundTypeList);
            changeMultipleFundDtoList.add(distributorChangeMultipleFundDto);
        }

        // 新增商城运营方余额
        if (mallAccountTotalAmount.compareTo(BigDecimal.ZERO) > 0) {
            ChangeMultipleFundDto distributorChangeMultipleFundDto = new ChangeMultipleFundDto();
            distributorChangeMultipleFundDto.setCustomerNo(mallOperator);
            distributorChangeMultipleFundDto.setOrderNo(order.getOrderNo());
            List<ChangeMultipleFundType> distributorChangeMultipleFundTypeList = new ArrayList<>();
            ChangeMultipleFundType fundType = new ChangeMultipleFundType();
            fundType.setFundType(Constants.BeanType.MALLACCOUNT);
            fundType.setRetype(Constants.Retype.IN);
            fundType.setChangeAmount(mallAccountTotalAmount);
            fundType.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_SALE_INCOME.getCode());
            distributorChangeMultipleFundTypeList.add(fundType);
            distributorChangeMultipleFundDto.setChangeMultipleFundTypeList(distributorChangeMultipleFundTypeList);
            changeMultipleFundDtoList.add(distributorChangeMultipleFundDto);
        }

        ApiResult apiResult = accountFeign.changeMultipleFund(changeMultipleFundDtoList);
        if (apiResult.hasFail()) {
            log.info("扣除客户余额失败，apiResult={}", apiResult.toString());
            throw new ServiceException(apiResult.getMsg(), ResultEnum.BALANCE_NOT_ENOUGHT.getCode());
        }
        // 更新订单状态为待发货
        order.setStatus(Constants.MallOrderStatus.UNDELIVERY);
        order.setPayTime(new Date());
        order.setModifyTime(new Date());
        orderService.update(order);
        // 事务提交后执行
        afterOrderPay(order, orderProductList, moneyTotalAmount);
        OrderPayVo result = new OrderPayVo();
        result.setOrderNo(orderPayDto.getOrderNo());
        log.info("订单支付成功，耗时{}", (System.currentTimeMillis() - start));
        return ApiResult.success(result);
    }

    /**
     * 订单支付事务提交后执行逻辑
     *
     * @param order
     * @param orderProductList
     * @param finalMoneyTotalAmount
     */
    private void afterOrderPay(Order order, List<OrderProduct> orderProductList, BigDecimal finalMoneyTotalAmount) {
        transactionalCallbackService.execute(() -> {
            // 发送拆单消息
            rocketMQUtil.sendMsg(splitOrderTopics, order.getOrderNo(), order.getOrderNo());
            // 发送更新商品销量消息
            changeSellCount(orderProductList);
            // 发送资金清算消息
            settlementOrder(finalMoneyTotalAmount, order);
            // 向福清报送订单消息
            reportOrderPay(order, finalMoneyTotalAmount);
        });
    }

    /**
     * 异步更新销量
     *
     * @param orderProductList
     */
    private void changeSellCount(List<OrderProduct> orderProductList) {
        List<ChangeSellCountMessage> messageList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProductList) {
            ChangeSellCountMessage message = new ChangeSellCountMessage();
            message.setShelfId(orderProduct.getShelfId());
            message.setSkuId(orderProduct.getSkuId());
            message.setChangeAmount(new BigDecimal(orderProduct.getQuantity()));
            message.setRetype(Constants.Retype.IN);
            messageList.add(message);
        }
        // 发送消息至商品服务，增加商品销量
        rocketMQUtil.sendMsg(changeSellCountTopics, JSON.toJSONString(messageList), orderProductList.get(0).getOrderId().toString());
    }

    /**
     * 支付完成异步发送资金清算消息
     *
     * @param moneyTotalAmount
     * @param order
     */
    private void settlementOrder(BigDecimal moneyTotalAmount, Order order) {
        if (moneyTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("订单{}不涉及现金支付，无需发送资金清算消息", order.getOrderNo());
            return;
        }
        List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
        // 客户余额减少的清算消息
        SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
        customerMsg.setCustomerNo(order.getCustomerNo());
        customerMsg.setAmount(moneyTotalAmount);
        customerMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        customerMsg.setRetype(Constants.Retype.OUT);
        customerMsg.setOrderNo(order.getOrderNo());
        customerMsg.setEventTime(new Date());
        customerMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(customerMsg);
        // 商城运营方余额增加的清算消息
        SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
        distributorMsg.setCustomerNo(mallOperator);
        distributorMsg.setAmount(moneyTotalAmount);
        distributorMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        distributorMsg.setRetype(Constants.Retype.IN);
        distributorMsg.setOrderNo(order.getOrderNo());
        distributorMsg.setEventTime(new Date());
        distributorMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(distributorMsg);
        String msg = JacksonUtil.beanToJson(settlementMetadataMsgList);
        rocketMQUtil.sendMsg(settlementCleanTopics, msg, order.getOrderNo());
        log.info("订单支付发送资金清算消息成功，orderNo={},msg={}", order.getOrderNo(), msg);
    }

    /**
     * 向福清报送订单确认消息
     *
     * @param order
     * @param amount
     */
    public void reportOrderPay(Order order, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("订单{}不涉及现金支付，无需报送", order.getOrderNo());
            return;
        }
        OrderPay orderPay = new OrderPay();
        orderPay.setTradeOrderNo(order.getOrderNo());
        orderPay.setTradeAmount(amount.longValue() * 100);
        orderPay.setTradeNo(order.getOrderNo());
        orderPay.setFinishTime(DateUtil.yyyyMMddHHmmssNoLine.get().format(new Date()));
        String msg = JacksonUtil.beanToJson(orderPay);
        rocketMQUtil.sendMsg(orderPayTopics, msg, order.getOrderNo());
        log.info("订单支付发送福清支付确定消息成功，orderNo={},msg={}", order.getOrderNo(), msg);
    }

    /**
     * 确认收货
     *
     * @param orderNo
     * @return
     */
    public ApiResult confirmDelivery(String orderNo, String customerNo) {
        OrderItem orderItem = orderItemService.findByOrderItemNo(orderNo);
        if (orderItem == null) {
            return ApiResult.badParam("订单不存在");
        }
        if (!customerNo.equals(orderItem.getCustomerNo())) {
            log.info("订单归属不正确，customerNo={}，order.customerNo={}", customerNo, orderItem.getCustomerNo());
            return ApiResult.badParam("订单归属不正确");
        }
        if (!Constants.MallOrderStatus.DELIVERYED.equals(orderItem.getStatus())) {
            return ApiResult.badParam("订单状态不正确");
        }
        // 更新持仓明细状态
        Condition condition = new Condition(OrderItem.class);
        condition.createCriteria().andEqualTo("orderItemNo", orderNo)
                .andEqualTo("status", Constants.MallOrderStatus.DELIVERYED);
        OrderItem newOrderItem = new OrderItem();
        newOrderItem.setStatus(Constants.MallOrderStatus.COMPLETED);
        newOrderItem.setModifyTime(new Date());
        newOrderItem.setCompletedTime(new Date());
        boolean result = orderItemService.updateByConditionSelective(newOrderItem, condition);
        if (!result) {
            return ApiResult.error("更新收货状态失败");
        }
        // 订单明细全部确认收货需要更新主订单状态为已完成
        List<OrderItem> orderItemList = orderItemService.findByOrderId(orderItem.getOrderId());
        boolean flag = true;
        for (OrderItem item : orderItemList) {
            if (!Constants.MallOrderStatus.COMPLETED.equals(item.getStatus())) {
                flag = false;
            }
        }
        if (flag) {
            Order newOrder = new Order();
            newOrder.setId(orderItem.getOrderId());
            newOrder.setStatus(Constants.MallOrderStatus.COMPLETED);
            newOrder.setModifyTime(new Date());
            orderService.update(newOrder);
            // 订单确认收货后向福清报送修改订单状态消息
            Order order = orderService.findById(orderItem.getOrderId());
            OrderUpdate orderUpdate = new OrderUpdate();
            orderUpdate.setTradeOrderNo(order.getOrderNo());
            orderUpdate.setGoodsStatus("3");
            orderUpdate.setOccurTime(Integer.valueOf(DateUtil.HHmmssNoLine.get().format(new Date())));
            rocketMQUtil.sendMsg(orderUpdateTopics, JacksonUtil.beanToJson(orderUpdate), order.getOrderNo());
        }
        return ApiResult.success();
    }

    /**
     * 订单删除
     *
     * @param orderNo
     * @param customerNo
     * @return
     */
    public ApiResult delete(String orderNo, String customerNo) {
        Order order = orderService.findByOrderNo(orderNo);
        if (order == null) {
            OrderItem orderItem = orderItemService.findByOrderItemNo(orderNo);
            if (orderItem == null) {
                return ApiResult.badParam("订单不存在");
            }
            if (!customerNo.equals(orderItem.getCustomerNo())) {
                log.info("订单归属不正确，customerNo={}，order.customerNo={}", customerNo, orderItem.getCustomerNo());
                return ApiResult.badParam("订单归属不正确");
            }
            orderItemService.softDeleteById(orderItem.getId());
        } else {
            if (!customerNo.equals(order.getCustomerNo())) {
                log.info("订单归属不正确，customerNo={}，order.customerNo={}", customerNo, order.getCustomerNo());
                return ApiResult.badParam("订单归属不正确");
            }
            orderService.softDeleteById(order.getId());
        }
        return ApiResult.success();
    }
}