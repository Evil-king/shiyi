package com.baibei.shiyi.order.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.KeyValue;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.order.feign.base.message.SendIntegralMsg;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.model.OrderProduct;
import com.baibei.shiyi.order.service.IOrderItemService;
import com.baibei.shiyi.order.service.IOrderProductService;
import com.baibei.shiyi.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/7 17:41
 * @description: 订单拆单逻辑
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SplitOrderBiz {
    @Value("${rocketmq.order.sendIntegral.topics}")
    private String sendIntegralTopics;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderProductService orderProductService;
    @Autowired
    private IOrderItemService orderItemService;

    /**
     * 拆单
     * 拆单逻辑：拆单情况下（拆分到最小SKU，每件商品一个子订单），按单价，按类型排序，展开，先全额分摊金额小的商品，
     * 最后金额大的商品分摊最后的余额
     *
     * @param orderNo
     */
    public ApiResult splitOrder(String orderNo) {
        log.info("订单号为{}正在执行拆单", orderNo);
        // step1.校验订单数据正确性
        Order order = orderService.findByOrderNo(orderNo);
        if (order == null) {
            log.info("订单不存在，orderNo={}", orderNo);
            return ApiResult.success();
        }
        if (!Constants.MallOrderStatus.UNDELIVERY.equals(order.getStatus())) {
            log.info("订单状态为{}，不支持拆单操作", order.getStatus());
            return ApiResult.success();
        }
        List<OrderProduct> orderProductList = orderProductService.findByOrderId(order.getId());
        if (CollectionUtils.isEmpty(orderProductList)) {
            log.info("orderProductList不存在，orderNo={}", orderNo);
            return ApiResult.success();
        }
        // step3.拆单
        Integer quantity;
        // 按照不同商品循环遍历
        List<KeyValue> kvList = new ArrayList<>();
        for (OrderProduct item : orderProductList) {
            quantity = item.getQuantity();
            // 相同商品，按照商品数量最小单位1进行订单拆分
            for (int i = 0; i < quantity; i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(IdWorker.getId());
                orderItem.setOrderItemNo(NoUtil.getMallOrderNo());
                orderItem.setOrderId(item.getOrderId());
                orderItem.setCustomerNo(order.getCustomerNo());
                orderItem.setShelfId(item.getShelfId());
                orderItem.setSkuId(item.getSkuId());
                orderItem.setSkuProperty(item.getSkuProperty());
                orderItem.setProductName(item.getProductName());
                orderItem.setProductImg(item.getProductImg());
                orderItem.setAmount(item.getAmount());
                orderItem.setQuantity(1);
                orderItem.setStatus(Constants.MallOrderItemStatus.UNDELIVERY);
                orderItem.setCreateTime(new Date());
                orderItem.setModifyTime(new Date());
                orderItem.setFlag(Byte.valueOf(Constants.Flag.VALID));
                orderItem.setActualAmount(item.getTotalAmount().divide(BigDecimal.valueOf(item.getQuantity())));
                orderItem.setPayWay(item.getPayWay());
                orderItem.setShelfType(item.getShelfType());
                if (!StringUtils.isEmpty(item.getSendIntegralJson())) {
                    List<KeyValue> itemKvList = JSON.parseArray(item.getSendIntegralJson(), KeyValue.class);
                    if (!CollectionUtils.isEmpty(itemKvList)) {
                        kvList.addAll(itemKvList);
                    }
                }
                orderItem.setSendIntegralJson(item.getSendIntegralJson());
                orderItemService.save(orderItem);
            }
        }
        // 发送赠送积分消息
        if (!CollectionUtils.isEmpty(kvList)) {
            List<KeyValue> integralList = KeyValue.merge(kvList);
            List<KeyValue> trueIntegralList = new ArrayList<>();
            for (KeyValue keyValue : integralList) {
                if (new BigDecimal(keyValue.getValue()).compareTo(BigDecimal.ZERO)>0) {
                    trueIntegralList.add(keyValue);
                }
            }
            log.info("订单{}赠送积分，integralList={}", orderNo, JSON.toJSONString(trueIntegralList));
            SendIntegralMsg msg = new SendIntegralMsg();
            msg.setCustomerNo(order.getCustomerNo());
            msg.setOrderNo(orderNo);
            msg.setIntegralList(trueIntegralList);
            rocketMQUtil.sendMsg(sendIntegralTopics, JSON.toJSONString(msg), orderNo);
        }
        return ApiResult.success();
    }
}