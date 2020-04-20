package com.baibei.shiyi.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.common.dto.EmpowermentDto;
import com.baibei.shiyi.account.dao.CustomerBeanMapper;
import com.baibei.shiyi.account.dao.EmpowermentDetailMapper;
import com.baibei.shiyi.account.dao.RecordBeanMapper;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.model.*;
import com.baibei.shiyi.account.service.*;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordEmpowermentEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordPassCardEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.content.feign.bean.vo.PassCardExchangeRateVo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/10/29 16:56:57
* @description: CustomerBean服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CustomerBeanServiceImpl extends AbstractService<CustomerBean> implements ICustomerBeanService {

    @Autowired
    private CustomerBeanMapper tblCustomerBeanMapper;

    @Autowired
    private RecordBeanMapper recordBeanMapper;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private IRecordEmpowermentBalanceService recordEmpowermentBalanceService;

    @Autowired
    private IEmpowermentDetailService empowermentDetailService;

    @Autowired
    private EmpowermentDetailMapper empowermentDetailMapper;

    @Autowired
    private IAccountService accountService;


    @Autowired
    private IRecordBeanService recordBeanService;

    @Autowired
    private IPassCardService passCardService;

    @Override
    public ApiResult<CustomerBeanVo> getBalance(CustomerNoDto customerNoDto) {
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNoDto.getCustomerNo());
        List<CustomerBean> customerBeans=tblCustomerBeanMapper.selectByCondition(condition);
        CustomerBeanVo customerBeanVo=new CustomerBeanVo();
        for (int i = 0; i <customerBeans.size() ; i++) {
            CustomerBean customerBean=customerBeans.get(i);
            if(Constants.BeanType.CONSUMPTION.equals(customerBean.getBeanType())){
                customerBeanVo.setConsumptionBalance(customerBean.getBalance());
            }
            if(Constants.BeanType.EXCHANGE.equals(customerBean.getBeanType())){
                //暂时只有兑换积分存在待赋能积分
                customerBeanVo.setExchangeBalance(customerBean.getBalance());
                customerBeanVo.setExchangeEmpowermentBalance(customerBean.getEmpowermentBalance());
            }
            if(Constants.BeanType.SHIYI.equals(customerBean.getBeanType())){
                customerBeanVo.setShiyiBalance(customerBean.getBalance());
            }
            if(Constants.BeanType.MALLACCOUNT.equals(customerBean.getBeanType())){
                customerBeanVo.setMallAccountBalance(customerBean.getBalance());
            }
        }
        PassCard passCard=passCardService.findByCustomerNo(customerNoDto.getCustomerNo());
        if(passCard!=null){
            customerBeanVo.setPassCardBalance(passCard.getBalance());
        }
        Account account=accountService.checkAccount(customerNoDto.getCustomerNo());
        if(account!=null){
            customerBeanVo.setMoneyBalance(account.getBalance());
            customerBeanVo.setWithdrawBalance(account.getWithdrawableCash());
        }
        return ApiResult.success(customerBeanVo);
    }

    @Override
    public ApiResult<CustomerBean> getBalanceByType(String customerNo, String beanType) {
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("beanType",beanType);
        List<CustomerBean> customerBeans=tblCustomerBeanMapper.selectByCondition(condition);
        if(customerBeans.size()>0){
            return ApiResult.success(customerBeans.get(0));
        }else {
            return ApiResult.badParam("账户不存在");
        }
    }

    @Override
    public ApiResult changeAmount(ChangeCustomerBeanDto changeAmountDto) {
        log.info("修改积分的值为"+JSONObject.toJSONString(changeAmountDto));
        if((changeAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        CustomerBean customerBean=findByCustomerAndType(changeAmountDto.getCustomerNo(),changeAmountDto.getCustomerBeanType());
        if(customerBean==null){
            throw new ServiceException("账户不存在");
        }
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("balance",customerBean.getBalance());
        criteria.andEqualTo("customerNo",customerBean.getCustomerNo());
        criteria.andEqualTo("beanType",changeAmountDto.getCustomerBeanType());
        if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
            //收入
            customerBean.setBalance(customerBean.getBalance().add(changeAmountDto.getChangeAmount()));
        }else if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
            //支出
            customerBean.setBalance(customerBean.getBalance().subtract(changeAmountDto.getChangeAmount()));
        }else {
            throw new ServiceException("收支类型错误");
        }
        if (checkAmountNegative(customerBean.getBalance())) {
            //判断是否为负数，为负数则返回余额不足
            throw new ServiceException("积分余额不足");
        }
        customerBean.setModifyTime(new Date());
        //插入一条积分流水
        insertRecordBean(changeAmountDto,customerBean);
        //判断是否为修改成功
        if (!updateByConditionSelective(customerBean,condition)) {
            throw new ServiceException("资金扣除失败");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult checkBalanceByType(String fundType, String customerNo, BigDecimal changeAmount) {
        CustomerBean customerBean = findByCustomerAndType(customerNo, fundType);
        if(customerBean!=null){
            if(customerBean.getBalance().compareTo(changeAmount)<0){
                if(Constants.BeanType.MALLACCOUNT.equals(fundType)){
                    return ApiResult.badParam("余额不足");
                }
                return ApiResult.badParam("积分余额不足");
            }else {
                return  ApiResult.success();
            }
        }else {
            return ApiResult.badParam("用户不存在");
        }
    }

    @Override
    public ApiResult empowerment(EmpowermentDto empowermentDto) {
        log.info("赋能通证传输过来的数据为"+JSONObject.toJSONString(empowermentDto));
        ApiResult<PassCardExchangeRateVo> integerApiResult = contentFeign.passCardExchangeRate();
        if(integerApiResult.hasFail()){
            return integerApiResult;
        }

        BigDecimal exchangeRate=integerApiResult.getData().getExchangeRate();
        CustomerBean customerBean = findByCustomerAndType(empowermentDto.getCustomerNo(), Constants.BeanType.EXCHANGE);
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("balance",customerBean.getBalance());
        criteria.andEqualTo("empowermentBalance",customerBean.getEmpowermentBalance());
        criteria.andEqualTo("beanType",customerBean.getBeanType());
        criteria.andEqualTo("customerNo",customerBean.getCustomerNo());
        BigDecimal changeAmout=exchangeRate.multiply(new BigDecimal(empowermentDto.getNumber())).setScale(2,BigDecimal.ROUND_DOWN);
        if(changeAmout.compareTo(customerBean.getEmpowermentBalance())>0){
            return ApiResult.badParam("待赋能积分不足");
        }
        customerBean.setBalance(customerBean.getBalance().subtract(changeAmout));
        customerBean.setEmpowermentBalance(customerBean.getEmpowermentBalance().subtract(changeAmout));
        customerBean.setModifyTime(new Date());
        if(!updateByConditionSelective(customerBean, condition)){
            throw new ServiceException("网络异常，请重试");
        }

        ChangeCustomerBeanDto changeAmountDto=new ChangeCustomerBeanDto();
        changeAmountDto.setTradeType(RecordBeanTradeTypeEnum.EXCHANGE_PASSCHECK_PAY.getCode());
        changeAmountDto.setReType(Constants.Retype.OUT);
        changeAmountDto.setChangeAmount(changeAmout);
        changeAmountDto.setOrderNo(IdWorker.get32UUID());
        changeAmountDto.setCustomerNo(customerBean.getCustomerNo());
        changeAmountDto.setCustomerBeanType(Constants.BeanType.EXCHANGE);
        //插入兑换积分流水
        insertRecordBean(changeAmountDto,customerBean);
        changeAmountDto.setTradeType(RecordEmpowermentEnum.EMPOWERMENT_PASS_CARD_PAY.getCode());
        //释放通证流水
        recordEmpowermentBalanceService.insertRecord(changeAmountDto,customerBean);
        //扣减积分完成接下来处理通证
        ChangeAmountDto changeAmountDto1=BeanUtil.copyProperties(changeAmountDto,ChangeAmountDto.class);
        changeAmountDto1.setReType(Constants.Retype.IN);
        changeAmountDto1.setTradeType(RecordPassCardEnum.EMPOWERMENT_PASS_CARD_INCOME.getCode());
        changeAmountDto1.setChangeAmount(new BigDecimal(empowermentDto.getNumber()));
        ApiResult apiResult = passCardService.changeAmount(changeAmountDto1);
        if(apiResult.hasFail()){
            throw new ServiceException(apiResult.getMsg());
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult dayRelease() {
        //找到所有账户
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("beanType",Constants.BeanType.EXCHANGE);
        List<CustomerBean> customerBeans=tblCustomerBeanMapper.selectByCondition(condition);
        for (int i = 0; i <customerBeans.size() ; i++) {
            //找到当天增量
            ApiResult<BigDecimal> DailyIncrement=recordBeanService.getDailyIncrement(customerBeans.get(i).getCustomerNo());
            BigDecimal todayRelease=BigDecimal.ZERO;
            //今天的释放量已拿到，并且已经插入了今日份的释放明细
            List<EmpowermentDetail> empowermentDetails=empowermentDetailService.findAllByCustomerNo(customerBeans.get(i).getCustomerNo());
            if(DailyIncrement.hasSuccess()&& DailyIncrement.getData().compareTo(new BigDecimal("3.65"))>0) {
                //成功就处理今天的释放量，失败则处理过去的即可
                //每日释放量
                BigDecimal dailyRelease=DailyIncrement.getData().divide(new BigDecimal(365),2,BigDecimal.ROUND_HALF_UP);
                //剩余释放量
                BigDecimal surplusRelease=DailyIncrement.getData().subtract(dailyRelease);

                todayRelease=todayRelease.add(dailyRelease);
                EmpowermentDetail empowermentDetail=new EmpowermentDetail();
                empowermentDetail.setDayRelease(dailyRelease);
                empowermentDetail.setTotalRelease(DailyIncrement.getData());
                empowermentDetail.setSurplusRelease(surplusRelease);
                empowermentDetail.setCustomerNo(customerBeans.get(i).getCustomerNo());
                empowermentDetail.setCreateTime(new Date());
                empowermentDetail.setModifyTime(new Date());
                empowermentDetail.setStatus("enable");
                empowermentDetail.setFlag(new Byte("1"));
                empowermentDetail.setId(IdWorker.getId());
                empowermentDetailMapper.insertSelective(empowermentDetail);
            }else {
                log.info("每日释放待赋能出错（今日份增量额度未处理）,用户编号为:{}，时间为：{}",customerBeans.get(i).getCustomerNo(),new Date());
            }
            if(empowermentDetails.size()>0 || todayRelease.compareTo(BigDecimal.ZERO)>0){
                for (int j = 0; j <empowermentDetails.size() ; j++) {
                    EmpowermentDetail empowermentDetail = empowermentDetails.get(j);
                    if(empowermentDetail.getDayRelease().compareTo(empowermentDetail.getSurplusRelease())>0){
                        //如果每日释放大于剩余释放量,则直接释放剩余的，并且将状态置为失效
                        empowermentDetail.setStatus("disable");
                        todayRelease=todayRelease.add(empowermentDetail.getSurplusRelease());
                        empowermentDetail.setSurplusRelease(BigDecimal.ZERO);
                    }else {
                        //如果不是则将剩余释放量减少每日释放的额度
                        todayRelease=todayRelease.add(empowermentDetail.getDayRelease());
                        empowermentDetail.setSurplusRelease(empowermentDetail.getSurplusRelease().subtract(empowermentDetail.getDayRelease()));
                    }
                    empowermentDetail.setModifyTime(new Date());
                }
                //开始处理每日释放额度
                Condition condition1=new Condition(CustomerBean.class);
                Example.Criteria criteria1=buildValidCriteria(condition1);
                criteria1.andEqualTo("beanType",Constants.BeanType.EXCHANGE);
                criteria1.andEqualTo("balance",customerBeans.get(i).getBalance());
                criteria1.andEqualTo("empowermentBalance",customerBeans.get(i).getEmpowermentBalance());
                criteria1.andEqualTo("customerNo",customerBeans.get(i).getCustomerNo());
                customerBeans.get(i).setEmpowermentBalance(customerBeans.get(i).getEmpowermentBalance().add(todayRelease));
                customerBeans.get(i).setModifyTime(new Date());
                int flag = tblCustomerBeanMapper.updateByCondition(customerBeans.get(i), condition1);
                if(flag==1){
                    //此时为修改成功，则进行修改释放明细部分
                    if(empowermentDetails.size()>0){
                        empowermentDetailService.updateMore(empowermentDetails);
                    }
                    ChangeCustomerBeanDto changeAmountDto=new ChangeCustomerBeanDto();
                    changeAmountDto.setChangeAmount(todayRelease);
                    changeAmountDto.setTradeType(RecordEmpowermentEnum.DAY_RELEASE.getCode());
                    changeAmountDto.setCustomerNo(customerBeans.get(i).getCustomerNo());
                    changeAmountDto.setReType(Constants.Retype.IN);
                    changeAmountDto.setOrderNo(IdWorker.get32UUID());
                    changeAmountDto.setCustomerBeanType(Constants.BeanType.EXCHANGE);
                    //插入流水
                    recordEmpowermentBalanceService.insertRecord(changeAmountDto,customerBeans.get(i));
                }else {
                    log.info("每日释放额度处理失败，全部分处理失败，用户编号为：{}",customerBeans.get(i).getCustomerNo());
                }
            }else {
                log.info("用户：{} 不用处理每日释放额度",customerBeans.get(i).getCustomerNo());
            }
        }
        return ApiResult.success();
    }

    public CustomerBean findByCustomerAndType(String customerNo,String customerBeanType){
        if(StringUtils.isEmpty(customerNo)){
            return null;
        }
        if(StringUtils.isEmpty(customerBeanType)){
            return null;
        }
        Condition condition=new Condition(CustomerBean.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("beanType",customerBeanType);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerBean> customerBeans=tblCustomerBeanMapper.selectByCondition(condition);
        if(customerBeans.size()>0){
            return customerBeans.get(0);
        }else {
            return null;
        }
    }
    /**
     * 插入一条可用资金修改流水
     * @param frozenAmountDto
     * @param customerBean
     */
    private void insertRecordBean(ChangeCustomerBeanDto frozenAmountDto, CustomerBean customerBean) {
        RecordBean recordBean=new RecordBean();
        String recordNo=IdWorker.get32UUID();
        recordBean.setId(IdWorker.getId());
        recordBean.setRecordNo(recordNo);
        //交易商编码
        recordBean.setCustomerNo(frozenAmountDto.getCustomerNo());
        //如果
        BigDecimal change=frozenAmountDto.getReType().equals(Constants.Retype.IN)? frozenAmountDto.getChangeAmount():BigDecimal.ZERO.subtract(frozenAmountDto.getChangeAmount());
        //变动数额
        recordBean.setChangeAmount(change.setScale(2,BigDecimal.ROUND_DOWN));
        //可用资金数额
        recordBean.setBalance(customerBean.getBalance());
        //交易类型
        recordBean.setTradeType(frozenAmountDto.getTradeType());
        //订单号
        recordBean.setOrderNo(frozenAmountDto.getOrderNo());
        //冻结类型 1：支出，2：收入
        recordBean.setRetype(frozenAmountDto.getReType());
        recordBean.setBeanType(frozenAmountDto.getCustomerBeanType());
        recordBean.setCreateTime(new Date());
        recordBean.setModifyTime(new Date());
        //是否有效
        recordBean.setFlag(new Byte("1"));
        recordBeanMapper.insertSelective(recordBean);
    }
    /**
     * 判断一个值是否为负数，
     *
     * @param balance 判断的金额
     * @return true：负数 false 非负数
     */
    private boolean checkAmountNegative(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) <0) {
            return true;
        } else {
            return false;
        }
    }
}
