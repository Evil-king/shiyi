package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.HoldResourceEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.SystemException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.dao.TransferDetailsMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.HoldPosition;
import com.baibei.shiyi.trade.model.TransferDetails;
import com.baibei.shiyi.trade.service.*;
import com.baibei.shiyi.trade.utils.TradeUtil;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/12/26 14:21:39
 * @description: TransferDetails服务实现
 */
@Slf4j
@Service
public class TransferDetailsServiceImpl extends AbstractService<TransferDetails> implements ITransferDetailsService {

    @Autowired
    private TransferDetailsMapper transferDetailsMapper;
    @Autowired
    private IFeeExemptionConfigService feeExemptionConfigService;
    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private ITransferLogRecordService transferLogRecordService;
    @Autowired
    private CustomerFeign customerFeign;
    @Autowired
    private IHoldPostionChangeService holdPostionChangeService;
    @Autowired
    private IHoldPositionService holdPositionService;


    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;
    @Value("${spring.application.name}")
    private String applicationName;
    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyPageInfo<TransferPageListVo> listPage(TransferPageListDto transferPageListDto) {

        MyPageInfo<TransferPageListVo> myPageInfo = null;
        if ("wait".equals(transferPageListDto.getType())) {
            myPageInfo = transferLogRecordService.listPage(transferPageListDto);
        }
        if ("execute".equals(transferPageListDto.getType())) {
            PageHelper.startPage(transferPageListDto.getCurrentPage(), transferPageListDto.getPageSize());
            List<TransferPageListVo> transferPageListVos = transferDetailsMapper.listPage(transferPageListDto);
            myPageInfo = new MyPageInfo<>(transferPageListVos);
        }
        return myPageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TransferPageListVo> export(TransferPageListDto transferPageListDto) {
        List<TransferPageListVo> transferPageListVos = transferDetailsMapper.listPage(transferPageListDto);
        return transferPageListVos;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public ApiResult transferData(TransferTemplateVo transferTemplateVo, long transferLogId, String orderNo,
                                  BigDecimal buyFee, BigDecimal sellFee) {
        log.info("transferData方法入参,transferTemplateVo={},orderNo={},buyFee={},sellFee={}", JSONObject.toJSONString(transferTemplateVo)
                , orderNo, buyFee, sellFee);
        BigDecimal changeAmount = new BigDecimal(transferTemplateVo.getPrice()).multiply(new BigDecimal(transferTemplateVo.getNum()));
        //判断转入转出用户资金和仓单是否够扣
        ApiResult validatorTransferData = validatorTransferData(transferTemplateVo, changeAmount);
        if (validatorTransferData.hasFail()) {
            log.info("【--------------转入转出用户资金和仓单校验失败--------------】,msg={}",validatorTransferData.getMsg());
            //写入transfer_details表中
            TransferDetails transferDetails = operatorObj(orderNo, transferTemplateVo, buyFee, sellFee);
            transferDetails.setTransferLogId(transferLogId);
            operatorTransferDetailsDB(transferDetails, "fail");
            return ApiResult.error();
        }
            //操作持仓
            try {
                operatorHoldPosition(transferTemplateVo, orderNo);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResult.error();
            }
            //调用账户服务扣/加钱
            ApiResult apiResult = transferAccountService(transferTemplateVo.getInCustomerNo(),
                    transferTemplateVo.getOutCustomerNo(), orderNo, changeAmount, buyFee, sellFee);
            if (apiResult.hasFail()) {
                log.info("apiResult={}", JSONObject.toJSONString(apiResult));
                throw new SystemException(apiResult.getMsg());
            }
            if (apiResult.hasSuccess()) {
                //写入transfer_details表中
                TransferDetails transferDetails = operatorObj(orderNo, transferTemplateVo, buyFee, sellFee);
                transferDetails.setTransferLogId(transferLogId);
                operatorTransferDetailsDB(transferDetails, "success");
                //发送清算消息
                sendMsgToSettleMent(transferTemplateVo.getInCustomerNo(), transferTemplateVo.getOutCustomerNo(), changeAmount,
                        orderNo, buyFee, sellFee);
                return ApiResult.success();
            }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int operatorTransferDetailsDB(TransferDetails transferDetails, String status) {
        transferDetails.setStatus(status);
        return transferDetailsMapper.insertSelective(transferDetails);
    }


    private ApiResult transferAccountService(String inCustomerNo, String outCustomerNo, String orderNo,
                                             BigDecimal changeAmount, BigDecimal buyFee, BigDecimal sellFee) {
        log.info("调用账户服务入参,inCustomerNo={},outCustomerNo={},changeAmount={},buyFee={},sellFee={}",
                inCustomerNo, outCustomerNo, changeAmount, buyFee, sellFee);
        //因为非交易过户会出现一种不产生资金变动的情况，这里的判断就是当changeAmount字段为0时不需要调用账户服务的
        if (changeAmount.compareTo(new BigDecimal(0)) == 0) {
            return ApiResult.success();
        }

        List<ChangeAmountDto> changeAmountDtos = Lists.newArrayList();
        //加钱
        ChangeAmountDto inChangeAmount = new ChangeAmountDto();
        inChangeAmount.setCustomerNo(outCustomerNo);
        inChangeAmount.setChangeAmount(changeAmount.subtract(sellFee));
        inChangeAmount.setOrderNo(orderNo);
        inChangeAmount.setTradeType(TradeMoneyTradeTypeEnum.SELL_TRANSFER.getCode());
        inChangeAmount.setReType("in");
        changeAmountDtos.add(inChangeAmount);
        //扣钱
        ChangeAmountDto outChangeAmount = new ChangeAmountDto();
        outChangeAmount.setOrderNo(orderNo);
        outChangeAmount.setReType("out");
        outChangeAmount.setCustomerNo(inCustomerNo);
        outChangeAmount.setChangeAmount(changeAmount.add(buyFee));
        outChangeAmount.setTradeType(TradeMoneyTradeTypeEnum.BUY_TRANSFER.getCode());

        changeAmountDtos.add(outChangeAmount);
        ApiResult apiResult = accountFeign.changeMoneyList(changeAmountDtos);
        return apiResult;
    }

    @Override
    public TransferDetails operatorObj(String orderNo, TransferTemplateVo transferTemplateVo, BigDecimal buyFee, BigDecimal sellFee) {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setId(IdWorker.getId());
        transferDetails.setSerialNumber(orderNo);
        transferDetails.setBuyFee(buyFee);
        transferDetails.setSellFee(sellFee);
        transferDetails.setInCustomerNo(transferTemplateVo.getInCustomerNo());
        transferDetails.setOutCustomerNo(transferTemplateVo.getOutCustomerNo());
        transferDetails.setCostPrice(new BigDecimal(transferTemplateVo.getCostPrice()));
        transferDetails.setCreateTime(new Date());
        transferDetails.setModifyTime(new Date());
        transferDetails.setFlag((byte) 1);
        transferDetails.setPrice(new BigDecimal(transferTemplateVo.getPrice()));
        transferDetails.setProductTradeNo(transferTemplateVo.getProductTradeNo());
        transferDetails.setRemark(transferTemplateVo.getRemark());
        transferDetails.setTransferNum(Integer.valueOf(transferTemplateVo.getNum()));
        return transferDetails;
    }

    private void operatorHoldPosition(TransferTemplateVo transferTemplateVo, String orderNo) {
        //新增持仓
        CustomerNoDto inCustomerNo = new CustomerNoDto();
        inCustomerNo.setCustomerNo(transferTemplateVo.getInCustomerNo());
        ApiResult<CustomerVo> inCustomerInfo = customerFeign.findUserByCustomerNo(inCustomerNo);
        if (inCustomerInfo.hasFail()) {
            throw new SystemException("用户信息不存在");
        }
        ChangeHoldPositionBo inChangeHoldPosition = ChangeHoldPositionBo.builder()
                .count(Integer.valueOf(transferTemplateVo.getNum()))
                .customerNo(transferTemplateVo.getInCustomerNo())
                .price(new BigDecimal(transferTemplateVo.getPrice()))
                .productTradeNo(transferTemplateVo.getProductTradeNo())
                .remark(transferTemplateVo.getRemark())
                .resource(HoldResourceEnum.BUY_TRANSFER.getCode())
                .resourceNo(orderNo)
                .tradeTime(DateUtil.strToDate(transferTemplateVo.getTradeTime()))
                .customerName(inCustomerInfo.getData().getRealName())
                .reType("in")
                .build();
        holdPostionChangeService.increase(inChangeHoldPosition);

        //扣减持仓
        CustomerNoDto outCustomerNo = new CustomerNoDto();
        outCustomerNo.setCustomerNo(transferTemplateVo.getOutCustomerNo());
        ApiResult<CustomerVo> outCustomerInfo = customerFeign.findUserByCustomerNo(outCustomerNo);
        if (outCustomerInfo.hasFail()) {
            throw new SystemException("用户信息不存在");
        }
        ChangeHoldPositionBo outChangeHoldPosition = ChangeHoldPositionBo.builder()
                .count(Integer.valueOf(transferTemplateVo.getNum()))
                .customerNo(transferTemplateVo.getOutCustomerNo())
                .price(new BigDecimal(transferTemplateVo.getPrice()))
                .productTradeNo(transferTemplateVo.getProductTradeNo())
                .remark(transferTemplateVo.getRemark())
                .resource(HoldResourceEnum.SELL_TRANSFER.getCode())
                .customerName(outCustomerInfo.getData().getRealName())
                .resourceNo(orderNo)
                .reType("out")
                .build();
        holdPostionChangeService.deduct(outChangeHoldPosition);
    }

    private void sendMsgToSettleMent(String inCustomerNo, String outCustomerNo, BigDecimal changeAmount,
                                     String orderNo, BigDecimal buyFee, BigDecimal sellFee) {
        log.info("发送清算服务入参,inCustomerNo={},outCustomerNo={},orderNo={},changeAmount={},buyFee={},sellFee={}",
                inCustomerNo, outCustomerNo, orderNo, changeAmount, buyFee, sellFee);
        //更新成功后通知清算服务
        List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
        //客户余额增加变动
        SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
        customerMsg.setCustomerNo(outCustomerNo);
        customerMsg.setAmount(changeAmount.subtract(sellFee));
        customerMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        customerMsg.setRetype(Constants.Retype.IN);
        customerMsg.setOrderNo(orderNo);
        customerMsg.setEventTime(new Date());
        customerMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(customerMsg);
        //客户账户余额减少变动
        SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
        distributorMsg.setCustomerNo(inCustomerNo);
        distributorMsg.setAmount(changeAmount.add(buyFee));
        distributorMsg.setTransferBizType(Constants.TransferBizType.TRADE);
        distributorMsg.setRetype(Constants.Retype.OUT);
        distributorMsg.setOrderNo(orderNo);
        distributorMsg.setEventTime(new Date());
        distributorMsg.setApplicationName(applicationName);
        settlementMetadataMsgList.add(distributorMsg);

        String msg = JacksonUtil.beanToJson(settlementMetadataMsgList);
        rocketMQUtil.sendMsg(settlementCleanTopics, msg, orderNo);
    }

    private ApiResult validatorTransferData(TransferTemplateVo transferTemplateVo, BigDecimal changeAmount) {
        //查询资金是否充足
        CustomerNoDto inCustomerNoDto = new CustomerNoDto();
        inCustomerNoDto.setCustomerNo(transferTemplateVo.getInCustomerNo());
        ApiResult<AccountVo> inAccountResult = accountFeign.findAccount(inCustomerNoDto);
        log.info("查询买方资金是否充足,inAccountResult={}", JSONObject.toJSONString(inAccountResult));
        if (inAccountResult.hasSuccess()) {
            AccountVo inAccountResultData = inAccountResult.getData();
            if (inAccountResultData.getBalance().compareTo(changeAmount) == -1) {
                return ApiResult.error("买方资金不足");
            }
        }
        //查询仓单是否充足
        HoldPosition outHoldPosition = holdPositionService.find(transferTemplateVo.getOutCustomerNo(), transferTemplateVo.getProductTradeNo());
        log.info("查询卖方仓单是否充足,outHoldPosition={}", JSONObject.toJSONString(outHoldPosition));
        if (outHoldPosition != null) {
            if (outHoldPosition.getRemaindCount() < Integer.valueOf(transferTemplateVo.getNum())) {
                return ApiResult.error("卖方仓单持仓数量不足");
            }
            if (outHoldPosition.getCanSellCount() < Integer.valueOf(transferTemplateVo.getNum())) {
                return ApiResult.error("卖方仓单可卖量不足");
            }
        } else {
            return ApiResult.error("客户持仓信息为空");
        }
        return ApiResult.success();
    }

    @Override
    public BigDecimal buyFee(String inCustomerNo, String price, String num, String isFee) {
        boolean inCustomerFlag = Boolean.FALSE;
        BigDecimal buyFee = null;
        if ("1".equals(isFee) || "".equals(isFee)) { //说明都要收手续费
            inCustomerFlag = feeExemptionConfigService.isExist(inCustomerNo);
            if (inCustomerFlag) { //说明在白名单中 就不用收手续
                buyFee = new BigDecimal(0);
            } else {
                buyFee = tradeUtil.getBuyFee(inCustomerNo,
                        new BigDecimal(price), Integer.valueOf(num));
            }
        }
        if("0".equals(isFee)){
            buyFee = new BigDecimal(0);
        }
        log.info("buyFee={}", buyFee);
        return buyFee;
    }

    @Override
    public BigDecimal sellFee(String outCustomerNo, String price, String num, String isFee) {
        boolean outCustomerFlag = Boolean.FALSE;
        BigDecimal sellFee = null;
        if ("1".equals(isFee) || "".equals(isFee)) { //说明都要收手续费
            outCustomerFlag = feeExemptionConfigService.isExist(outCustomerNo);
            if (outCustomerFlag) { //说明在白名单中 就不用收手续
                sellFee = new BigDecimal(0);
            } else {
                sellFee = tradeUtil.getSellFee(outCustomerNo,
                        new BigDecimal(price), Integer.valueOf(num));
            }
        }
        if("0".equals(isFee)){
            sellFee = new BigDecimal(0);
        }
        log.info("sellFee={}", sellFee);
        return sellFee;
    }
}
