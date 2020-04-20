package com.baibei.shiyi.cash.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.cash.dao.OrderDepositMapper;
import com.baibei.shiyi.cash.enumeration.OrderTypeEnum;
import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.model.OrderDeposit;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderDepositService;
import com.baibei.shiyi.cash.util.SerialNumberComponent;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/11/02 17:09:32
 * @description: OrderDeposit服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OrderDepositServiceImpl extends AbstractService<OrderDeposit> implements IOrderDepositService {

    @Autowired
    private OrderDepositMapper tblTraOrderDepositMapper;

    @Autowired
    private ISigningRecordFeign signingRecordFeign;

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    /**
     * rocketMQUtil
     */
    @Autowired
    private RocketMQUtil rocketMQUtil;


    //出金消息主题
    @Value("${rocketmq.deposit.topics}")
    private String depositTopic;

    //清算消息
    @Value("${rocketmq.settlement.clean.topics}")
    private String setCleanTopics;


    /**
     * 入金
     *
     * @param depositDto
     * @return
     */
    @Override
    public PABDepositVo deposit(PABDepositDto depositDto) {
        log.info("-----------------开始入金-------------------");
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByCustAcctId(depositDto.getCustAcctId());
        SigningRecordVo signingRecordVo;
        PABDepositVo pabDepositVo;
        if (signingRecordVoApiResult.getCode() != ResultEnum.SUCCESS.getCode()) {
            throw new ServiceException("会员子账号不存在");
        } else {
            signingRecordVo = signingRecordVoApiResult.getData();
            if (Constants.SigningStatus.SIGNING_DELETE.equals(signingRecordVo.getFuncFlag())) {
                throw new ServiceException("会员子账号已经签约");
            }
        }
        log.info("当前用户的信息为{}", JSON.toJSONString(signingRecordVo));
        OrderDeposit orderDeposit;
        String externalNo = depositDto.getExternalNo(); //银行流水号
        Condition condition = new Condition(OrderDeposit.class);
        condition.createCriteria().andEqualTo("externalNo", externalNo);
        List<OrderDeposit> resultList = this.findByCondition(condition);

        if (resultList.size() > 0) {
            log.info("当前订单{}已经存在,状态为", resultList.get(0), resultList.get(0).getStatus());
            if (resultList.get(0).getStatus().equals(Constants.SuccessOrFail.SUCCESS)) { //判断该笔订单是否成功
                log.info("该外部订单号已经成功入金不能重复入金");
                pabDepositVo = new PABDepositVo();
                pabDepositVo.setThirdLogNo(resultList.get(0).getOrderNo());
                return pabDepositVo;
            } else {
                orderDeposit = resultList.get(0);
            }
        } else {
            depositDto.setCustomerNo(signingRecordVo.getThirdCustId());//保存会员代码
            orderDeposit = toEntity(depositDto);
            String orderNO = serialNumberComponent.generateOrderNo(OrderDeposit.class, this, "D", "orderNo");
            orderDeposit.setId(IdWorker.getId());
            orderDeposit.setOrderNo(orderNO);
            orderDeposit.setExternalNo(externalNo);
            orderDeposit.setStatus(Constants.SuccessOrFail.SUCCESS);
            this.save(orderDeposit);
            log.info("新增入金订单为:[{}]", JSONObject.toJSONString(orderDeposit));
        }
        // stop  用mq发送增加账号余额的事务消息;
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(signingRecordVo.getThirdCustId());
        changeAmountDto.setReType(Constants.Retype.IN);
        changeAmountDto.setOrderNo(orderDeposit.getOrderNo());
        changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.RECHARGE.getCode());
        changeAmountDto.setChangeAmount(depositDto.getTranAmount());
        rocketMQUtil.sendMsg(depositTopic, JSON.toJSONString(changeAmountDto), orderDeposit.getOrderNo());
         // stop 发起清结算消息
        sendSettlement(orderDeposit);
        pabDepositVo = new PABDepositVo();
        pabDepositVo.setThirdLogNo(orderDeposit.getOrderNo());
        return pabDepositVo;
    }

    @Override
    public List<OrderDeposit> getPeriodOrderList(String batchNo) {
        return tblTraOrderDepositMapper.selectPeriodOrderList(batchNo);
    }

    /**
     * 发送消息
     */
    private void sendSettlement(OrderDeposit orderDeposit) {
        List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
        SettlementMetadataMsg settlementMetadataMsg = new SettlementMetadataMsg();
        settlementMetadataMsg.setOrderNo(orderDeposit.getOrderNo());
        settlementMetadataMsg.setAmount(orderDeposit.getOrderAmt());
        settlementMetadataMsg.setCustomerNo(orderDeposit.getCustomerNo());
       /* settlementMetadataMsg.setEffectField(Constants.SettlementEffectField.DEPOSIT_NEWBALANCE);*/
        settlementMetadataMsg.setEventTime(new Date());
        /*settlementMetadataMsg.setTradeType(TradeMoneyTradeTypeEnum.RECHARGE.getCode());*/
        settlementMetadataMsgList.add(settlementMetadataMsg);
        rocketMQUtil.sendMsg(setCleanTopics, JacksonUtil.beanToJson(settlementMetadataMsgList),orderDeposit.getOrderNo());
    }

    @Override
    public OrderDeposit getByOrderNo(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            throw new ServiceException("未指定入金单号");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<OrderDeposit> orderDepositList = tblTraOrderDepositMapper.selectByCondition(condition);
        if (orderDepositList.size() > 1) {
            throw new ServiceException("入金单号异常");
        }
        return orderDepositList.size() == 0 ? null : orderDepositList.get(0);
    }

    @Override
    public OrderDeposit getOrderByExternalNo(String externalNo) {
        Condition condition = new Condition(OrderDeposit.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(externalNo)) {
            criteria.andEqualTo("externalNo", externalNo);
        }
        List<OrderDeposit> orderDepositList = tblTraOrderDepositMapper.selectByCondition(condition);
        return orderDepositList.size() == 0 ? null : orderDepositList.get(0);
    }

    @Override
    public OrderDeposit getOrderByOrderNo(String orderNo) {
        Condition condition = new Condition(OrderDeposit.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(orderNo)) {
            criteria.andEqualTo("orderNo", orderNo);
        }
        List<OrderDeposit> orderDepositList = tblTraOrderDepositMapper.selectByCondition(condition);
        return orderDepositList.size() == 0 ? null : orderDepositList.get(0);
    }

    @Override
    public int safetyUpdateOrderBySelective(OrderDeposit updateEntity, String orderNo) {
        OrderDeposit orderDeposit = this.getByOrderNo(orderNo);
        if (StringUtils.isEmpty(orderDeposit)) {
            throw new ServiceException("查询不到指定入金单信息");
        }
        Condition condition = new Condition(OrderDeposit.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo", orderNo);
        criteria.andEqualTo("status", orderDeposit.getStatus());
        criteria.andEqualTo("orderAmt", orderDeposit.getOrderAmt());
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        updateEntity.setUpdateTime(new Date());
        return tblTraOrderDepositMapper.updateByConditionSelective(updateEntity, condition);
    }

    private OrderDeposit toEntity(PABDepositDto orderDepositDto) {
        OrderDeposit orderDeposit = new OrderDeposit();
        orderDeposit.setAccount(orderDepositDto.getInAcctId()); //入金账号
        orderDeposit.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType()); // 入金类型
        orderDeposit.setAccountName(orderDepositDto.getInAcctIdName()); // 入金账号名称
        orderDeposit.setCustomerNo(orderDepositDto.getCustomerNo()); // 会员代码
        orderDeposit.setOrderAmt(orderDepositDto.getTranAmount()); // 入金金额
        orderDeposit.setCreateTime(new Date()); // 创建时间
        orderDeposit.setFlag(new Byte(Constants.Flag.VALID)); // 未删除
        orderDeposit.setAcctDate(orderDepositDto.getAcctDate()); // 银行记账日期
        orderDeposit.setCcyCode(orderDepositDto.getCcyCode()); // 币种
        orderDeposit.setSupAcctId(orderDepositDto.getSupAcctId()); //资金汇总账号
        orderDeposit.setCustAcctId(orderDepositDto.getCustAcctId()); // 会员子账号
        return orderDeposit;
    }
}
