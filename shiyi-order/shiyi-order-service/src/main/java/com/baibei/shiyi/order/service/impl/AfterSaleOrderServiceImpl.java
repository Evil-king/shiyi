package com.baibei.shiyi.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.CustomerBeanFeign;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.order.common.dto.*;
import com.baibei.shiyi.order.common.vo.*;
import com.baibei.shiyi.order.dao.AfterSaleOrderMapper;
import com.baibei.shiyi.order.feign.base.dto.AfterSalePageListDto;
import com.baibei.shiyi.order.feign.base.dto.ConfirmReceiptDto;
import com.baibei.shiyi.order.feign.base.dto.OperatorApplicationDto;
import com.baibei.shiyi.order.feign.base.vo.AfterSaleOrderVo;
import com.baibei.shiyi.order.model.*;
import com.baibei.shiyi.order.service.IAfterSaleGoodsService;
import com.baibei.shiyi.order.service.IAfterSaleOrderDetailsService;
import com.baibei.shiyi.order.service.IAfterSaleOrderService;
import com.baibei.shiyi.product.feign.base.shiyi.IProductBase;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/10/15 10:16:48
 * @description: AfterSaleOrder服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AfterSaleOrderServiceImpl extends AbstractService<AfterSaleOrder> implements IAfterSaleOrderService {

    @Autowired
    private AfterSaleOrderMapper aftersaleOrderMapper;
    @Autowired
    private IAfterSaleGoodsService afterSaleGoodsService;
    @Autowired
    private IAfterSaleOrderDetailsService afterSaleOrderDetailsService;
    @Autowired
    private AccountFeign mallAccountFeign;
    @Autowired
    private CustomerBeanFeign customerBeanFeign;
    @Value("${default.sendback.adress}")
    private String defaultSendBackAddress;
    @Autowired
    private IProductBase productBase;
    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;
    @Value("${shiyi.refund}")
    private String refund;
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Override
    public MyPageInfo<AfterSaleOrderVo> pageListData(AfterSalePageListDto afterSalePageListDto) {
        PageHelper.startPage(afterSalePageListDto.getCurrentPage(), afterSalePageListDto.getPageSize());
        List<AfterSaleOrderVo> afterSaleOrderVos = aftersaleOrderMapper.pageList(afterSalePageListDto);
        MyPageInfo<AfterSaleOrderVo> pageInfo = new MyPageInfo<>(afterSaleOrderVos);
        return pageInfo;
    }

    @Override
    public ApiResult createOrder(OrderItem byOrderItemNo, Order byOrderNo) {
        //先判断该用户是否有生成过售后单
        if(ifHasAfterSaleOrder(byOrderItemNo.getOrderItemNo(),byOrderItemNo.getCustomerNo()) == Boolean.FALSE){
            return ApiResult.error("已存在售后单");
        }
        String serverNo = String.valueOf(IdWorker.getId());
        //1、将子订单中的商品存放到售后订单商品表中，这个时候是没有售后类型的
        AfterSaleGoods afterSaleGoods = afterSaleGoodsService.operatorAfterSaleGoods(byOrderItemNo);
        //调用商品服务获取商品的编码
        ShelfRefDto shelfRefDto = new ShelfRefDto();
        shelfRefDto.setShelfId(byOrderItemNo.getShelfId());
        shelfRefDto.setSkuId(byOrderItemNo.getSkuId());
        ApiResult<BaseShelfVo> shelfInfo = productBase.getShelfInfo(shelfRefDto);
        if(shelfInfo.hasSuccess()){
            if(shelfInfo != null){
                afterSaleGoods.setSpuNo(shelfInfo.getData().getSpuNo());
            }
        } else {
            return ApiResult.error("调用商品服务获取商品编码失败");
        }
        afterSaleGoods.setServerNo(serverNo);
        if (ObjectUtils.isEmpty(afterSaleGoods)) {
            return ApiResult.error("售后商品对象为空");
        }
        if (afterSaleGoodsService.insertData(afterSaleGoods) < 1) {
            return ApiResult.error("插入售后商品表有误");
        }
        //2、生成售后订单
        AfterSaleOrder afterSaleOrder = createAfterSaleOrder(byOrderItemNo, byOrderNo.getOrderNo());
        afterSaleOrder.setServerNo(serverNo);
        if (ObjectUtils.isEmpty(afterSaleOrder)) {
            return ApiResult.error("售后订单对象为空");
        }
        if (aftersaleOrderMapper.insert(afterSaleOrder) < 1) {
            return ApiResult.error("插入售后商品表有误");
        }
        //3、生成售后订单详情
        AfterSaleOrderDetails afterSaleOrderDetails = afterSaleOrderDetailsService.
                createAfterSaleOrderDetails(byOrderItemNo.getOrderItemNo(), serverNo);
        afterSaleOrderDetails.setReceivername(byOrderNo.getReceiverName());
        afterSaleOrderDetails.setReceiverphone(byOrderNo.getReceiverPhone());
        afterSaleOrderDetails.setRefuseAccount(byOrderItemNo.getPayWay());
        afterSaleOrderDetails.setReissueAddress(byOrderNo.getReceiverProvince() + byOrderNo.getReceiverCity() +
                byOrderNo.getReceiverRegion() + byOrderNo.getReceiverDetailAddress());

        BigDecimal actualMoney = byOrderItemNo.getActualAmount();
        if("money".equals(byOrderItemNo.getPayWay())){
            afterSaleOrderDetails.setRefuseMoney(actualMoney);
        }
        if("consumption".equals(byOrderItemNo.getPayWay())){
            afterSaleOrderDetails.setRefuseIntegral(actualMoney);
        }
        afterSaleOrderDetails.setCustomerNo(byOrderItemNo.getCustomerNo());
        if (ObjectUtils.isEmpty(afterSaleOrderDetails)) {
            return ApiResult.error("售后订单详情对象为空");
        }
        afterSaleOrder.setServerNo(afterSaleOrderDetails.getServerNo());
        if (afterSaleOrderDetailsService.insertData(afterSaleOrderDetails) < 1) {
            return ApiResult.error("插入售后订单详情表有误");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult<List<AfterSaleOrderVo>> exportData(AfterSalePageListDto afterSalePageListDto) {
        List<AfterSaleOrderVo> afterSaleOrderVos = aftersaleOrderMapper.exportData(afterSalePageListDto);
        return ApiResult.success(afterSaleOrderVos);
    }

    @Override
    public String submitApplication(SubmitApplicationDto submitApplicationDto) {
        log.info("submitApplicationDto={}", JSONObject.toJSONString(submitApplicationDto));
        AfterSaleOrderDetails afterSaleOrderDetails = new AfterSaleOrderDetails();
        afterSaleOrderDetails.setOrderItemNo(submitApplicationDto.getOrderItemNo());
        afterSaleOrderDetails.setPhoto(submitApplicationDto.getPhoto());
        afterSaleOrderDetails.setProblemdescription(submitApplicationDto.getProblemDescription());
        afterSaleOrderDetails.setReturnreasons(submitApplicationDto.getReturnReasons());
        if("refund".equals(submitApplicationDto.getType())){
            afterSaleOrderDetails.setRefuseMoney(new BigDecimal(submitApplicationDto.getRefuseMoney()));
            afterSaleOrderDetails.setRefuseIntegral(new BigDecimal(submitApplicationDto.getRefuseIntegral()));
            afterSaleOrderDetails.setCustomerNo(submitApplicationDto.getCustomerNo());
        }
        //1、先更新售后订单
        AfterSaleOrder afterSaleOrder = queryByParams(submitApplicationDto.getOrderItemNo());
        afterSaleOrder.setType(submitApplicationDto.getType());
        afterSaleOrder.setStatus("waiting");
        afterSaleOrder.setApplicationTime(new Date());//申请时间
        afterSaleOrder.setModifyTime(new Date());
        if (aftersaleOrderMapper.updateByPrimaryKeySelective(afterSaleOrder) < 1) {
            return "fail";
        }
        //2、更新售后订单商品类型
        if (afterSaleGoodsService.updateByOrderItemNo(submitApplicationDto.getOrderItemNo(), submitApplicationDto.getType()) < 1) {
            return "fail";
        }
        //3、再更新售后订单详情
        if (afterSaleOrderDetailsService.updateAfterSaleOrderDetails(afterSaleOrderDetails) > 0) {
            return "success";
        }
        return "fail";
    }

    @Override
    public AfterSaleDetailsVo details(String orderItemNo) {
        AfterSaleDetailsVo afterSaleDetailsVo = new AfterSaleDetailsVo();
        AfterSaleOrderDetailsVo afterSaleOrderDetailsVo = new AfterSaleOrderDetailsVo();
        List<AfterSaleGoodsVo> afterSaleGoodsVoList = Lists.newArrayList();

        AfterSaleGoods afterSaleGoods = afterSaleGoodsService.selectByOrderItemNo(orderItemNo);
        afterSaleGoods.setSkuproperty(SkuPropertyUtil.getSkuPropertyValue(afterSaleGoods.getSkuproperty()));
        AfterSaleGoodsVo afterSaleGoodsVo = BeanUtil.copyProperties(afterSaleGoods, AfterSaleGoodsVo.class);
        afterSaleGoodsVo.setTotalAmount(afterSaleGoodsVo.getAmount().multiply(new BigDecimal(afterSaleGoodsVo.getQuantity())));
        afterSaleGoodsVoList.add(afterSaleGoodsVo);
        afterSaleDetailsVo.setAfterSaleGoodsVoList(afterSaleGoodsVoList);

        AfterSaleOrder afterSaleOrder = queryByParams(orderItemNo);

        afterSaleOrderDetailsVo.setType(afterSaleOrder.getType());
        afterSaleOrderDetailsVo.setStatus(afterSaleOrder.getStatus());
        afterSaleOrderDetailsVo.setServerNo(afterSaleOrder.getServerNo());
        afterSaleOrderDetailsVo.setOrderItemNo(orderItemNo);
        afterSaleOrderDetailsVo.setCustomerNo(afterSaleOrder.getCustomerNo());

        AfterSaleOrderDetails afterSaleOrderDetails = afterSaleOrderDetailsService.selectByOrderItemNo(orderItemNo);
        afterSaleOrderDetailsVo.setReceiverPhone(afterSaleOrderDetails.getReceiverphone());
        afterSaleOrderDetailsVo.setReturnReasons(afterSaleOrderDetails.getReturnreasons());
        afterSaleOrderDetailsVo.setProblemDescription(afterSaleOrderDetails.getProblemdescription());
        afterSaleOrderDetailsVo.setPhoto(afterSaleOrderDetails.getPhoto());
        if("money".equals(afterSaleOrderDetails.getRefuseAccount())){
            afterSaleOrderDetailsVo.setConfirmAmount(afterSaleOrderDetails.getRefuseMoney());
            afterSaleOrderDetailsVo.setRefuseMoney(afterSaleOrderDetails.getRefuseMoney());
        }
        if("consumption".equals(afterSaleOrderDetails.getRefuseAccount())){
            afterSaleOrderDetailsVo.setConfirmAmount(afterSaleOrderDetails.getRefuseIntegral());
            afterSaleOrderDetailsVo.setRefuseMoney(afterSaleOrderDetails.getRefuseIntegral());
        }
        afterSaleOrderDetailsVo.setRefuseAccount(afterSaleOrderDetails.getRefuseAccount());
        afterSaleOrderDetailsVo.setRefuseIntegral(afterSaleOrderDetails.getRefuseIntegral());
        afterSaleOrderDetailsVo.setRemark(afterSaleOrderDetails.getRemark());
        afterSaleOrderDetailsVo.setSendBackAddress(afterSaleOrderDetails.getSendbackAddress());
        afterSaleOrderDetailsVo.setCreateTime(afterSaleOrderDetails.getCreateTime());
        afterSaleOrderDetailsVo.setSendLogisticsName(afterSaleOrderDetails.getSendLogisticsName());
        afterSaleOrderDetailsVo.setSendLogisticsNo(afterSaleOrderDetails.getSendLogisticsNo());

        afterSaleDetailsVo.setAfterSaleOrderDetailsVo(afterSaleOrderDetailsVo);

        return afterSaleDetailsVo;
    }

    @Override
    public ApiResult operatorApplication(OperatorApplicationDto operatorApplicationDto) {
        log.info("operatorApplicationDto={}", JSONObject.toJSONString(operatorApplicationDto));
        //查询订单详细获取信息
        AfterSaleOrderDetails afterSaleOrderDetails = afterSaleOrderDetailsService.
                selectByOrderItemNo(operatorApplicationDto.getOrderItemNo());
        //1、更新售后订单状态
        Condition condition = new Condition(AfterSaleOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("serverNo", operatorApplicationDto.getServerNo());
        criteria.andEqualTo("orderItemNo", operatorApplicationDto.getOrderItemNo());
        criteria.andEqualTo("status", "waiting");
        List<AfterSaleOrder> afterSaleOrders = aftersaleOrderMapper.selectByCondition(condition);

        if (!CollectionUtils.isEmpty(afterSaleOrders)) {
            AfterSaleOrder afterSaleOrder = afterSaleOrders.get(0);
            afterSaleOrder.setModifyTime(new Date());
            if ("success".equals(operatorApplicationDto.getStatus())) {
                afterSaleOrder.setSuccessTime(new Date());
                afterSaleOrder.setStatus(operatorApplicationDto.getStatus());
                if (aftersaleOrderMapper.updateByPrimaryKeySelective(afterSaleOrder) > 0) {
                    //2、更新售后订单详情的退款金额
                    if("money".equals( afterSaleOrderDetails.getRefuseAccount())){
                        afterSaleOrderDetails.setRefuseMoney(new BigDecimal(operatorApplicationDto.getConfirmAmount()));
                        if (afterSaleOrderDetailsService.updateAfterSaleOrderDetails(operatorApplicationDto.getServerNo(),
                                afterSaleOrderDetails.getRefuseMoney(), operatorApplicationDto.getSendBackAddress(), operatorApplicationDto.getRemark()) > 0) {
                            return ApiResult.success();
                        }
                    }
                    if("consumption".equals(afterSaleOrderDetails.getRefuseAccount())){
                        afterSaleOrderDetails.setRefuseIntegral(new BigDecimal(operatorApplicationDto.getConfirmAmount()));
                        if (afterSaleOrderDetailsService.updateAfterSaleOrderDetails(operatorApplicationDto.getServerNo(),
                                afterSaleOrderDetails.getRefuseIntegral(), operatorApplicationDto.getSendBackAddress(), operatorApplicationDto.getRemark()) > 0) {
                            return ApiResult.success();
                        }
                    }
                }
            } else {
                afterSaleOrder.setStatus(operatorApplicationDto.getStatus());
                if (aftersaleOrderMapper.updateByPrimaryKeySelective(afterSaleOrder) > 0) {
                    //更新详情
                    if("money".equals( afterSaleOrderDetails.getRefuseAccount())){
                        afterSaleOrderDetails.setRefuseMoney(new BigDecimal(operatorApplicationDto.getConfirmAmount()));
                        if (afterSaleOrderDetailsService.updateAfterSaleOrderDetails(operatorApplicationDto.getServerNo(),
                                afterSaleOrderDetails.getRefuseMoney(), operatorApplicationDto.getSendBackAddress(), operatorApplicationDto.getRemark()) > 0) {
                            return ApiResult.success();
                        }
                    }
                    if("consumption".equals(afterSaleOrderDetails.getRefuseAccount())){
                        afterSaleOrderDetails.setRefuseIntegral(new BigDecimal(operatorApplicationDto.getConfirmAmount()));
                        if (afterSaleOrderDetailsService.updateAfterSaleOrderDetails(operatorApplicationDto.getServerNo(),
                                afterSaleOrderDetails.getRefuseIntegral(), operatorApplicationDto.getSendBackAddress(), operatorApplicationDto.getRemark()) > 0) {
                            return ApiResult.success();
                        }
                    }
                }
            }
        }
        return ApiResult.error();
    }

    @Override
    public ApiResult confirmReceipt(ConfirmReceiptDto confirmReceiptDto) {
        log.info("confirmReceiptDto={}", JSONObject.toJSONString(confirmReceiptDto));
        if ("refund".equals(confirmReceiptDto.getType())) {
            //退款操作
            AfterSaleOrderDetails afterSaleOrderDetails =
                    afterSaleOrderDetailsService.selectByServerNo(confirmReceiptDto.getServerNo());
            // 调用账户服务扣减余额
            ApiResult accountApiResult = null;
            BigDecimal moneyAmount = null;
            if("money".equals(afterSaleOrderDetails.getRefuseAccount())){
                moneyAmount = afterSaleOrderDetails.getRefuseMoney();

                List<ChangeAmountDto> changeAmountDtos = Lists.newArrayList();

                ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                changeAmountDto.setCustomerNo(confirmReceiptDto.getCustomerNo());
                changeAmountDto.setOrderNo(confirmReceiptDto.getServerNo());
                changeAmountDto.setReType(Constants.Retype.IN);
                changeAmountDto.setChangeAmount(moneyAmount);
                changeAmountDto.setTradeType(String.valueOf(TradeMoneyTradeTypeEnum.REFUND_INCOME.getCode()));
                changeAmountDtos.add(changeAmountDto);
                //扣减指定退款账户的钱
                ChangeAmountDto changeAmountDto1 = new ChangeAmountDto();
                changeAmountDto1.setCustomerNo(refund);
                changeAmountDto1.setOrderNo(confirmReceiptDto.getServerNo());
                changeAmountDto1.setReType(Constants.Retype.OUT);
                changeAmountDto1.setChangeAmount(moneyAmount);
                changeAmountDto1.setTradeType(String.valueOf(TradeMoneyTradeTypeEnum.REFUND_PAY.getCode()));
                changeAmountDtos.add(changeAmountDto1);
                accountApiResult = mallAccountFeign.changeMoneyList(changeAmountDtos);
                log.info("accountApiResult={}", JSONObject.toJSONString(accountApiResult));
            }
            if("consumption".equals(afterSaleOrderDetails.getRefuseAccount())){
                BigDecimal amount = afterSaleOrderDetails.getRefuseIntegral();
                ChangeCustomerBeanDto changeCustomerBeanDto = new ChangeCustomerBeanDto();
                changeCustomerBeanDto.setChangeAmount(amount);
                changeCustomerBeanDto.setReType(Constants.Retype.IN);
                changeCustomerBeanDto.setTradeType(String.valueOf(RecordBeanTradeTypeEnum.CONSUMPTION_RETURN_INCOME.getCode()));
                changeCustomerBeanDto.setCustomerNo(confirmReceiptDto.getCustomerNo());
                changeCustomerBeanDto.setOrderNo(confirmReceiptDto.getServerNo());
                changeCustomerBeanDto.setCustomerBeanType(Constants.BeanType.CONSUMPTION);
                 accountApiResult = customerBeanFeign.changeAmount(changeCustomerBeanDto);
                log.info("accountApiResult={}", JSONObject.toJSONString(accountApiResult));
            }
            if (accountApiResult.hasSuccess()) {
                //更新售后订单状态
                if (aftersaleOrderMapper.updateByParamsRefunded(confirmReceiptDto.getServerNo(), "refunded") > 0) {
                    if ("money".equals(afterSaleOrderDetails.getRefuseAccount())) {
                        //更新成功后通知清算服务
                        List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
                        //客户余额增加变动
                        SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
                        customerMsg.setCustomerNo(confirmReceiptDto.getCustomerNo());
                        customerMsg.setAmount(moneyAmount);
                        customerMsg.setTransferBizType(Constants.TransferBizType.TRADE);
                        customerMsg.setRetype(Constants.Retype.IN);
                        customerMsg.setOrderNo(confirmReceiptDto.getServerNo());
                        customerMsg.setEventTime(new Date());
                        customerMsg.setApplicationName(applicationName);
                        settlementMetadataMsgList.add(customerMsg);
                        //系统退款账户余额减少变动
                        SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
                        distributorMsg.setCustomerNo(refund);
                        distributorMsg.setAmount(moneyAmount);
                        distributorMsg.setTransferBizType(Constants.TransferBizType.TRADE);
                        distributorMsg.setRetype(Constants.Retype.OUT);
                        distributorMsg.setOrderNo(confirmReceiptDto.getServerNo());
                        distributorMsg.setEventTime(new Date());
                        distributorMsg.setApplicationName(applicationName);
                        settlementMetadataMsgList.add(distributorMsg);

                        String msg = JacksonUtil.beanToJson(settlementMetadataMsgList);
                        rocketMQUtil.sendMsg(settlementCleanTopics, msg, confirmReceiptDto.getServerNo());
                        log.info("订单{}发送资金清算消息成功，orderNo={}", confirmReceiptDto.getServerNo());
                        return ApiResult.success();
                    }
                    return ApiResult.success();
                }
            } else {
                log.info("服务号为{}，余额支付失败，accountApiResult={}", confirmReceiptDto.getServerNo(), accountApiResult.toString());
                return ApiResult.error("余额支付失败");
            }
        }
        if ("exchange".equals(confirmReceiptDto.getType())) {
            //更新售后订单状态
            if (aftersaleOrderMapper.updateByParamsReissued(confirmReceiptDto.getServerNo(), "reissued") > 0) {
                //换货操作
                if (afterSaleOrderDetailsService.updateByServerNoAndLogistics(confirmReceiptDto.getServerNo(),
                        confirmReceiptDto.getLogisticsName(), confirmReceiptDto.getLogisticsNo()) > 0) {
                    return ApiResult.success();
                }
            }
        }
        return ApiResult.error();
    }

    @Override
    public MyPageInfo<ApiAfterSalePageListVo> apiPageList(ApiAfterSalePageListDto apiAfterSalePageListDto) {
        MyPageInfo<ApiAfterSalePageListVo> afterSaleGoods = afterSaleGoodsService.selectByOrderItemNoTypeIsNotNull(apiAfterSalePageListDto.getCustomerNo(),
                apiAfterSalePageListDto.getCurrentPage(), apiAfterSalePageListDto.getPageSize());
        for (int i = 0; i < afterSaleGoods.getList().size(); i++) {
            afterSaleGoods.getList().get(i).setSkuProperty(SkuPropertyUtil.getSkuPropertyValue(afterSaleGoods.getList().get(i).getSkuProperty()));
            if("refund".equals(afterSaleGoods.getList().get(i).getType())){
                afterSaleGoods.getList().get(i).setTypeRemark("退款");
            }
            if("exchange".equals(afterSaleGoods.getList().get(i).getType())){
                afterSaleGoods.getList().get(i).setTypeRemark("换货");
            }
        }
        return afterSaleGoods;

    }

    @Override
    public ApiResult cancelApplication(ApiCancelApplicationDto apiCancelApplicationDto) {
        log.info("apiCancelApplicationDto={}", JSONObject.toJSONString(apiCancelApplicationDto));
        AfterSaleOrder afterSaleOrder = aftersaleOrderMapper.selectByOrderItemNoToWaiting(apiCancelApplicationDto.getOrderItemNo());
        if (afterSaleOrder != null) {
            afterSaleOrder.setStatus("revoked");
            afterSaleOrder.setRevokedTime(new Date());
            if (aftersaleOrderMapper.updateByPrimaryKeySelective(afterSaleOrder) > 0) {
                return ApiResult.success();
            }
        }
        return ApiResult.error();
    }

    public AfterSaleOrder createAfterSaleOrder(OrderItem byOrderItemNo, String orderNo) {
        AfterSaleOrder afterSaleOrder = new AfterSaleOrder();
        afterSaleOrder.setId(IdWorker.getId());
        afterSaleOrder.setOrderNo(orderNo);
        afterSaleOrder.setStatus("init");
        afterSaleOrder.setCustomerNo(byOrderItemNo.getCustomerNo());
        afterSaleOrder.setOrderItemNo(byOrderItemNo.getOrderItemNo());
        afterSaleOrder.setTotalAmount(byOrderItemNo.getAmount().multiply(new BigDecimal(byOrderItemNo.getQuantity())));
        afterSaleOrder.setFlag((byte) 1);
        afterSaleOrder.setCreateTime(new Date());
        return afterSaleOrder;
    }

    public AfterSaleOrder queryByParams(String orderItemNo) {
        Condition condition = new Condition(AfterSaleOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", orderItemNo);
        List<AfterSaleOrder> afterSaleOrders = aftersaleOrderMapper.selectByCondition(condition);
        return afterSaleOrders.get(0);
    }

    public AfterSaleOrder queryByOrderItemNoAndTypeAndCustomerNo(String orderItemNo, String type, String customerNo, String signer) {
        Condition condition = new Condition(AfterSaleOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", orderItemNo);
        if("details".equals(signer)){
            criteria.andEqualTo("type", type);
        }
        criteria.andEqualTo("customerNo", customerNo);
        List<AfterSaleOrder> afterSaleOrders = aftersaleOrderMapper.selectByCondition(condition);
        if(CollectionUtils.isEmpty(afterSaleOrders)){
            return new AfterSaleOrder();
        }
        return afterSaleOrders.get(0);
    }

    @Override
    public boolean whetherToOpenAfterSaleOrder(String orderItemNo, String customerNo) {
        if (aftersaleOrderMapper.whetherToOpenAfterSaleOrder(orderItemNo, customerNo) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean ifHasAfterSaleOrder(String orderItemNo, String customerNo) {
        if (aftersaleOrderMapper.whetherToOpenAfterSaleOrder(orderItemNo, customerNo) >= 1) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public ApiResult apiAfterSaleDetails(ApiAfterSaleDetailsDto apiAfterSaleDetailsDto) {
        log.info("apiAfterSaleDetailsDto={}", JSONObject.toJSONString(apiAfterSaleDetailsDto));
        ApiAfterSaleDetailsVo apiAfterSaleDetailsVo = new ApiAfterSaleDetailsVo();
        //组装售后商品信息
        AfterSaleGoods afterSaleGoods = afterSaleGoodsService.selectByOrderItemNoAndType(apiAfterSaleDetailsDto.getOrderItemNo(),
                apiAfterSaleDetailsDto.getType(),apiAfterSaleDetailsDto.getSigner());
        if(afterSaleGoods == null){
            log.info("orderItemNo={}", apiAfterSaleDetailsDto.getOrderItemNo());
            return ApiResult.error("商品信息不存在");
        }
        //根据订单状态组装msg
        AfterSaleOrder afterSaleOrder = queryByOrderItemNoAndTypeAndCustomerNo(apiAfterSaleDetailsDto.getOrderItemNo(),
                apiAfterSaleDetailsDto.getType(), apiAfterSaleDetailsDto.getCustomerNo(),apiAfterSaleDetailsDto.getSigner());
        if(afterSaleOrder == null){
            log.info("orderItemNo={},type={},customerNo={},singer={}", apiAfterSaleDetailsDto.getOrderItemNo(),apiAfterSaleDetailsDto.getType(),
                    apiAfterSaleDetailsDto.getCustomerNo(),apiAfterSaleDetailsDto.getSigner());
            return ApiResult.error("售后订单信息不存在");
        }
        //查看订单详情
        AfterSaleOrderDetails afterSaleOrderDetails = afterSaleOrderDetailsService.selectByOrderItemNo(apiAfterSaleDetailsDto.getOrderItemNo());
        if (afterSaleOrderDetails == null) {
            log.info("orderItemNo={}", apiAfterSaleDetailsDto.getOrderItemNo());
            return ApiResult.error("售后订单详情信息不存在");
        }
        if ("refund".equals(apiAfterSaleDetailsDto.getType())) { //退款
            apiAfterSaleDetailsVo.setImage(afterSaleGoods.getImage());
            apiAfterSaleDetailsVo.setName(afterSaleGoods.getName());
            apiAfterSaleDetailsVo.setOrderItemNo(afterSaleGoods.getOrderItemNo());
            apiAfterSaleDetailsVo.setQuantity(afterSaleGoods.getQuantity());
            apiAfterSaleDetailsVo.setSkuproperty(SkuPropertyUtil.getSkuPropertyValue(afterSaleGoods.getSkuproperty()));

            apiAfterSaleDetailsVo.setRefuseAccount(afterSaleOrderDetails.getRefuseAccount());
            if("money".equals(afterSaleOrderDetails.getRefuseAccount())){
                apiAfterSaleDetailsVo.setRefuseIntegral(new BigDecimal(0));
                apiAfterSaleDetailsVo.setRefuseMoney(afterSaleOrderDetails.getRefuseMoney());
                apiAfterSaleDetailsVo.setRefuseAccountRemark("现金余额");
            }
            if("consumption".equals(afterSaleOrderDetails.getRefuseAccount())){
                apiAfterSaleDetailsVo.setRefuseIntegral(afterSaleOrderDetails.getRefuseIntegral());
                apiAfterSaleDetailsVo.setRefuseMoney(new BigDecimal(0));
                apiAfterSaleDetailsVo.setRefuseAccountRemark("消费积分");
            }
            apiAfterSaleDetailsVo.setFinishTime(afterSaleOrder.getFinishTime());
        }
        if ("exchange".equals(apiAfterSaleDetailsDto.getType())) { //换货
            apiAfterSaleDetailsVo.setImage(afterSaleGoods.getImage());
            apiAfterSaleDetailsVo.setName(afterSaleGoods.getName());
            apiAfterSaleDetailsVo.setOrderItemNo(afterSaleGoods.getOrderItemNo());
            apiAfterSaleDetailsVo.setQuantity(afterSaleGoods.getQuantity());
            apiAfterSaleDetailsVo.setSkuproperty(SkuPropertyUtil.getSkuPropertyValue(afterSaleGoods.getSkuproperty()));
            apiAfterSaleDetailsVo.setFinishTime(afterSaleOrder.getFinishTime());
        }
        if ("success".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("平台已经同意退货,请将商品寄回以下地址");
            if("".equals(afterSaleOrderDetails.getSendbackAddress()) || "null".equals(afterSaleOrderDetails.getSendbackAddress())){
                apiAfterSaleDetailsVo.setMsg("邮寄地址:"+defaultSendBackAddress);
            } else {
                apiAfterSaleDetailsVo.setMsg("邮寄地址:"+afterSaleOrderDetails.getSendbackAddress());
            }
        }
        if ("doing".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("请等待平台收货");
        }
        if ("refunded".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("平台已收货并退款");
            apiAfterSaleDetailsVo.setMsg("您的退款将于1-7个工作日退回给您支付账户");
        }
        if ("reissued".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("平台已收货并补发");
            apiAfterSaleDetailsVo.setMsg("物流: "+afterSaleOrderDetails.getLogisticsName()+" 单号:"+afterSaleOrderDetails.getSendLogisticsNo());
        }
        if ("waiting".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("请等待平台处理");
        }
        if ("revoked".equals(afterSaleOrder.getStatus())) {
            apiAfterSaleDetailsVo.setStatus(afterSaleOrder.getStatus());
            apiAfterSaleDetailsVo.setStatusRemark("撤销申请");
        }
        if("send_integral".equals(afterSaleGoods.getShelfType())){
            apiAfterSaleDetailsVo.setShelfType(afterSaleGoods.getShelfType());
            apiAfterSaleDetailsVo.setShelfTypeRemark("赠送积分商品");
        }
        if("consume_ingtegral".equals(afterSaleGoods.getShelfType())){
            apiAfterSaleDetailsVo.setShelfType(afterSaleGoods.getShelfType());
            apiAfterSaleDetailsVo.setShelfTypeRemark("交割商品");
        }
        if("transfer_product".equals(afterSaleGoods.getShelfType())){
            apiAfterSaleDetailsVo.setShelfType(afterSaleGoods.getShelfType());
            apiAfterSaleDetailsVo.setShelfTypeRemark("消费积分商品");
        }
        apiAfterSaleDetailsVo.setAmount(afterSaleGoods.getAmount());
        apiAfterSaleDetailsVo.setApplicationTime(afterSaleOrder.getApplicationTime());
        apiAfterSaleDetailsVo.setDoingTime(afterSaleOrder.getDoingTime());
        apiAfterSaleDetailsVo.setSuccessTime(afterSaleOrder.getSuccessTime());
        apiAfterSaleDetailsVo.setRevokedTime(afterSaleOrder.getRevokedTime());
        return ApiResult.success(apiAfterSaleDetailsVo);
    }

    @Override
    public String selectByOrderItemNoToStatus(String orderItemNo, String customerNo) {
        return aftersaleOrderMapper.selectByOrderItemNoToStatus(orderItemNo,customerNo);
    }

    @Override
    public ApiResult shipping(ShippingDto shippingDto) {
        AfterSaleOrder afterSaleOrder = aftersaleOrderMapper.selectByOrderItemNoToStatusObject(shippingDto.getOrderItemNo());
        afterSaleOrder.setDoingTime(new Date());
        afterSaleOrder.setStatus("doing");
        if(aftersaleOrderMapper.updateByPrimaryKeySelective(afterSaleOrder) > 0){
            if(afterSaleOrderDetailsService.updateByOrderItemNoAndSendLogistics(shippingDto.getOrderItemNo(),
                    shippingDto.getSendLogisticsNo(),
                    shippingDto.getSendLogisticsName()) > 0){
                return ApiResult.success();
            }
        }
        return ApiResult.error();
    }

}
