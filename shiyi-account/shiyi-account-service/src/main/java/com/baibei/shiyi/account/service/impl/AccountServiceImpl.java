package com.baibei.shiyi.account.service.impl;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.common.dto.ConsignmentDto;
import com.baibei.shiyi.account.common.dto.InsertFundPasswordDto;
import com.baibei.shiyi.account.common.vo.AccountDetailVo;
import com.baibei.shiyi.account.dao.*;
import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.FundDetailVo;
import com.baibei.shiyi.account.feign.bean.vo.FundInformationVo;
import com.baibei.shiyi.account.feign.bean.vo.SumWithdrawAndDepositVo;
import com.baibei.shiyi.account.model.*;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.cash.feign.base.dto.CashChangeAmountDto;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.enumeration.FreezingAmountTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import com.baibei.shiyi.common.tool.utils.MD5Util;
import com.baibei.shiyi.content.feign.base.IContentBase;
import com.baibei.shiyi.content.feign.bean.vo.ConsignmentVo;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import com.baibei.shiyi.publicc.feign.client.SmsFeign;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementMetadataMsg;
import com.baibei.shiyi.user.feign.bean.dto.UpdatePasswordDto;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/05/24 10:40:51
 * @description: Account服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AccountServiceImpl extends AbstractService<Account> implements IAccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RecordFreezingAmountMapper recordFreezingAmountMapper;

    @Autowired
    private RecordMoneyMapper recordMoneyMapper;

    @Autowired
    private CustomerBeanMapper customerBeanMapper;

    @Autowired
    private ConsignmentOrderMapper consignmentOrderMapper;

    @Autowired
    private ICustomerBeanService customerBeanService;

    @Autowired
    private CustomerFeign customerFeign;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SmsFeign smsFeign;

    @Autowired
    private IContentBase contentBase;

    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Value("${rocketmq.settlement.clean.topics}")
    private String settlementCleanTopics;

    @Value("${shiyi.consignment}")
    private String consignment;

    @Override
    public ApiResult<String> register(CustomerNoDto customerNoDto) {
        //注册需要向多个表插入数据
        //创建一条交易账户
        Account account = new Account();
        account.setId(IdWorker.getId());
        account.setCustomerNo(customerNoDto.getCustomerNo());
        account.setCreateTime(new Date());
        account.setModifyTime(new Date());
        account.setFlag(new Byte("1"));
        accountMapper.insertSelective(account);
        //创建消费积分账户
        CustomerBean goldBean=new CustomerBean();
        goldBean.setId(IdWorker.getId());
        goldBean.setBeanType(Constants.BeanType.CONSUMPTION);
        goldBean.setCustomerNo(customerNoDto.getCustomerNo());
        goldBean.setBalance(BigDecimal.ZERO);
        goldBean.setCreateTime(new Date());
        goldBean.setModifyTime(new Date());
        goldBean.setFlag(new Byte(Constants.Flag.VALID));
        customerBeanMapper.insertSelective(goldBean);
        //创建兑换积分账户
        CustomerBean redBean=new CustomerBean();
        redBean.setId(IdWorker.getId());
        redBean.setBeanType(Constants.BeanType.EXCHANGE);
        redBean.setCustomerNo(customerNoDto.getCustomerNo());
        redBean.setBalance(BigDecimal.ZERO);
        redBean.setCreateTime(new Date());
        redBean.setModifyTime(new Date());
        redBean.setFlag(new Byte(Constants.Flag.VALID));
        customerBeanMapper.insertSelective(redBean);
        //创建商城账户
        CustomerBean mallAccount=new CustomerBean();
        mallAccount.setId(IdWorker.getId());
        mallAccount.setBeanType(Constants.BeanType.MALLACCOUNT);
        mallAccount.setCustomerNo(customerNoDto.getCustomerNo());
        mallAccount.setBalance(BigDecimal.ZERO);
        mallAccount.setCreateTime(new Date());
        mallAccount.setModifyTime(new Date());
        mallAccount.setFlag(new Byte(Constants.Flag.VALID));
        customerBeanMapper.insertSelective(mallAccount);
        //创建兑换积分账户
        CustomerBean shiyiBean=new CustomerBean();
        shiyiBean.setId(IdWorker.getId());
        shiyiBean.setBeanType(Constants.BeanType.SHIYI);
        shiyiBean.setCustomerNo(customerNoDto.getCustomerNo());
        shiyiBean.setBalance(BigDecimal.ZERO);
        shiyiBean.setCreateTime(new Date());
        shiyiBean.setModifyTime(new Date());
        shiyiBean.setFlag(new Byte(Constants.Flag.VALID));
        customerBeanMapper.insertSelective(shiyiBean);
        return ApiResult.success("账户开户成功");
    }

    @Override
    public ApiResult<AccountVo> findAccount(CustomerNoDto customerNoDto) {
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNoDto.getCustomerNo());
        List<Account> mallAccounts=accountMapper.selectByCondition(condition);
        if(mallAccounts.size()>0){
            AccountVo accountVo=BeanUtil.copyProperties(mallAccounts.get(0),AccountVo.class);
            accountVo.setFundPassword(StringUtils.isEmpty(mallAccounts.get(0).getPassword())?"0":"1");
            return ApiResult.success(accountVo);
        }else {
            return ApiResult.badParam("账户不存在");
        }
    }

    /**
     * 冻结资金
     * @param frozenAmountDto
     * @param account
     * @return
     */
    @Override
    public ApiResult<String> frozenAmount(ChangeAmountDto frozenAmountDto, Account account) {
        log.info("冻结金额传入的值为"+frozenAmountDto);
        if((frozenAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        ApiResult result = new ApiResult();

        //添加冻结资金后的冻结资金数额
        BigDecimal freezingAmount = account.getFreezingAmount().add(frozenAmountDto.getChangeAmount());
        //冻结资金后的可用资金
        BigDecimal balance = account.getBalance().subtract(frozenAmountDto.getChangeAmount());
        if (checkAmountNegative(balance)) {
            //判断是否为负数，为负数则返回余额不足
            result.setCode(ResultEnum.BALANCE_NOT_ENOUGH.getCode());
            result.setMsg(ResultEnum.BALANCE_NOT_ENOUGH.getMsg());
            throw new ServiceException("余额不足");
        }
        //可提现金额
        BigDecimal withDrawCash=account.getWithdrawableCash();

        //对比可提现金额与当前可用资金
        if(withDrawCash.compareTo(balance)>0){
            //将操作了的可提资金放入缓存中
            String key=MessageFormat.format(RedisConstant.ACCOUNT_WITHDRAW_AMOUT,account.getCustomerNo()+frozenAmountDto.getOrderNo());
            redisUtil.set(key,withDrawCash.subtract(balance)+"");
            //如果可提现金额大于可用资金，则当前可用资金就是可提现金额
            withDrawCash=balance;

        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setFreezingAmount(freezingAmount);
        newAccount.setWithdrawableCash(withDrawCash);
        newAccount.setModifyTime(new Date());
        newAccount.setCustomerNo(account.getCustomerNo());
        //插入一条冻结资金修改流水
        insertRecordFreezingAmount(frozenAmountDto, newAccount);
//        //插入一条可用资金修改流水
//        insertRecordMoney(frozenAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw new ServiceException("资金扣除失败");
        }

        return ApiResult.success();
    }

    @Override
    public ApiResult thawAmountByTrade(ChangeAmountDto frozenAmountDto) {
        log.info("交易解冻传入的值为"+frozenAmountDto);
        Account account=checkAccount(frozenAmountDto.getCustomerNo());
        if(account==null){
            //没找到用户
            throw new ServiceException("帐户不存在");
        }
        if(Constants.Retype.OUT.equals(frozenAmountDto.getReType())){
            //减少冻结资金的冻结资金数额
            BigDecimal freezingAmount = account.getFreezingAmount().subtract(frozenAmountDto.getChangeAmount());
            // 总余额减少
            BigDecimal totalBalance = account.getTotalBalance().subtract(frozenAmountDto.getChangeAmount());
            if (checkAmountNegative(freezingAmount)) {
                //判断是否为负数，为负数则返回冻结资金小于冻结数额
                throw  new ServiceException("冻结金额不足,无法进行解冻操作");
            }
            if (checkAmountNegative(totalBalance)) {
                throw  new ServiceException("总余额不足");
            }
            //乐观锁实现方式
            Condition condition=new Condition(Account.class);
            Example.Criteria criteria=condition.createCriteria();
            criteria.andEqualTo("customerNo",account.getCustomerNo());
            criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
            criteria.andEqualTo("totalBalance",account.getTotalBalance());
            Account newAccount=new Account();
            newAccount.setFreezingAmount(freezingAmount);
            newAccount.setTotalBalance(totalBalance);
            newAccount.setModifyTime(new Date());
            newAccount.setBalance(account.getBalance());
            newAccount.setWithdrawableCash(account.getWithdrawableCash());
            newAccount.setCustomerNo(account.getCustomerNo());
            //插入一条冻结资金修改流水
            insertRecordFreezingAmount(frozenAmountDto, newAccount);
            //挂牌买入解冻
            insertRecordMoney(frozenAmountDto,newAccount);
            //判断是否为修改成功
            if (!updateByConditionSelective(newAccount,condition)) {
                throw  new ServiceException("资金操作失败！");
            }
            return ApiResult.success();
        }else{
            throw  new ServiceException("参数输入错误！");
        }
    }

    @Override
    public ApiResult changeAmount(ChangeAmountDto changeAmountDto) {
        log.info("修改金额传入的值为"+changeAmountDto);
        if((changeAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        Account account=checkAccount(changeAmountDto.getCustomerNo());
        if(account==null){
            throw new ServiceException("帐户不存在");
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
            //支出
            account.setTotalBalance(account.getTotalBalance().subtract(changeAmountDto.getChangeAmount()));
            account.setBalance(account.getBalance().subtract(changeAmountDto.getChangeAmount()));
        }else if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
            //收入
            account.setBalance(account.getBalance().add(changeAmountDto.getChangeAmount()));
            account.setTotalBalance(account.getTotalBalance().add(changeAmountDto.getChangeAmount()));
        }else {
            throw new ServiceException("收支类型错误");
        }
        if (checkAmountNegative(account.getBalance())) {
            //判断是否为负数，为负数则返回余额不足
            throw new ServiceException("现金余额不足");
        }
        //判断当前余额是否小于可出金余额
        //对比可提现金额与当前可用资金
        if(account.getWithdrawableCash().compareTo(account.getBalance())>0){
            //如果可提现金额大于可用资金，则当前可用资金就是可提现金额
            account.setWithdrawableCash(account.getBalance());
        }
        account.setModifyTime(new Date());
        log.info("修改后的可提现金额为"+account.getWithdrawableCash());
        //插入一条可用资金修改流水
        insertRecordMoney(changeAmountDto,account);
        //判断是否为修改成功
        if (!updateByConditionSelective(account,condition)) {
            throw new ServiceException("资金扣除失败");
        }
        return ApiResult.success();
    }

    /**
     * 插入一条可用资金修改流水
     * @param changeAmountDto
     * @param account
     */
    private void insertRecordMoney(ChangeAmountDto changeAmountDto, Account account) {
        RecordMoney recordMoney=new RecordMoney();
        recordMoney.setRecordNo(IdWorker.get32UUID());
        recordMoney.setId(IdWorker.getId());
        //交易商编码
        recordMoney.setCustomerNo(account.getCustomerNo());
        //如果
        BigDecimal change=Constants.Retype.OUT.equals(changeAmountDto.getReType())? BigDecimal.ZERO.subtract(changeAmountDto.getChangeAmount()):changeAmountDto.getChangeAmount();
        //变动数额
        recordMoney.setChangeAmount(change.setScale(2,BigDecimal.ROUND_DOWN));
        //可用资金数额
        recordMoney.setBalance(account.getBalance());
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
        recordMoneyMapper.insertSelective(recordMoney);
    }

    /**
     * 判断该帐户是否存在
     *
     * @param customerNo 交易商编码
     * @return
     */
    @Override
    public Account checkAccount(String customerNo) {
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        //先查询账户信息存不存在
        List<Account> accounts=accountMapper.selectByCondition(condition);
        if(accounts.size()>0){
            return accounts.get(0);
        }else {
            return null;
        }
    }

    @Override
    public ApiResult<String> thawAmount(ChangeAmountDto frozenAmountDto, Account account) {
        log.info("挂单撤回解冻传入的值为"+frozenAmountDto);
        if((frozenAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        //减少冻结资金的冻结资金数额
        BigDecimal freezingAmount = account.getFreezingAmount().subtract(frozenAmountDto.getChangeAmount());
        //解冻资金后的可用资金
        BigDecimal balance = account.getBalance().add(frozenAmountDto.getChangeAmount());
        if (checkAmountNegative(freezingAmount)) {
            //判断是否为负数，为负数则返回冻结资金小于冻结数额
            throw new ServiceException("冻结金额不足,无法进行解冻操作");
        }
        String key=MessageFormat.format(RedisConstant.ACCOUNT_WITHDRAW_AMOUT,account.getCustomerNo()+frozenAmountDto.getOrderNo());
        String withdraw=redisUtil.get(key);
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("freezingAmount",account.getFreezingAmount());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        if(withdraw!=null){
            log.info("上次挂单的可提金额为"+withdraw);
            if (new BigDecimal(withdraw).compareTo(frozenAmountDto.getChangeAmount())<0){
                //如果小于撤回金额
                account.setWithdrawableCash(account.getWithdrawableCash().add(new BigDecimal(withdraw)));
            }else {
                //如果大于撤回金额
                account.setWithdrawableCash(account.getWithdrawableCash().add(frozenAmountDto.getChangeAmount()));
            }
            log.info("撤回后的可提金额为"+account.getWithdrawableCash());
        }
        redisUtil.delete(key);
        //乐观锁实现方式
        Account newAccount=new Account();
        newAccount.setBalance(balance);
        newAccount.setFreezingAmount(freezingAmount);
        newAccount.setModifyTime(new Date());
        newAccount.setWithdrawableCash(account.getWithdrawableCash());
        newAccount.setCustomerNo(account.getCustomerNo());
        //插入一条冻结资金修改流水
        insertRecordFreezingAmount(frozenAmountDto, newAccount);
        //插入一条可用资金修改流水
//        ChangeAmountDto changeAmountDto=BeanUtil.copyProperties(frozenAmountDto,ChangeAmountDto.class);
//        changeAmountDto.setReType(new Byte("2"));
//        changeAmountDto.setTradeType(FundTradeTypeEnum.REPEAL_MONEY.getCode());
//        insertRecordMoney(frozenAmountDto,newAccount);
        //判断是否为修改成功
        if (!updateByConditionSelective(newAccount,condition)) {
            throw new ServiceException("网络异常，请稍后再试！");
        }
        return ApiResult.success();
    }

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    @Override
    public ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto) {
        ApiResult result=new ApiResult();
        Account account=checkAccount(updatePasswordDto.getCustomerNo());
        String password=MD5Util.md5Hex(updatePasswordDto.getOldPassword(),account.getSalt());
        if(!password.equals(account.getPassword())){
            result.setCode(ResultEnum.ACCOUNT_PASSWORD_ERRO.getCode());
            result.setMsg(ResultEnum.ACCOUNT_PASSWORD_ERRO.getMsg());
            return  result;
        }
        account.setPassword(MD5Util.md5Hex(updatePasswordDto.getNewPassword(),account.getSalt()));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public ApiResult<FundInformationVo> fundInformation(CustomerNoDto customerNoDto) {
        ApiResult result=null;
        Account account=checkAccount(customerNoDto.getCustomerNo());
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        FundInformationVo fundInformationVo=new FundInformationVo();
        //从交易服务获取持有市值，浮动盈亏
//        ApiResult<StatisticsVo> holdMarketValue=customerHoldFeign.marketValue(customerNoDto.getCustomerNo());
        fundInformationVo=BeanUtil.copyProperties(account,FundInformationVo.class);
        //持仓市值
//        fundInformationVo.setHoldMarketValue(holdMarketValue.getData().getMarketValue());
//        //总资产 总资金+持仓市值
//        fundInformationVo.setTotalAssets(fundInformationVo.getTotalBalance().add(holdMarketValue.getData().getMarketValue()));
//        //总浮动盈亏
//        fundInformationVo.setFloatProfitAndLoss(holdMarketValue.getData().getProfitAndLoss());
        return ApiResult.success(fundInformationVo);
    }
    @Override
    public ApiResult<FundDetailVo> fundDetail(CustomerNoDto customerNoDto) {
        //从资金信息方法获取父类中的一些字段信息
        ApiResult<FundInformationVo> fundInformationVoApiResult=fundInformation(customerNoDto);
        FundDetailVo fundDetailVo=BeanUtil.copyProperties(fundInformationVoApiResult.getData(),FundDetailVo.class);
        ApiResult result=new ApiResult();
        //设置子类的一些信息
        Account account=checkAccount(customerNoDto.getCustomerNo());
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        //冻结资金
        fundDetailVo.setFreezingAmount(account.getFreezingAmount());
        //交易商编号
        fundDetailVo.setCustomerNo(customerNoDto.getCustomerNo());
        //获取期初资金
        String key=MessageFormat.format(RedisConstant.ACCOUNT_INIT_FUND,customerNoDto.getCustomerNo());
        redisUtil.set(key,"10000");
        String initFund=redisUtil.get(key);
        fundDetailVo.setInitFund(new BigDecimal(initFund));
        //TODO 从出入金服务获取总出入金金额
        return ApiResult.success(fundDetailVo);
    }

    /**
     * 从用户服务通过手机号查找到交易商编码
     * @param mobile
     * @return
     */
    @Override
    public ApiResult<String> findCustomerNoByMobile(String mobile) {
        ApiResult<String>  result=customerFeign.findCustomerNoByMobile(mobile);
        return result;
    }

    @Override
    public boolean checkMobileVerificationCode(String mobile, String mobileVerificationCode,String mobileVerificationType) {
        ValidateCodeDto validateCodeDto=new ValidateCodeDto();
        validateCodeDto.setPhone(mobile);
        validateCodeDto.setCode(mobileVerificationCode);
        validateCodeDto.setType(mobileVerificationType);
        ApiResult result=smsFeign.validateCode(validateCodeDto);
        if(200==result.getCode().intValue()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ApiResult<String> forgetPassword(String customerNo, String password) {
        ApiResult result=new ApiResult();
        Account account=checkAccount(customerNo);
        if(account==null){
            result.setCode(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.ACCOUNT_NOT_EXIST.getMsg());
            return result;
        }
        //重新生成盐值
        String salt=MD5Util.getRandomSalt(10);
        account.setSalt(salt);
        account.setPassword(MD5Util.md5Hex(password,salt));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public ApiResult insertFundPassword(InsertFundPasswordDto fundPasswordDto) {
        Account account=checkAccount(fundPasswordDto.getCustomerNo());
        if(account==null){
            return ApiResult.badParam("交易账号为空");
        }
        //生成资金密码
        String salt=MD5Util.getRandomSalt(10);
        account.setSalt(salt);
        account.setPassword(MD5Util.md5Hex(fundPasswordDto.getPassword(),salt));
        account.setModifyTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);
        return ApiResult.success();
    }

    @Override
    public ApiResult changeMoneyAndBean(ChangeMoneyAndBeanDto changeMoneyAndBeanDto) {
        ChangeAmountDto moneyChangeAmountDto=new ChangeAmountDto();
        moneyChangeAmountDto.setCustomerNo(changeMoneyAndBeanDto.getCustomerNo());
        moneyChangeAmountDto.setOrderNo(changeMoneyAndBeanDto.getOrderNo());
        moneyChangeAmountDto.setTradeType(changeMoneyAndBeanDto.getMoneyTradeType());
        moneyChangeAmountDto.setReType(changeMoneyAndBeanDto.getMoneyRetype());
        moneyChangeAmountDto.setChangeAmount(changeMoneyAndBeanDto.getMoneyChangeAmount());
        ApiResult apiResult = changeAmount(moneyChangeAmountDto);
        if(!apiResult.hasSuccess()){
            throw new ServiceException("同时修改资金以及积分——修改金额失败");
        }
        if(!StringUtils.isEmpty(changeMoneyAndBeanDto.getChangeBeanDtos())){
            List<ChangeBeanDto> changeBeanDtos = changeMoneyAndBeanDto.getChangeBeanDtos();
            for (int i = 0; i <changeBeanDtos.size() ; i++) {
                ChangeBeanDto changeBeanDto=changeBeanDtos.get(i);
                ChangeCustomerBeanDto changeCustomerBeanDto=new ChangeCustomerBeanDto();
                changeCustomerBeanDto.setCustomerBeanType(changeBeanDto.getBeanType());
                changeCustomerBeanDto.setCustomerNo(changeMoneyAndBeanDto.getCustomerNo());
                changeCustomerBeanDto.setOrderNo(changeMoneyAndBeanDto.getOrderNo());
                changeCustomerBeanDto.setTradeType(changeBeanDto.getBeanTradeType());
                changeCustomerBeanDto.setReType(changeBeanDto.getBeanReType());
                changeCustomerBeanDto.setChangeAmount(changeBeanDto.getBeanChangeAmout());
                ApiResult apiResult1 = customerBeanService.changeAmount(changeCustomerBeanDto);
                if(!apiResult1.hasSuccess()){
                    throw new ServiceException("同时修改资金以及积分——修改积分失败");
                }
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult checkFundPassword(String customerNo, String password) {
        Account account = checkAccount(customerNo);
        if(account==null){
            return ApiResult.badParam("用户不存在");
        }
        if(account.getPassword().equals(MD5Util.md5Hex(password,account.getSalt()))){
            return ApiResult.success();
        }else {
            return ApiResult.error();
        }
    }

    @Override
    public ApiResult consignmentApplication(ConsignmentDto consignmentDto) {
        //判断是否是寄售时间
        ApiResult<ConsignmentVo> consignmentTime = contentBase.consignmentConfiguration();
        if(consignmentTime.hasSuccess()){
            ConsignmentVo consignmentTimeVo = consignmentTime.getData();
            boolean flag = compareTime(consignmentTimeVo.getStartTime(),consignmentTimeVo.getEndTime());
            if (!flag) {
                return ApiResult.error("当前非寄售时间");
            }
        }
        //判断用户状态
        CustomerNoDto customerNoDto =new CustomerNoDto();
        customerNoDto.setCustomerNo(consignmentDto.getCustomerNo());
        ApiResult<CustomerVo> apiResult = customerFeign.findUserByCustomerNo(customerNoDto);
        if(apiResult.hasSuccess()){
            if(apiResult.getData().getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_CONSIGNMENT.getCode()) !=-1){
                return ApiResult.error("寄售失败，请联系客服");
            }
        }
        //判断用户余额是否充足
        CheckByFundTypes checkByFundTypes = new CheckByFundTypes();

        List<CheckFundType> checkFundTypeList = Lists.newArrayList();
        CheckFundType checkFundType = new CheckFundType();
        checkFundType.setChangeAmount(consignmentDto.getPrice());
        checkFundType.setFundType(Constants.BeanType.EXCHANGE);
        checkFundTypeList.add(checkFundType);

        checkByFundTypes.setCustomerNo(consignmentDto.getCustomerNo());
        checkByFundTypes.setCheckFundTypes(checkFundTypeList);

        ApiResult apiResult1 = checkByFundType(checkByFundTypes);
        if(apiResult1.hasSuccess()){
            //生成寄售订单
            long orderNo = IdWorker.getId();
            ConsignmentOrder consignmentOrder = new ConsignmentOrder();
            consignmentOrder.setId(orderNo);
            consignmentOrder.setOrderNo(String.valueOf(IdWorker.getId()));
            consignmentOrder.setCreateTime(new Date());
            consignmentOrder.setModifyTime(new Date());
            consignmentOrder.setFee(consignmentDto.getFee());
            consignmentOrder.setPrice(consignmentDto.getPrice());

            List<ChangeMultipleFundDto> multipleFundDtoList = Lists.newArrayList();

            ChangeMultipleFundDto  changeMultipleFundDto = new ChangeMultipleFundDto();
            changeMultipleFundDto.setOrderNo(String.valueOf(orderNo));
            changeMultipleFundDto.setCustomerNo(consignmentDto.getCustomerNo());

            List<ChangeMultipleFundType> changeMultipleFundTypeList = Lists.newArrayList();

            ChangeMultipleFundType changeMultipleFundType = new ChangeMultipleFundType();
            changeMultipleFundType.setChangeAmount(consignmentDto.getPrice());
            changeMultipleFundType.setFundType(Constants.BeanType.EXCHANGE);
            changeMultipleFundType.setRetype(Constants.Retype.OUT);
            changeMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.EXCHANGE_CONSIGNMENT_SERVICE_FEE.getCode());
            changeMultipleFundTypeList.add(changeMultipleFundType);
            changeMultipleFundDto.setChangeMultipleFundTypeList(changeMultipleFundTypeList);

            ApiResult result = changeMultipleFund(multipleFundDtoList);
            if(result.hasSuccess()){
                consignmentOrder.setStatus("success");
                consignmentOrderMapper.insertSelective(consignmentOrder);
                //价钱成功后通知清算服务
                List<SettlementMetadataMsg> settlementMetadataMsgList = new ArrayList<>();
                //客户余额增加变动
                SettlementMetadataMsg customerMsg = new SettlementMetadataMsg();
                customerMsg.setCustomerNo(consignmentDto.getCustomerNo());
                customerMsg.setAmount(consignmentDto.getPrice());
                /*customerMsg.setTradeType(TradeMoneyTradeTypeEnum.CONSUMPTION_PAY.getCode());*/
                customerMsg.setOrderNo(String.valueOf(orderNo));
                /*customerMsg.setEffectField(Constants.SettlementEffectField.PROFITAMOUNT);*/
                customerMsg.setEventTime(new Date());
                settlementMetadataMsgList.add(customerMsg);

                //系统寄售账户余额减少变动
                SettlementMetadataMsg distributorMsg = new SettlementMetadataMsg();
                distributorMsg.setCustomerNo(consignment);
                distributorMsg.setAmount(consignmentDto.getPrice());
                /*distributorMsg.setTradeType(TradeMoneyTradeTypeEnum.SALE_INCOME.getCode());*/
                distributorMsg.setOrderNo(String.valueOf(orderNo));
                /*distributorMsg.setEffectField(Constants.SettlementEffectField.LOSSAMOUNT);*/
                distributorMsg.setEventTime(new Date());
                settlementMetadataMsgList.add(distributorMsg);
                rocketMQUtil.sendMsg(settlementCleanTopics, JacksonUtil.beanToJson(settlementMetadataMsgList), String.valueOf(orderNo));
                log.info("订单{}发送资金清算消息成功，orderNo={}",orderNo);
            } else {
                consignmentOrder.setStatus("fail");
                consignmentOrderMapper.insertSelective(consignmentOrder);
            }
        } else {
            return ApiResult.error(apiResult1.getMsg());
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult changeMultipleFund(List<ChangeMultipleFundDto> changeMultipleFundDtos) {
        for (int j = 0; j <changeMultipleFundDtos.size() ; j++) {
            ChangeMultipleFundDto changeMultipleFundDto=changeMultipleFundDtos.get(j);
            for (int i = 0; i <changeMultipleFundDto.getChangeMultipleFundTypeList().size() ; i++) {
                List<ChangeMultipleFundType> changeMultipleFundTypeList = changeMultipleFundDto.getChangeMultipleFundTypeList();
                if(changeMultipleFundTypeList.get(i).getChangeAmount().compareTo(BigDecimal.ZERO)<0){
                    return ApiResult.badParam("变动金额应大于0");
                }
                if(changeMultipleFundTypeList.get(i).getFundType().equals("money")){
                    //为资金
                    ChangeAmountDto changeAmountDto=BeanUtil.copyProperties(changeMultipleFundDto,ChangeAmountDto.class);
                    changeAmountDto.setChangeAmount(changeMultipleFundTypeList.get(i).getChangeAmount());
                    changeAmountDto.setReType(changeMultipleFundTypeList.get(i).getRetype());
                    changeAmountDto.setTradeType(changeMultipleFundTypeList.get(i).getTradeType());
                    ApiResult apiResult = changeAmount(changeAmountDto);
                    if(!apiResult.hasSuccess()){
                        throw  new ServiceException("资金操作失败");
                    }
                }else {
                    ChangeCustomerBeanDto changeCustomerBeanDto=BeanUtil.copyProperties(changeMultipleFundDto,ChangeCustomerBeanDto.class);
                    changeCustomerBeanDto.setCustomerBeanType(changeMultipleFundTypeList.get(i).getFundType());
                    changeCustomerBeanDto.setChangeAmount(changeMultipleFundTypeList.get(i).getChangeAmount());
                    changeCustomerBeanDto.setReType(changeMultipleFundTypeList.get(i).getRetype());
                    changeCustomerBeanDto.setTradeType(changeMultipleFundTypeList.get(i).getTradeType());
                    ApiResult apiResult = customerBeanService.changeAmount(changeCustomerBeanDto);
                    if(!apiResult.hasSuccess()){
                        throw  new ServiceException("积分操作失败");
                    }
                }
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult checkByFundType(CheckByFundTypes checkByFundTypes) {
        for (int i = 0; i <checkByFundTypes.getCheckFundTypes().size() ; i++) {
            CheckFundType checkFundType=checkByFundTypes.getCheckFundTypes().get(i);
            if(checkFundType.getChangeAmount().compareTo(BigDecimal.ZERO)<0){
                throw  new ServiceException("变动金额应大于0");
            }
            if(checkFundType.getFundType().equals("money")){
                //为资金
                Account account = checkAccount(checkByFundTypes.getCustomerNo());
                if(account.getBalance().compareTo(checkFundType.getChangeAmount())<0){
                    throw  new ServiceException("现金余额不足");
                }
            }else {
                ApiResult apiResult=customerBeanService.checkBalanceByType(checkFundType.getFundType(),checkByFundTypes.getCustomerNo(),checkFundType.getChangeAmount());
                if(!apiResult.hasSuccess()){
                    throw  new ServiceException(apiResult.getMsg());
                }
            }
        }
        return ApiResult.success();
    }


    @Override
    public void withdrawDetuchMoney(OrderWithdrawDto orderWithdrawDto) {
        //扣本金
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(orderWithdrawDto.getCustomerNo());
        changeAmountDto.setOrderNo(orderWithdrawDto.getOrderNo());
        changeAmountDto.setChangeAmount(orderWithdrawDto.getAmount());
        changeAmountDto.setReType(Constants.Retype.OUT);
        changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW.getCode());
        updateWithDraw(changeAmountDto);
        //扣手续费
        ChangeAmountDto feeDto = new ChangeAmountDto();
        feeDto.setCustomerNo(orderWithdrawDto.getCustomerNo());
        feeDto.setOrderNo(orderWithdrawDto.getOrderNo());
        feeDto.setChangeAmount(orderWithdrawDto.getFee());
        feeDto.setReType(Constants.Retype.OUT);
        feeDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_FEE_PAY.getCode());
        updateWithDraw(feeDto);

    }

    @Override
    public ApiResult updateWithDraw(ChangeAmountDto changeAmountDto) {
        log.info("修改金额传入的值为"+changeAmountDto);
        if((changeAmountDto.getChangeAmount().compareTo(BigDecimal.ZERO))<1){
            throw new ServiceException("变动值只能为正数");
        }
        Account account=checkAccount(changeAmountDto.getCustomerNo());
        if(account==null){
            throw new ServiceException("帐户不存在");
        }
        //乐观锁实现方式
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=condition.createCriteria();
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
            //支出
            account.setTotalBalance(account.getTotalBalance().subtract(changeAmountDto.getChangeAmount()));
            account.setWithdrawableCash(account.getWithdrawableCash().subtract(changeAmountDto.getChangeAmount()));
            account.setBalance(account.getBalance().subtract(changeAmountDto.getChangeAmount()));
        }else if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
            //收入
            account.setBalance(account.getBalance().add(changeAmountDto.getChangeAmount()));
            account.setWithdrawableCash(account.getWithdrawableCash().add(changeAmountDto.getChangeAmount()));
            account.setTotalBalance(account.getTotalBalance().add(changeAmountDto.getChangeAmount()));
        }else {
            throw new ServiceException("收支类型错误");
        }
        if (checkAmountNegative(account.getWithdrawableCash())) {
            //判断是否为负数，为负数则返回余额不足
            throw new ServiceException("余额不足");
        }
        account.setModifyTime(new Date());
        log.info("修改后的可提现金额为"+account.getWithdrawableCash());
        //插入一条可用资金修改流水
        insertRecordMoney(changeAmountDto,account);
        //判断是否为修改成功
        if (!updateByConditionSelective(account,condition)) {
            throw new ServiceException("资金扣除失败");
        }
        return ApiResult.success();
    }

    @Override
    public void dealDiffChangeAmount(DealDiffMessage dealDiffMessage) {
        for (CashChangeAmountDto cashChangeAmountDto : dealDiffMessage.getCashChangeAmountDtoList()) {
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setOrderNo(cashChangeAmountDto.getOrderNo());
            changeAmountDto.setCustomerNo(cashChangeAmountDto.getCustomerNo());
            changeAmountDto.setChangeAmount(cashChangeAmountDto.getChangeAmount());
            changeAmountDto.setReType(cashChangeAmountDto.getReType());
            changeAmountDto.setTradeType(cashChangeAmountDto.getTradeType());
            this.changeAmount(changeAmountDto);
        }
    }

    @Override
    public List<SumWithdrawAndDepositVo> sumWithdrawAndDeposit(SumWithdrawAndDepositDto sumWithdrawAndDepositDto) {
        List<SumWithdrawAndDepositVo> withdrawAndDepositVoList = accountMapper.sumWithdrawAndDeposit();
        return withdrawAndDepositVoList;
    }

    @Override
    public ApiResult resetWithdrawByCustomer(String customerNo, BigDecimal withdraw) {
        log.info("resetWithdrawByCustomer请求参数为{}，{}",customerNo,withdraw);
        Account account=checkAccount(customerNo);
        if(account==null){
            return ApiResult.badParam("账户不存在");
        }
        Condition condition=new Condition(Account.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("balance",account.getBalance());
        criteria.andEqualTo("customerNo",account.getCustomerNo());
        criteria.andEqualTo("totalBalance",account.getTotalBalance());
        criteria.andEqualTo("withdrawableCash",account.getWithdrawableCash());
        account.setWithdrawableCash(withdraw);
        account.setModifyTime(new Date());
        int i = accountMapper.updateByConditionSelective(account,condition);
        if(i!=1){
            return ApiResult.error("乐观锁问题");
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult synchronizationBalance() {
        accountMapper.synchronizationBalance();
        return ApiResult.success();
    }

    @Override
    public ApiResult dealOrder(List<ChangeAmountDto> changeAmountDtos) {
        for (int i = 0; i <changeAmountDtos.size() ; i++) {
            ChangeAmountDto changeAmountDto = changeAmountDtos.get(i);
            if(Constants.Retype.OUT.equals(changeAmountDto.getReType())){
                //此处为解冻
                thawAmountByTrade(changeAmountDto);
            }else if(Constants.Retype.IN.equals(changeAmountDto.getReType())){
                //收入则为摘牌买入方
                changeAmount(changeAmountDto);
            }else {
                return  ApiResult.badParam();
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult<AccountDetailVo> getAccountDetail(CustomerNoDto customerNoDto) {
        Account account = checkAccount(customerNoDto.getCustomerNo());
        if(account==null){
            return ApiResult.badParam("账户不存在");
        }
        //将账户信息关联出来
        AccountDetailVo accountDetailVo=BeanUtil.copyProperties(account,AccountDetailVo.class);
        //从流水表中查询出对应的信息
        //总入金
        BigDecimal totalRecharge = recordMoneyMapper.findTotalChangeAmountByTradeType(customerNoDto.getCustomerNo(), TradeMoneyTradeTypeEnum.RECHARGE.getCode());
        //总出金
        BigDecimal totalWithdraw = BigDecimal.ZERO.subtract(recordMoneyMapper.findTotalChangeAmountByTradeType(customerNoDto.getCustomerNo(), TradeMoneyTradeTypeEnum.WITHDRAW.getCode()));
        //总收入
        BigDecimal totalIn =recordMoneyMapper.findTotalByRetype(customerNoDto.getCustomerNo(),Constants.Retype.IN);
        //总支出
        BigDecimal totalOut =BigDecimal.ZERO.subtract(recordMoneyMapper.findTotalByRetype(customerNoDto.getCustomerNo(),Constants.Retype.OUT));
        accountDetailVo.setTotalIn(totalIn);
        accountDetailVo.setTotalOut(totalOut);
        accountDetailVo.setTotalRecharge(totalRecharge);
        accountDetailVo.setTotalWithdraw(totalWithdraw);
        return ApiResult.success(accountDetailVo);
    }

    @Override
    public ApiResult changeMoneyList(List<ChangeAmountDto> changeAmountDtos) {
        for (int i = 0; i <changeAmountDtos.size() ; i++) {
            ApiResult apiResult = changeAmount(changeAmountDtos.get(i));
            if(!apiResult.hasSuccess()){
                throw  new ServiceException(changeAmountDtos.get(i).getCustomerNo()+"操作资金失败");
            }
        }
        return ApiResult.success();
    }

    /**
     * 插入一条冻结资金的流水记录
     *
     * @param changeAmountDto 冻结的资金,订单ID以及订单类型等
     * @param account        操作修改后的账户
     */
    private void insertRecordFreezingAmount(ChangeAmountDto changeAmountDto, Account account) {
        RecordFreezingAmount recordFreezingAmount = new RecordFreezingAmount();
        recordFreezingAmount.setId(IdWorker.getId());
        //交易商编码
        recordFreezingAmount.setCustomerNo(account.getCustomerNo());
        //变动数额
        recordFreezingAmount.setChangeAmount(changeAmountDto.getChangeAmount().toString());
        //冻结资金后的冻结资金数额
        recordFreezingAmount.setBalance(account.getFreezingAmount());
        //交易类型
        recordFreezingAmount.setTradeType(changeAmountDto.getTradeType());
        //订单号
        recordFreezingAmount.setOrderNo(changeAmountDto.getOrderNo());
        //冻结类型 1：冻结，2：解冻
        recordFreezingAmount.setRetype(changeAmountDto.getReType());
        recordFreezingAmount.setCreateTime(new Date());
        recordFreezingAmount.setModifyTime(new Date());
        //是否有效
        recordFreezingAmount.setFlag(new Byte("1"));
        recordFreezingAmount.setCustomerNo(account.getCustomerNo());
        recordFreezingAmountMapper.insertSelective(recordFreezingAmount);
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

    /**
     * 判断是否是出金时间
     *
     * @return
     */
    public static boolean compareTime(String dTime, String wTime) {
        boolean flag = false;
        try {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            Calendar cal = Calendar.getInstance();//使用日历类
            cal.setTime(new Date());
            int hour = cal.get(cal.HOUR_OF_DAY);
            int minute = cal.get(cal.MINUTE);
            int second = cal.get(cal.SECOND);
            String hourStr = hour < 10 ? "0" + hour : hour + "";
            String minuteStr = minute < 10 ? "0" + minute : minute + "";
            String secondStr = second < 10 ? "0" + second : second + "";
            String nowTime = hourStr + ":" + minuteStr + ":" + secondStr;
            String depositTime1 = dTime;
            String withdrawTime1 = wTime;
            Date depositTime = format.parse(depositTime1);
            Date withdrawTime = format.parse(withdrawTime1);
            Date nowTime1 = format.parse(nowTime);
            if (nowTime1.compareTo(depositTime) == 1 && nowTime1.compareTo(withdrawTime) == -1) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;

    }
}
