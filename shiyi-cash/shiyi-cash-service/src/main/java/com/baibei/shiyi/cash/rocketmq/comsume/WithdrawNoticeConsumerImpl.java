package com.baibei.shiyi.cash.rocketmq.comsume;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Longer
 * @date: 2019/12/04 14:24
 * @description: 银行出金异步结果通知处理逻辑
 */
@Component
@Slf4j
public class WithdrawNoticeConsumerImpl implements IConsumer<String> {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private PropertiesVal propertiesVal;
    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;
    @Value("${shiyi.fee.gainer}")
    private String nettingGainer;//轧差收益账户
    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public ApiResult execute(String msg) {
        log.info("出金结果返回：{}",msg);
        try{
            JSONObject jsonObject = JSONObject.parseObject(msg);
            if ("8".equals(jsonObject.getString("bankTransType"))) {//出金结果返回
                OrderWithdraw orderWithdraw = orderWithdrawService.getByOrderNo(jsonObject.getString("requestId"));
                if(StringUtils.isEmpty(orderWithdraw)){
                    log.info("找不到指定出金单号，单号："+jsonObject.getString("requestId"));
                    return ApiResult.error("找不到指定出金单号，单号："+jsonObject.getString("requestId"));
                }
                if(orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_DOING)){//处于处理中的订单(防止与主动查那边产生冲突)
                if("S".equals(jsonObject.getString("bankExecuteStatus"))){//出金成功
                    OrderWithdraw updateEntity = new OrderWithdraw();
                    String serialNo = jsonObject.getString("serialNo");//银行流水号
                    updateEntity.setExternalNo(serialNo);
                    updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
                    updateEntity.setFuqingWithdrawResp(msg);
                    int i = orderWithdrawService.safetyUpdateOrderBySelective2(updateEntity,orderWithdraw,orderWithdraw.getOrderNo());
                    if(i!=1){
                        log.info("出金结果返回：更新出金订单失败，单号为："+orderWithdraw.getOrderNo());
                        return ApiResult.error("出金结果返回：更新出金订单失败");
                    }
                    //发送清算消息
                    List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
                    // 客户余额减少的清算消息
                    SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
                    customerMsg.setCustomerNo(orderWithdraw.getCustomerNo());
                    customerMsg.setAmount(orderWithdraw.getHandelFee());
                    customerMsg.setTransferBizType(Constants.TransferBizType.FEE);
                    customerMsg.setRetype(Constants.Retype.OUT);
                    customerMsg.setOrderNo(orderWithdraw.getOrderNo());
                    customerMsg.setEventTime(new Date());
                    customerMsg.setApplicationName(applicationName);
                    settlementMetadataMsgList.add(customerMsg);
                    // 商城运营方余额增加的清算消息
                    SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
                    distributorMsg.setCustomerNo(nettingGainer);
                    distributorMsg.setAmount(orderWithdraw.getHandelFee());
                    distributorMsg.setTransferBizType(Constants.TransferBizType.FEE);
                    distributorMsg.setRetype(Constants.Retype.IN);
                    distributorMsg.setOrderNo(orderWithdraw.getOrderNo());
                    distributorMsg.setEventTime(new Date());
                    distributorMsg.setApplicationName(applicationName);
                    settlementMetadataMsgList.add(distributorMsg);
                    String message = JacksonUtil.beanToJson(settlementMetadataMsgList);
                    rocketMQUtil.sendMsg(settlementCleanTopics, message, orderWithdraw.getOrderNo());
                    log.info("订单支付发送资金清算消息成功，orderNo={},msg={}", orderWithdraw.getOrderNo(), msg);
                }else{//出金失败
                    OrderWithdraw updateEntity = new OrderWithdraw();
                    String serialNo = jsonObject.getString("serialNo");//银行流水号
                    updateEntity.setExternalNo(serialNo);
                    updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
                    updateEntity.setFuqingWithdrawResp(msg);
                    int i = orderWithdrawService.safetyUpdateOrderBySelective2(updateEntity,orderWithdraw,orderWithdraw.getOrderNo());
                    if(i!=1){
                        log.info("出金结果返回：更新出金订单失败，单号为："+orderWithdraw.getOrderNo());
                        return ApiResult.error("出金结果返回：更新出金订单失败");
                    }
                    //异步加钱
                    ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                    changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                    changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
                    changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
                    changeAmountDto.setReType(Constants.Retype.IN);
                    changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_BACK.getCode());
                    //发送加钱消息
                    rocketMQUtil.sendMsg(propertiesVal.getWithdrawAddMoneyTopic(),
                            JacksonUtil.beanToJson(changeAmountDto),changeAmountDto.getOrderNo());
                }
            }
            }
           return ApiResult.success();
        }catch (Exception e){
            log.error("WithdrawNoticeConsumerImpl消息执行报错：",e);
            return ApiResult.error("WithdrawNoticeConsumerImpl消息执行报错："+e);
        }
    }
}