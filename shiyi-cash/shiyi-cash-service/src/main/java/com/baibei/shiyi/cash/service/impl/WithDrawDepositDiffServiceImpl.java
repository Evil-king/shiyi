package com.baibei.shiyi.cash.service.impl;

import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.cash.dao.WithDrawDepositDiffMapper;
import com.baibei.shiyi.cash.enumeration.OrderTypeEnum;
import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.model.BankOrder;
import com.baibei.shiyi.cash.model.OrderDeposit;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.model.WithDrawDepositDiff;
import com.baibei.shiyi.cash.service.IBankOrderService;
import com.baibei.shiyi.cash.service.IOrderDepositService;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.service.IWithDrawDepositDiffService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.cash.util.Utils;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/11/06 13:51:22
* @description: WithDrawDepositDiff服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WithDrawDepositDiffServiceImpl extends AbstractService<WithDrawDepositDiff> implements IWithDrawDepositDiffService {

    @Autowired
    private WithDrawDepositDiffMapper withDrawDepositDiffMapper;
    @Autowired
    private IBankOrderService bankOrderService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private IOrderDepositService orderDepositService;
    @Autowired
    private ISigningRecordFeign signingRecordFeign;
    @Autowired
    private PropertiesVal propertiesVal;



    @Override
    public ApiResult withDrawDepositDiff(String batchNo) {
        log.info("======开始出入金流水对账=======");
        if(StringUtils.isEmpty(batchNo)){
            return ApiResult.error("对账失败，未指定对账批次");
        }
        //先删除，防止多次对账，多次入库的情况
        this.deleteByBatchNo(batchNo);
        /**
         * 对账：
         * 银行出入金流水信息与业务系统出入金流水信息进行对账
         */
        List<BankOrder> bankOrderList = bankOrderService.getOrderListByBatchNo(batchNo);

        //系统出金订单信息
        List<OrderWithdraw> withdrawOrderList = orderWithdrawService.getPeriodOrderList(batchNo);
        //系统入金订单信息
        List<OrderDeposit> depositOrderList = orderDepositService.getPeriodOrderList(batchNo);

        //合并出金和入金流水
        List<WithDrawDepositDto> combineList = combineWithDrawAndDepositList(withdrawOrderList,depositOrderList);

        //生成差异流水集合
        List<WithDrawDepositDiff> withDrawDepositDiffList=diffList(bankOrderList,combineList,batchNo);

        if (!CollectionUtils.isEmpty(withDrawDepositDiffList)) {
            //插入对账流水
            save(withDrawDepositDiffList);
            /*withDrawDepositDiffMapper.insertList(withDrawDepositDiffList);*/
        }
        log.info("======结束出入金流水对账=======");
        return ApiResult.success();
    }

    @Override
    public int deleteByBatchNo(String batchNo) {
        Condition condition = new Condition(BankOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(batchNo)){
            criteria.andEqualTo("batchNo",batchNo);
        }
        return withDrawDepositDiffMapper.deleteByCondition(condition);
    }

    @Override
    public ApiResult<DealDiffMessage> dealDiff(DealDiffDto dealDiffDto) {
        //根据id获取异常信息
        WithDrawDepositDiff withDrawDepositDiff = withDrawDepositDiffMapper.selectByPrimaryKey(dealDiffDto.getDiffId());
        if (withDrawDepositDiff==null) {
            throw new ServiceException("找不到该差异流水信息");
        }
        String diffType = withDrawDepositDiff.getDiffType();//差异类型
        if(withDrawDepositDiff.getStatus().equals(Constants.DiffStatus.DEAL)){
            throw new ServiceException("调账失败，不可重复调账");
        }
        //调账
        DealDiffMessage dealDiffMessage = deal(withDrawDepositDiff);
        //改为已处理状态
        withDrawDepositDiff.setStatus(dealDiffMessage.isChangeFlag()?Constants.DiffStatus.DEAL:Constants.DiffStatus.DOING);
        int i = this.updateDiffWithWait(withDrawDepositDiff);
        if(i==0){
            throw new ServiceException("调账失败，异常流水乐观锁问题");
        }
        return ApiResult.success(dealDiffMessage);
    }

    @Override
    public void dealDiffAck(DealDiffMessage dealDiffMessage) {
        String diffType = dealDiffMessage.getDiffType();
        String type = dealDiffMessage.getOrderType();
        switch(diffType){
            case Constants.DiffType.LONG_DIFF ://长款差异（银行有，系统没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffWithDrawAckLongDiff(dealDiffMessage);
                break;
            case Constants.DiffType.AMOUNT_DIFF ://金额不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffWithDrawAckAmountDiff(dealDiffMessage);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffDepositAckAmountDiff(dealDiffMessage);
                break;
            case Constants.DiffType.STATUS_DIFF ://状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffWithDrawAckStatusDiff(dealDiffMessage);
                break;
            case Constants.DiffType.AMOUNT_STATUS_DIFF://金额和状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffWithDrawAckAmountStatusDiff(dealDiffMessage);
                break;
            case Constants.DiffType.SHORT_DIFF ://短款差异（系统有，银行没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffWithDrawAckShortDiff(dealDiffMessage);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffDepositAckShortDiff(dealDiffMessage);
                break;
            default : //可选
                log.info("dealDiffAck error ==>{}",diffType);
        }
    }

    /**
     * 入金，短款差异（系统有，银行没有），调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffDepositAckShortDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())){
            //修改入金订单状态
            //更新入金订单的金额
            OrderDeposit updateEntity = new OrderDeposit();
            updateEntity.setStatus(dealDiffMessage.getOrderStatus());
            int i = orderDepositService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("更新入金订单失败，乐观锁问题");
            }
            //修改调账状态为“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //修改调账状态为“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 入金，金额不一致，调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffDepositAckAmountDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())){
            //更新入金订单的金额
            OrderDeposit updateEntity = new OrderDeposit();
            updateEntity.setOrderAmt(dealDiffMessage.getBankAmount());
            int i = orderDepositService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("更新入金订单失败，乐观锁问题");
            }

            //修改调账状态为“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //修改调账状态为“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 出金，长款差异，调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffWithDrawAckLongDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())) {//修改资金成功
            //更新出金订单状态
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(dealDiffMessage.getOrderStatus());
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("dealDiffAck修改出金订单状态失败，乐观锁问题");
            }
            //修改调账状态为“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //删除该订单，这样才可以针对同个订单进行多次调账
            orderWithdrawService.deleteByOrderNo(dealDiffMessage.getOrderNo());
            //修改调账状态为“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 出金，金额差异，调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffWithDrawAckAmountDiff(DealDiffMessage dealDiffMessage) {
        if(Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())){//修改用户资金成功
            //更新订单金额和手续费
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setOrderamt(dealDiffMessage.getBankAmount());
            updateEntity.setHandelFee(dealDiffMessage.getBankFee());
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("dealDiffAck修改出金订单状态失败，乐观锁问题");
            }
            //将调账状态修改成“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //将调账状态修改成“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 出金，状态不一致，调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffWithDrawAckStatusDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())) {
            //更改出金订单状态
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(dealDiffMessage.getOrderStatus());
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("dealDiffAck修改出金订单状态失败，乐观锁问题");
            }
            //将调账状态修改成“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //将调账状态修改成“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 出金，状态和资金不一致，调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffWithDrawAckAmountStatusDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())){
            //更改出金订单状态和金额
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(dealDiffMessage.getOrderStatus());
            updateEntity.setOrderamt(dealDiffMessage.getBankAmount());
            updateEntity.setHandelFee(dealDiffMessage.getBankFee());
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("dealDiffAck修改出金订单状态失败，乐观锁问题");
            }
            //将调账状态修改成“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //将调账状态修改成“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    /**
     * 出金，短款差异（系统有，银行没有）,调账回调执行逻辑
     * @param dealDiffMessage
     */
    public void dealDiffWithDrawAckShortDiff(DealDiffMessage dealDiffMessage) {
        if (Constants.Status.SUCCESS.equals(dealDiffMessage.getResultFlag())){
            //更新出金订单状态
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(dealDiffMessage.getOrderStatus());
            int i = orderWithdrawService.safetyUpdateOrderBySelective(updateEntity, dealDiffMessage.getOrderNo());
            if(i==0){
                throw new ServiceException("dealDiffAck修改出金订单状态失败，乐观锁问题");
            }
            //修改调账状态为“已处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }else{
            //修改调账状态为“待处理”
            WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
            withDrawDepositDiff.setId(dealDiffMessage.getDiffId());
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiffMapper.updateByPrimaryKeySelective(withDrawDepositDiff);
        }
    }

    @Override
    public int updateDiffWithWait(WithDrawDepositDiff withDrawDepositDiff) {
        Condition condition = new Condition(WithDrawDepositDiff.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status",Constants.DiffStatus.WAIT);//乐观锁
        criteria.andEqualTo("id",withDrawDepositDiff.getId());
        return withDrawDepositDiffMapper.updateByCondition(withDrawDepositDiff,condition);
    }



    /**
     * 合并系统出金流水和入金流水
     * @param withdrawOrderList
     * @param depositOrderList
     * @return
     */
    public List<WithDrawDepositDto> combineWithDrawAndDepositList(List<OrderWithdraw> withdrawOrderList,List<OrderDeposit> depositOrderList){
        List<WithDrawDepositDto> combineList = new ArrayList<>();
        for (OrderWithdraw orderWithdraw : withdrawOrderList) {
            WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto();
            withDrawDepositDto.setType(OrderTypeEnum.WITHDRAW.getOrderType());
            withDrawDepositDto.setOrderNo(orderWithdraw.getOrderNo());
            withDrawDepositDto.setExternalNo(orderWithdraw.getExternalNo());
            withDrawDepositDto.setAmount(orderWithdraw.getOrderamt());
            withDrawDepositDto.setStatus(orderWithdraw.getStatus());
            combineList.add(withDrawDepositDto);
        }
        for (OrderDeposit orderDeposit : depositOrderList) {
            WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto();
            withDrawDepositDto.setType(OrderTypeEnum.DEPOSIT.getOrderType());
            withDrawDepositDto.setOrderNo(orderDeposit.getOrderNo());
            withDrawDepositDto.setExternalNo(orderDeposit.getExternalNo());
            withDrawDepositDto.setAmount(orderDeposit.getOrderAmt());
            withDrawDepositDto.setStatus(orderDeposit.getStatus());
            combineList.add(withDrawDepositDto);
        }
        return combineList;
    }
    public List<WithDrawDepositDiff> diffList(List<BankOrder> bankOrderList,List<WithDrawDepositDto> combineList,String batchNo){
        //对账流水信息
        List<WithDrawDepositDiff> withDrawDepositDiffList = new ArrayList<>();
        int count=0;
        for (int i = 0; i < bankOrderList.size(); i++) {
            BankOrder bankOrder = bankOrderList.get(i);
            if(combineList.size()!=0){
                for (int j = 0; j < combineList.size(); j++) {
                    WithDrawDepositDto withDrawDepositDto = combineList.get(j);
                    if(!bankOrder.getBankSerialNo().equals(withDrawDepositDto.getExternalNo())){
                        count++;
                        //长款差异，银行有，系统没有
                        if(count==combineList.size()){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.LONG_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                            count=0;//重置
                        }
                    }else{
                        //金额不一致
                        if (withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())!=0&&
                                (withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS)||withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))) {
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.AMOUNT_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        //状态不一致(出金成功状态:4 ，入金成功状态:success)
                        if(withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())==0
                                &&(!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS)&&!withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.STATUS_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        //金额和状态不一致
                        if(withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())!=0&&
                                (!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS)&&!withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.AMOUNT_STATUS_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        count=0;//重置
                    }
                }
            }else{ //长款差异，银行有，系统没有
                WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,null,Constants.DiffType.LONG_DIFF,batchNo);
                withDrawDepositDiffList.add(withDrawDepositDiff);
            }
            count=0;//重置
        }

        //短款差错（系统有，银行没有）
        int countj=0;
        for (int i = 0; i < combineList.size(); i++) {
            WithDrawDepositDto withDrawDepositDto = combineList.get(i);
            if(bankOrderList.size()!=0){
                for (int j = 0; j < bankOrderList.size(); j++) {
                    BankOrder bankOrder = bankOrderList.get(j);
                    if(!withDrawDepositDto.getExternalNo().equals(bankOrder.getBankSerialNo())){
                        countj++;
                        if(countj==bankOrderList.size()){
                            if(!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS)
                                    &&!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_FAIL)
                                    &&!withDrawDepositDto.getStatus().equals(Constants.Status.FAIL)) {
                                WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder, withDrawDepositDto, Constants.DiffType.SHORT_DIFF, batchNo);
                                withDrawDepositDiffList.add(withDrawDepositDiff);
                            }
                        }
                    }
                }
            }else{
                if(!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS)
                        &&!withDrawDepositDto.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_FAIL)
                        &&!withDrawDepositDto.getStatus().equals(Constants.Status.FAIL)){
                    WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(null,withDrawDepositDto,Constants.DiffType.SHORT_DIFF,batchNo);
                    withDrawDepositDiffList.add(withDrawDepositDiff);
                }
            }
            countj=0;//重置
        }
        return withDrawDepositDiffList;
    }

    public WithDrawDepositDiff createDiffInfo(BankOrder bankOrder,WithDrawDepositDto withDrawDepositDto,String diffType,String batchNo){
        WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
        withDrawDepositDiff.setId(IdWorker.getId());
        withDrawDepositDiff.setBatchNo(batchNo);
        if(Constants.DiffType.LONG_DIFF.equals(diffType)){//长款差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(bankOrder.getType().toString().equals("1")?OrderTypeEnum.WITHDRAW.getOrderType():OrderTypeEnum.DEPOSIT.getOrderType());
            //对账差异 长款差错（银行有，系统没有）
            withDrawDepositDiff.setDiffType(diffType);
            //银行系统订单号
            withDrawDepositDiff.setExternalNo(bankOrder.getBankSerialNo());
            //银行订单状态（全部是success）
            withDrawDepositDiff.setBankStatus(Constants.Status.SUCCESS);
            //银行系统订单金额
            withDrawDepositDiff.setBankAmount(bankOrder.getAmount());
            //处理状态，wait=待处理，deal=已处理
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }else if(Constants.DiffType.SHORT_DIFF.equals(diffType)){//短款差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(withDrawDepositDto.getType());
            //恒价系统订单号
            withDrawDepositDiff.setOrderNo(withDrawDepositDto.getOrderNo());
            //外部订单号
            withDrawDepositDiff.setExternalNo(withDrawDepositDto.getExternalNo());
            //恒价系统订单状态
            withDrawDepositDiff.setHengjiaStatus(withDrawDepositDto.getStatus());
            //恒价系统订单金额
            withDrawDepositDiff.setHengjiaAmount(withDrawDepositDto.getAmount());
            withDrawDepositDiff.setDiffType(diffType);
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }else{//其他差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(withDrawDepositDto.getType());
            //对账差异 金额不一致
            withDrawDepositDiff.setDiffType(diffType);
            //恒价系统订单号
            withDrawDepositDiff.setOrderNo(withDrawDepositDto.getOrderNo());
            //银行系统订单号
            withDrawDepositDiff.setExternalNo(bankOrder.getBankSerialNo());
            //恒价状态
            withDrawDepositDiff.setHengjiaStatus(withDrawDepositDto.getStatus());
            //银行订单状态（全部是success）
            withDrawDepositDiff.setBankStatus(Constants.Status.SUCCESS);
            //恒价系统订单金额
            withDrawDepositDiff.setHengjiaAmount(withDrawDepositDto.getAmount());
            //银行系统订单金额
            withDrawDepositDiff.setBankAmount(bankOrder.getAmount());
            //处理状态，wait=待处理，deal=已处理
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }
        return withDrawDepositDiff;
    }


    /**
     * 处理差异流水(调账)
     * @param withDrawDepositDiff
     */
    public DealDiffMessage deal (WithDrawDepositDiff withDrawDepositDiff){
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        String diffType = withDrawDepositDiff.getDiffType();//差异类型
        String type = withDrawDepositDiff.getType();//（withdraw:出金，deposit:入金）
        switch(diffType){
            case Constants.DiffType.LONG_DIFF ://长款差异（银行有，系统没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffMessage=withDrawLongDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffMessage=depositLongDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.AMOUNT_DIFF ://金额不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffMessage=withDrawAmountDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffMessage=depositAmountDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.STATUS_DIFF ://状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffMessage=withDrawStatusDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffMessage=depositStatusDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.AMOUNT_STATUS_DIFF://金额和状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffMessage=withDrawAmountStatusDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffMessage=depositAmountStatusDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.SHORT_DIFF ://短款差异（系统有，银行没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    dealDiffMessage=withDrawShortDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    dealDiffMessage=depositShortDiff(withDrawDepositDiff);
                break;
            default : //可选
                log.info("diffType error ==>{}",diffType);
        }
        return dealDiffMessage;
    }


    /**
     * 出金，长款差异 处理逻辑（银行有，系统没有）
     * @param withDrawDepositDiff
     */
    public DealDiffMessage withDrawLongDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //获取用户签约信息
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(customerNo);
        SigningRecordVo signingRecord = signingRecordVoApiResult.getData();
        if (signingRecordVoApiResult.getCode()!=200) {
            log.info("获取用户签约信息报错：",signingRecordVoApiResult.getMsg());
            throw new ServiceException("获取用户签约信息失败");
        }
        if (StringUtils.isEmpty(signingRecord)) {
            throw new ServiceException("用户签约信息不存在");
        }
        //生成出金订单
        OrderWithdrawDto orderWithdrawDto = new OrderWithdrawDto();
        orderWithdrawDto.setType("dealDiff");
        orderWithdrawDto.setCustomerNo(customerNo);
        BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
        orderWithdrawDto.setOrderAmt(withdrawAmount);//金额
        orderWithdrawDto.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
        orderWithdrawDto.setBankName(signingRecord.getBankName());
        orderWithdrawDto.setReceiveAccount(signingRecord.getRelatedAcctId());//收款账号
        orderWithdrawDto.setExternalNo(withDrawDepositDiff.getExternalNo());//外部流水号
        String orderNo = NoUtil.generateOrderNo();
        orderWithdrawDto.setOrderNo(orderNo);//订单号
        orderWithdrawDto.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_INIT);
        orderWithdrawService.createOrder(orderWithdrawDto);

        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderNo);
        dealDiffMessage.setOrderType(OrderTypeEnum.WITHDRAW.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
        dealDiffMessage.setChangeFlag(true);
        dealDiffMessage.setDiffType(Constants.DiffType.LONG_DIFF);
        //计算手续费
        BigDecimal Fee = Utils.getFee(orderWithdrawDto.getOrderAmt(), propertiesVal.getRate(), propertiesVal.getFee());
        CashChangeAmountDto amountDto = new CashChangeAmountDto();
        amountDto.setOrderNo(orderNo);
        amountDto.setChangeAmount(withdrawAmount.subtract(Fee));
        amountDto.setCustomerNo(customerNo);
        amountDto.setReType(Constants.Retype.OUT);
        amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());

        CashChangeAmountDto feeDto = new CashChangeAmountDto();
        feeDto.setOrderNo(orderNo);
        feeDto.setChangeAmount(Fee);
        feeDto.setCustomerNo(customerNo);
        feeDto.setReType(Constants.Retype.OUT);
        feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode());
        dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
        return dealDiffMessage;
    }

    /**
     * 出金，金额不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage withDrawAmountDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();

        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderWithdraw.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.WITHDRAW.getOrderType());
        dealDiffMessage.setChangeFlag(true);
        dealDiffMessage.setOrderStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
        dealDiffMessage.setDiffType(Constants.DiffType.AMOUNT_DIFF);
        dealDiffMessage.setBankAmount(withDrawDepositDiff.getBankAmount());
        if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())>0){//银行金额>恒价金额
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal shouldFee=fee.subtract(orderWithdraw.getHandelFee());
            BigDecimal shouldWithdrawAmount = withdrawAmount.subtract(orderWithdraw.getOrderamt()).subtract(fee);
            dealDiffMessage.setBankFee(fee);
            if(shouldFee.compareTo(new BigDecimal("0"))>0){
                //扣除手续费
                CashChangeAmountDto feeDto = new CashChangeAmountDto();
                feeDto.setOrderNo(orderWithdraw.getOrderNo());
                feeDto.setChangeAmount(shouldFee);
                feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
                feeDto.setReType(Constants.Retype.OUT);
                feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode());
                dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
            }
            if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                //扣除钱
                CashChangeAmountDto amountDto = new CashChangeAmountDto();
                amountDto.setOrderNo(orderWithdraw.getOrderNo());
                amountDto.setChangeAmount(shouldWithdrawAmount);
                amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                amountDto.setReType(Constants.Retype.OUT);
                amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
                dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
            }
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())<0){//银行金额<恒价金额
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal shouldFee=orderWithdraw.getHandelFee().subtract(fee);
            BigDecimal shouldWithdrawAmount = orderWithdraw.getOrderamt().subtract((withdrawAmount.subtract(fee)));
            dealDiffMessage.setBankFee(fee);
            if(shouldFee.compareTo(new BigDecimal("0"))>0){
                //加回之前扣除的手续费
                CashChangeAmountDto feeDto = new CashChangeAmountDto();
                feeDto.setOrderNo(orderWithdraw.getOrderNo());
                feeDto.setChangeAmount(shouldFee);
                feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
                feeDto.setReType(Constants.Retype.IN);
                feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode());
                dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);

            }
            if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                //加回之前扣除的钱
                CashChangeAmountDto amountDto = new CashChangeAmountDto();
                amountDto.setOrderNo(orderWithdraw.getOrderNo());
                amountDto.setChangeAmount(shouldWithdrawAmount);
                amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                amountDto.setReType(Constants.Retype.IN);
                amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
                dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
            }
        }
        return dealDiffMessage;
    }

    /**
     * 出金，状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage withDrawStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        if (Constants.OrderWithdrawStatus.WITHDRAW_INIT.equals(orderWithdraw.getStatus())) {
            throw new ServiceException("订单异常");
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffType(Constants.DiffType.STATUS_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderWithdraw.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.WITHDRAW.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
        if(orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_FAIL)
                ||orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS)){
            dealDiffMessage.setChangeFlag(true);
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            //扣除本金
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderWithdraw.getOrderNo());
            amountDto.setChangeAmount(withDrawDepositDiff.getBankAmount());
            amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            amountDto.setReType(Constants.Retype.OUT);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
            //扣除手续费
            CashChangeAmountDto feeDto = new CashChangeAmountDto();
            feeDto.setOrderNo(orderWithdraw.getOrderNo());
            feeDto.setChangeAmount(fee);
            feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
            feeDto.setReType(Constants.Retype.OUT);
            feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
        }
        return dealDiffMessage;
    }

    /**
     * 出金，金额和状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage withDrawAmountStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        if (Constants.OrderWithdrawStatus.WITHDRAW_INIT.equals(orderWithdraw.getStatus())) {
            throw new ServiceException("订单异常");
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffType(Constants.DiffType.AMOUNT_STATUS_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderWithdraw.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.WITHDRAW.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
        dealDiffMessage.setBankAmount(withDrawDepositDiff.getBankAmount());
        dealDiffMessage.setChangeFlag(true);
        if(!orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_FAIL)&&
                !orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS)
                ){
            if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())>0){//银行金额>恒价金额
                BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal shouldFee=fee.subtract(orderWithdraw.getHandelFee());
                BigDecimal shouldWithdrawAmount = withdrawAmount.subtract(orderWithdraw.getOrderamt()).subtract(fee);
                dealDiffMessage.setBankFee(fee);
                if(shouldFee.compareTo(new BigDecimal("0"))>0){
                    //扣除手续费
                    CashChangeAmountDto feeDto = new CashChangeAmountDto();
                    feeDto.setOrderNo(orderWithdraw.getOrderNo());
                    feeDto.setChangeAmount(shouldFee);
                    feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
                    feeDto.setReType(Constants.Retype.OUT);
                    feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode());
                    dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
                }
                if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                    //扣除钱
                    CashChangeAmountDto amountDto = new CashChangeAmountDto();
                    amountDto.setOrderNo(orderWithdraw.getOrderNo());
                    amountDto.setChangeAmount(shouldWithdrawAmount);
                    amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                    amountDto.setReType(Constants.Retype.OUT);
                    amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
                    dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
                }
            }
            if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())<0){//银行金额<恒价金额
                BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal shouldFee=orderWithdraw.getHandelFee().subtract(fee);
                BigDecimal shouldWithdrawAmount = orderWithdraw.getOrderamt().subtract((withdrawAmount.subtract(fee)));
                dealDiffMessage.setBankFee(fee);
                if(shouldFee.compareTo(new BigDecimal("0"))>0){
                    //加回之前扣除的手续费
                    CashChangeAmountDto feeDto = new CashChangeAmountDto();
                    feeDto.setOrderNo(orderWithdraw.getOrderNo());
                    feeDto.setChangeAmount(shouldFee);
                    feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
                    feeDto.setReType(Constants.Retype.IN);
                    feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode());
                    dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
                }
                if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                    //加回之前扣除的钱
                    CashChangeAmountDto amountDto = new CashChangeAmountDto();
                    amountDto.setOrderNo(orderWithdraw.getOrderNo());
                    amountDto.setChangeAmount(shouldWithdrawAmount);
                    amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                    amountDto.setReType(Constants.Retype.IN);
                    amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
                    dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
                }
            }
        }else{//出金失败或者提现审核失败或者初始化状态，系统就没扣用户的钱，这时就直接以银行的出金金额为准，扣除用户的钱
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            dealDiffMessage.setBankFee(fee);
            //扣除手续费
            CashChangeAmountDto feeDto = new CashChangeAmountDto();
            feeDto.setOrderNo(orderWithdraw.getOrderNo());
            feeDto.setChangeAmount(fee);
            feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
            feeDto.setReType(Constants.Retype.OUT);
            feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);

            //扣除钱
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderWithdraw.getOrderNo());
            amountDto.setChangeAmount(withDrawDepositDiff.getBankAmount());
            amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            amountDto.setReType(Constants.Retype.OUT);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }
        return dealDiffMessage;
    }

    /**
     * 出金，短款差异（系统有，银行没有）处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage withDrawShortDiff(WithDrawDepositDiff withDrawDepositDiff){
        if (StringUtils.isEmpty(withDrawDepositDiff.getOrderNo())) {
            throw new ServiceException("短款差异，系统订单号为空");
        }
        OrderWithdraw orderWithdraw = orderWithdrawService.getByOrderNo(withDrawDepositDiff.getOrderNo());
        if(orderWithdraw==null){
            throw new ServiceException("短款差异，找不到该出金订单，订单号为："+withDrawDepositDiff.getOrderNo());
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffType(Constants.DiffType.SHORT_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderWithdraw.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.WITHDRAW.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);

        //将提现的金额加回给用户
        if(!orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_FAIL)
                ||!orderWithdraw.getStatus().equals(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS)){//非提现失败的订单
            dealDiffMessage.setChangeFlag(true);
            //加回之前扣除的钱
            //本金
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderWithdraw.getOrderNo());
            amountDto.setChangeAmount(orderWithdraw.getOrderamt());
            amountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            amountDto.setReType(Constants.Retype.IN);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);

            //手续费
            CashChangeAmountDto feeDto = new CashChangeAmountDto();
            feeDto.setOrderNo(orderWithdraw.getOrderNo());
            feeDto.setChangeAmount(orderWithdraw.getHandelFee());
            feeDto.setCustomerNo(orderWithdraw.getCustomerNo());
            feeDto.setReType(Constants.Retype.IN);
            feeDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(feeDto);
        }
        return dealDiffMessage;
    }


    /**
     * 入金，长款差异 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage depositLongDiff(WithDrawDepositDiff withDrawDepositDiff) {
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //获取用户签约信息
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(customerNo);
        SigningRecordVo signingRecord = signingRecordVoApiResult.getData();
        if (signingRecordVoApiResult.getCode()!=200) {
            log.info("获取用户签约信息报错：",signingRecordVoApiResult.getMsg());
            throw new ServiceException("获取用户签约信息失败");
        }
        if (StringUtils.isEmpty(signingRecord)) {
            throw new ServiceException("用户签约信息不存在");
        }
        //生成入金订单
        PABDepositDto pabDepositDto = new PABDepositDto();
        //入金金额
        pabDepositDto.setTranAmount(withDrawDepositDiff.getBankAmount());
        //入金账号
        pabDepositDto.setInAcctId(signingRecord.getRelatedAcctId());
        //入金账号名称
        pabDepositDto.setInAcctIdName(signingRecord.getAcctName());
        //币种（人民币）
        pabDepositDto.setCcyCode("RMB");
        //会计日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date acctDate = null;
        try {
            acctDate = simpleDateFormat.parse(customerBankOrder.getPayDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pabDepositDto.setAcctDate(acctDate);
        //会员子账号
        pabDepositDto.setCustAcctId(signingRecord.getCustAcctId());
        pabDepositDto.setExternalNo(withDrawDepositDiff.getExternalNo());
        orderDepositService.deposit(pabDepositDto);
        //将调账状态更改成“已处理”
        WithDrawDepositDiff updateEntity = new WithDrawDepositDiff();
        updateEntity.setId(withDrawDepositDiff.getId());
        updateEntity.setStatus(Constants.DiffStatus.DEAL);
        updateEntity.setModifyTime(new Date());
        withDrawDepositDiffMapper.updateByPrimaryKeySelective(updateEntity);
        return new DealDiffMessage();
    }


    /**
     * 入金，金额不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage depositAmountDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffType(Constants.DiffType.AMOUNT_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderDeposit.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.Status.SUCCESS);
        dealDiffMessage.setChangeFlag(true);
        dealDiffMessage.setBankAmount(withDrawDepositDiff.getBankAmount());
        if(withDrawDepositDiff.getBankAmount().compareTo(orderDeposit.getOrderAmt())>0){//银行金额>恒价金额
            //差额
            BigDecimal subPrice = withDrawDepositDiff.getBankAmount().subtract(orderDeposit.getOrderAmt());
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderDeposit.getOrderNo());
            amountDto.setChangeAmount(subPrice);
            amountDto.setCustomerNo(orderDeposit.getCustomerNo());
            amountDto.setReType(Constants.Retype.IN);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderDeposit.getOrderAmt())<0){//银行金额<恒价金额
            //差额
            BigDecimal subPrice = orderDeposit.getOrderAmt().subtract(withDrawDepositDiff.getBankAmount());
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderDeposit.getOrderNo());
            amountDto.setChangeAmount(subPrice);
            amountDto.setCustomerNo(orderDeposit.getCustomerNo());
            amountDto.setReType(Constants.Retype.OUT);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }
        return dealDiffMessage;
    }

    /**
     * 入金，状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage depositStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        /**
         * 由于入金是不存在“失败状态的”，所以，如果对账存在系统有失败状态的入金订单跟银行那边对不上的话，则直接报错，后续在排查
         */
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }
        if (Constants.Status.FAIL.equals(orderDeposit.getStatus())) {
            throw new ServiceException("订单异常");
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        // TODO 注意，这里下面注释的代码不能删除
       /* dealDiffMessage.setDiffType(Constants.DiffType.STATUS_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderDeposit.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.Status.SUCCESS);

        if(!orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金失败
            dealDiffMessage.setChangeFlag(true);
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderDeposit.getOrderNo());
            amountDto.setChangeAmount(orderDeposit.getOrderAmt());
            amountDto.setCustomerNo(orderDeposit.getCustomerNo());
            amountDto.setReType(Constants.Retype.IN);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }*/
        return dealDiffMessage;
    }

    /**
     * 入金，金额和状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage depositAmountStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        /**
         * 由于入金是不存在“失败状态的”，所以，如果对账存在系统有失败状态的入金订单跟银行那边对不上的话，则直接报错，后续在排查
         */
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();

        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }
        if (Constants.Status.FAIL.equals(orderDeposit.getStatus())) {
            throw new ServiceException("订单异常");
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        // TODO 注意，这里下面注释的代码不能删除
        /*dealDiffMessage.setDiffType(Constants.DiffType.AMOUNT_STATUS_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderDeposit.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.Status.SUCCESS);
        dealDiffMessage.setBankAmount(withDrawDepositDiff.getBankAmount());
        if(!orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金失败
            dealDiffMessage.setChangeFlag(true);
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderDeposit.getOrderNo());
            amountDto.setChangeAmount(withDrawDepositDiff.getBankAmount());
            amountDto.setCustomerNo(orderDeposit.getCustomerNo());
            amountDto.setReType(Constants.Retype.IN);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }*/
        return dealDiffMessage;
    }

    /**
     * 入金，短款差错 处理逻辑
     * @param withDrawDepositDiff
     */
    public DealDiffMessage depositShortDiff(WithDrawDepositDiff withDrawDepositDiff){
        if (StringUtils.isEmpty(withDrawDepositDiff.getOrderNo())) {
            throw new ServiceException("短款差异，系统订单号为空");
        }
        OrderDeposit orderDeposit = orderDepositService.getOrderByOrderNo(withDrawDepositDiff.getOrderNo());
        if(orderDeposit==null){
            throw new ServiceException("找不到指定的入金订单信息");
        }
        DealDiffMessage dealDiffMessage = new DealDiffMessage();
        dealDiffMessage.setDiffType(Constants.DiffType.SHORT_DIFF);
        dealDiffMessage.setDiffId(withDrawDepositDiff.getId());
        dealDiffMessage.setOrderNo(orderDeposit.getOrderNo());
        dealDiffMessage.setOrderType(OrderTypeEnum.DEPOSIT.getOrderType());
        dealDiffMessage.setOrderStatus(Constants.Status.FAIL);
        dealDiffMessage.setBankAmount(withDrawDepositDiff.getBankAmount());
        if(orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金成功，扣钱
            dealDiffMessage.setChangeFlag(true);
            CashChangeAmountDto amountDto = new CashChangeAmountDto();
            amountDto.setOrderNo(orderDeposit.getOrderNo());
            amountDto.setChangeAmount(orderDeposit.getOrderAmt());
            amountDto.setCustomerNo(orderDeposit.getCustomerNo());
            amountDto.setReType(Constants.Retype.OUT);
            amountDto.setTradeType(TradeMoneyTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode());
            dealDiffMessage.getCashChangeAmountDtoList().add(amountDto);
        }
        return dealDiffMessage;
    }

    @Override
    public MyPageInfo<WithDrawDepositDiffVo> pageList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
        PageHelper.startPage(withDrawDepositDiffDto.getCurrentPage(), withDrawDepositDiffDto.getPageSize());
        List<WithDrawDepositDiffVo> list = withDrawDepositDiffMapper.myList(withDrawDepositDiffDto);
        MyPageInfo<WithDrawDepositDiffVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public List<WithDrawDepositDiffVo> WithDrawDepositDiffVoList(WithDrawDepositDiffDto withDrawDepositDiffDto) {
        List<WithDrawDepositDiffVo> list = withDrawDepositDiffMapper.myList(withDrawDepositDiffDto);
        return list;
    }
}
