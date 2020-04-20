package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.common.dto.ExtractProductDto;
import com.baibei.shiyi.account.dao.PassCardExtractOrderMapper;
import com.baibei.shiyi.account.dao.PassCardMapper;
import com.baibei.shiyi.account.dao.RecordPassCardMapper;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.model.*;
import com.baibei.shiyi.account.service.IPassCardExtractOrderService;
import com.baibei.shiyi.account.service.IPassCardService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordPassCardEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.content.feign.bean.vo.ExtractProductVo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.ProductVo;
import com.baibei.shiyi.trade.feign.client.shiyi.TradeFeign;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/11/11 10:32:40
* @description: PassCard服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PassCardServiceImpl extends AbstractService<PassCard> implements IPassCardService {

    @Autowired
    private PassCardMapper tblAccountPassCardMapper;

    @Autowired
    private RecordPassCardMapper recordPassCardMapper;

    @Autowired
    private TradeFeign tradeFeign;

    @Autowired
    private PassCardExtractOrderMapper passCardExtractOrderMapper;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private IPassCardExtractOrderService passCardExtractOrderService;



    public PassCard findByCustomer(String customerNo){
        Condition condition=new Condition(PassCard.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<PassCard> passCards = tblAccountPassCardMapper.selectByCondition(condition);
        if(passCards.size()>0){
            return passCards.get(0);
        }else {
            return null;
        }
    }

    @Override
    public ApiResult changeAmount(ChangeAmountDto changeAmountDto) {
        log.info("修改通证传入的值为"+changeAmountDto);
        if((changeAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        PassCard passCard=findByCustomer(changeAmountDto.getCustomerNo());
        if(passCard==null){
            //如果不存在则创建
            passCard=new PassCard();
            passCard.setBalance(BigDecimal.ZERO);
            passCard.setCustomerNo(changeAmountDto.getCustomerNo());
            passCard.setId(IdWorker.getId());
            passCard.setCreateTime(new Date());
            passCard.setModifyTime(new Date());
            passCard.setFlag(new Byte(Constants.Flag.VALID));
            tblAccountPassCardMapper.insertSelective(passCard);
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",passCard.getCustomerNo());
        criteria.andEqualTo("balance",passCard.getBalance());
        if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
            //支出
            passCard.setBalance(passCard.getBalance().subtract(changeAmountDto.getChangeAmount()));
        }else if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
            //收入
            passCard.setBalance(passCard.getBalance().add(changeAmountDto.getChangeAmount()));
        }else {
            throw new ServiceException("收支类型错误");
        }
        if (checkAmountNegative(passCard.getBalance())) {
            //判断是否为负数，为负数则返回余额不足
            throw new ServiceException("红木券余额不足");
        }
        passCard.setModifyTime(new Date());
        //插入一条可用资金修改流水
        insertRecordPassCard(changeAmountDto,passCard);
        //判断是否为修改成功
        if (!updateByConditionSelective(passCard,condition)) {
            throw new ServiceException("红木券扣除失败");
        }
        return ApiResult.success();
    }

    @Override
    public PassCard findByCustomerNo(String customerNo) {
        Condition condition=new Condition(PassCard.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<PassCard> passCards = tblAccountPassCardMapper.selectByCondition(condition);
        if(passCards.size()>0){
            return passCards.get(0);
        }else {
            return null;
        }
    }

    @Override
    public ApiResult extractProduct(ExtractProductDto extractProductDto, ExtractProductVo data) {
        //判断总数的手续费是否大于最低手续费，若小于则选择最低手续费，若不小于则选择总手续费
        //手续费
        BigDecimal changeAmountFee=new BigDecimal(extractProductDto.getNumber()).multiply(data.getExchangeRate()).multiply(data.getServiceCharge()).compareTo(new BigDecimal(data.getMin()))==-1?new BigDecimal(data.getMin()):new BigDecimal(extractProductDto.getNumber()).multiply(data.getExchangeRate()).multiply(data.getServiceCharge());
        //本金
        BigDecimal changeAmount=new BigDecimal(extractProductDto.getNumber()).multiply(data.getExchangeRate());
        ChangeAmountDto changeAmountDto=new ChangeAmountDto();
        changeAmountDto.setCustomerNo(extractProductDto.getCustomerNo());
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setTradeType(RecordPassCardEnum.EXTRACT_PAY.getCode());
        changeAmountDto.setReType(Constants.Retype.OUT);
        changeAmountDto.setOrderNo(IdWorker.get32UUID());
        //修改本金
        ApiResult apiResult = changeAmount(changeAmountDto);
        if(apiResult.hasFail()){
            throw new ServiceException(apiResult.getMsg());
        }
        changeAmountDto.setChangeAmount(changeAmountFee);
        changeAmountDto.setTradeType(RecordPassCardEnum.SERVICE_CHARGE.getCode());
        //修改手续费
        ApiResult apiResult1 = changeAmount(changeAmountDto);
        if(apiResult1.hasFail()){
            throw new ServiceException(apiResult1.getMsg());
        }
        //生成提取仓单
        PassCardExtractOrder order=new PassCardExtractOrder();
        ApiResult<List<ProductVo>> listApiResult = tradeFeign.productList(new ProductDto());
        if(listApiResult.hasFail()){
            return ApiResult.badParam("没有找到商品");
        }
        order.setProductTradeNo(listApiResult.getData().get(0).getProductTradeNo());
        order.setProductName(listApiResult.getData().get(0).getProductName());
        order.setId(IdWorker.getId());
        order.setCustomerNo(extractProductDto.getCustomerNo());
        order.setPrice(data.getExchangeRate());
        order.setAmount(Integer.valueOf(extractProductDto.getNumber()));
        order.setTotalPrice(changeAmount);
        order.setExtractCustomerNo(data.getExtractCustomerNo());
        order.setServiceCharge(changeAmountFee);
        order.setOrderNo(changeAmountDto.getOrderNo());
        order.setStatus(Constants.ExtractOrderStatus.WAIT);
        order.setCreateTime(new Date());
        order.setModifyTime(new Date());
        order.setFlag(new Byte("1"));
        passCardExtractOrderMapper.insertSelective(order);
        ApiResult<Integer> auditQuantity = contentFeign.getAuditQuantity();
        if (!auditQuantity.hasSuccess()) {
            return ApiResult.error();
        }
        //查询该用户历史未失败的数量
        Integer count = passCardExtractOrderMapper.selectUnfailCount(extractProductDto.getCustomerNo());
        if (count <= auditQuantity.getData()) {
            OperationDto operationDto = new OperationDto();
            List<Long> list = new ArrayList<>();
            list.add(order.getId());
            operationDto.setIds(list);
            operationDto.setStatus(Constants.ExtractOrderStatus.SUCCESS);
            operationDto.setOperatorName("system");
           return passCardExtractOrderService.operation(operationDto);
        }
        return ApiResult.success();
    }

    private void insertRecordPassCard(ChangeAmountDto changeAmountDto, PassCard passCard) {
        RecordPassCard recordMoney=new RecordPassCard();
        recordMoney.setRecordNo(IdWorker.get32UUID());
        recordMoney.setId(IdWorker.getId());
        //交易商编码
        recordMoney.setCustomerNo(passCard.getCustomerNo());
        //如果
        BigDecimal change=Constants.Retype.OUT.equals(changeAmountDto.getReType())? BigDecimal.ZERO.subtract(changeAmountDto.getChangeAmount()):changeAmountDto.getChangeAmount();
        //变动数额
        recordMoney.setChangeAmount(change.setScale(2,BigDecimal.ROUND_DOWN));
        //可用资金数额
        recordMoney.setBalance(passCard.getBalance());
        //交易类型
        recordMoney.setTradeType(changeAmountDto.getTradeType());
        //订单号
        recordMoney.setOrderNo(changeAmountDto.getOrderNo());
        //冻结类型 in：收入，out：支出
        recordMoney.setRetype(changeAmountDto.getReType());
        recordMoney.setCreateTime(new Date());
        recordMoney.setModifyTime(new Date());
        //是否有效
        recordMoney.setFlag(new Byte("1"));
        recordPassCardMapper.insertSelective(recordMoney);
    }

    /**
     * 判断一个值是否为负数，
     *
     * @param balance 判断的金额
     * @return true：负数 false 非负数
     */
    private boolean checkAmountNegative(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return true;
        } else {
            return false;
        }
    }
}
