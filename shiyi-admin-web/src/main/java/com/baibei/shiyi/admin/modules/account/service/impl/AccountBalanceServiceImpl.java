package com.baibei.shiyi.admin.modules.account.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.base.shiyi.ICustomerBeanBase;
import com.baibei.shiyi.account.feign.bean.dto.*;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.admin.modules.account.async.AccountBalanceAsync;
import com.baibei.shiyi.admin.modules.account.bean.dto.AccountBalanceDto;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceExportVo;
import com.baibei.shiyi.admin.modules.account.bean.vo.AccountBalanceVo;
import com.baibei.shiyi.admin.modules.account.dao.AccountBalanceMapper;
import com.baibei.shiyi.admin.modules.account.model.AccountBalance;
import com.baibei.shiyi.admin.modules.account.model.AccountBalanceError;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceErrorService;
import com.baibei.shiyi.admin.modules.account.service.IAccountBalanceService;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.RandomUtils;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author: uqing
 * @date: 2019/11/26 15:41:32
 * @description: AccountBalance服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class AccountBalanceServiceImpl extends AbstractService<AccountBalance> implements IAccountBalanceService {

    @Autowired
    private AccountBalanceMapper tblAdminAccountBalanceMapper;

    @Autowired
    private CustomerFeign customerFeign;

    /**
     * excel导入的数据执行条数
     */
    private Integer importDataCount = 500;

    @Autowired
    private IAccountBalanceErrorService accountBalanceErrorService;


    @Autowired
    private AccountBalanceAsync accountBalanceAsync;

    @Autowired
    private ICustomerBeanBase customerBeanBase;


    @Override
    public MyPageInfo<AccountBalanceVo> pageList(AccountBalanceDto accountBalanceDto) {
        if (accountBalanceDto.getCurrentPage() != null || accountBalanceDto.getPageSize() != null) {
            PageHelper.startPage(accountBalanceDto.getCurrentPage(), accountBalanceDto.getPageSize());
        }
        List<AccountBalanceVo> accountBalanceVoList = tblAdminAccountBalanceMapper.findPageList(accountBalanceDto);
        return new MyPageInfo<>(accountBalanceVoList);
    }

    /**
     * @param accountBalanceList
     */
    @Override
    public Boolean batchSave(List<AccountBalanceExportVo> accountBalanceList, String batchNo) {
        Boolean errorResult = true;
        // 1、定义5个线程池
        if (accountBalanceList.isEmpty()) {
            log.info("当前导入的账号的数据为空,不支持导入");
            throw new ServiceException("当前导入的账号的数据为空,请验证数据");
        }
        if (accountBalanceList.size() > 1000) {
            throw new ServiceException("已超过1000条数据不支持导入");
        }
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Integer number = (accountBalanceList.size() + importDataCount - 1) / importDataCount; //分割list为多少组

        // 执行人
        String createBy = SecurityUtils.getUsername();
        List<Callable<List<AccountBalanceError>>> resultList = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(number).forEach(
                i -> {
                    List<AccountBalanceExportVo> accountBalances = accountBalanceList.stream().skip(i * importDataCount).
                            limit(importDataCount).collect(Collectors.toList());
                    AccountBalanceFileImport accountBalanceFileImport = new AccountBalanceFileImport(accountBalances, createBy, batchNo);
                    resultList.add(accountBalanceFileImport);
                }
        );
        try {
            List<Future<List<AccountBalanceError>>> resultFuture = executor.invokeAll(resultList);
            for (Future<List<AccountBalanceError>> future : resultFuture) {
                List<AccountBalanceError> accountBalanceErrors = future.get();
                if (!CollectionUtils.isEmpty(accountBalanceErrors)) {
                    System.out.println("当前的导入结果" + JSONObject.toJSONString(accountBalanceErrors));
                    accountBalanceErrorService.save(accountBalanceErrors);
                    errorResult = false;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
        return errorResult;
    }

    @Override
    public Boolean isDuplicate(AccountBalance accountBalanceDto) {
        List<AccountBalanceVo> accountBalanceVos = this.tblAdminAccountBalanceMapper.duplicateList(accountBalanceDto);
        if (accountBalanceVos.size() > 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<AccountBalance> findByIds(Iterable<String> ids) {
        Condition condition = new Condition(AccountBalance.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andIn("id", ids);
        List<AccountBalance> accountBalances = this.findByCondition(condition);
        return accountBalances;
    }


    @Override
    public ApiResult execution(Long id) {
        AccountBalance accountBalance = this.findById(id);
        if (accountBalance == null) {
            log.info("当前用户的数据不存在{}", id);
            return ApiResult.error("当前执行数据不存在");
        }

        if (accountBalance.getStatus().equals(Constants.Status.TIMEOUT)) {
            log.info("当前用户的数据已超时不能执行{}", JSONObject.toJSONString(accountBalance));
            return ApiResult.error("当前订单已超时,不能执行");
        }
        // step 1
        if (accountBalance.getStatus().equals(Constants.Status.SUCCESS)) {
            log.info("当前订单已经成功执行,不能重复执行");
            return ApiResult.error("当前订单已经成功执行,不能重复执行");
        }

        if (StringUtils.isEmpty(accountBalance.getOrderNo())) {
            String orderNo = null;
            if (Constants.Retype.IN.equals(accountBalance.getBalanceType())) {
                orderNo = this.generateOrderNo("D");
            } else if (Constants.Retype.OUT.equals(accountBalance.getBalanceType())) {
                orderNo = this.generateOrderNo("W");
            }
            if (StringUtils.isEmpty(orderNo)) {
                accountBalance.setStatus(Constants.Status.FAIL);
                this.update(accountBalance);
                return ApiResult.error("生成订单号失败");
            }
            accountBalance.setOrderNo(orderNo);
            this.update(accountBalance);
        }
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(accountBalance.getCustomerNo());
        ApiResult<CustomerBeanVo> account = customerBeanBase.getBalance(customerNoDto);
        if (account.hasFail()) {
            log.info("获取当前账号余额失败");
            accountBalance.setStatus(Constants.Status.FAIL);
            this.update(accountBalance);
            return ApiResult.error(account.getMsg());
        }

        if (accountBalance.getBalanceType().equals(Constants.Retype.OUT)) {
            CustomerBeanVo customerBeanVo = account.getData();
            // 账号余额减去-可出金余额
            BigDecimal result = customerBeanVo.getMallAccountBalance().subtract(accountBalance.getBalance());
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                log.info("当前用户余额为{},出金余额为{}", customerBeanVo.getMallAccountBalance(), accountBalance.getBalance());
                accountBalance.setStatus(Constants.SuccessOrFail.FAIL);
                this.update(accountBalance);
                return ApiResult.error("用户的余额不支持出金");
            }
        }
        // step 2、执行添加用户余额
        ChangeCustomerBeanDto changeCustomerBeanDto = toChangeCustomerBeanDto(accountBalance.getOrderNo(), accountBalance);
        log.info("当前用户执行余额的数据为{}", JSONObject.toJSONString(changeCustomerBeanDto));
        ApiResult result = customerBeanBase.changeAmount(changeCustomerBeanDto);
        if (result.hasFail()) {
            accountBalance.setStatus(Constants.SuccessOrFail.FAIL);
            this.accountBalanceAsync.updateAccountBalanceStatus(accountBalance);
            log.info("当前执行用户出金和入金的余额失败消息为{}", result.getMsg());
            return ApiResult.error(result.getMsg());
        }
        // 这里可以走异步,防止数据库错误,导致数据回滚
        accountBalance.setExecutionBy(SecurityUtils.getUsername());
        accountBalance.setExecutionTime(new Date());
        accountBalance.setStatus(Constants.SuccessOrFail.SUCCESS);
        this.accountBalanceAsync.updateAccountBalanceStatus(accountBalance);
        return ApiResult.success();
    }

    /**
     * 批量执行,先验证所有的结果
     *
     * @param accountBalanceDto
     * @return
     */
    @Override
    public ApiResult batchExecution(AccountBalanceDto accountBalanceDto) {
        List<ApiResult> accountBalances = new ArrayList<>();
        for (String ids : accountBalanceDto.getIds()) {
            accountBalances.add(execution(Long.valueOf(ids)));
        }
        List<ApiResult> resultList = accountBalances.stream().filter(x -> x.hasFail()).collect(Collectors.toList());
        if (resultList.size() > 0) {
            return ApiResult.error("有部分数据执行失败");
        }
        return ApiResult.success();
    }

    /**
     * 保存
     *
     * @param accountBalanceDto
     */
    @Override
    public void saveAccountBalance(AccountBalanceDto accountBalanceDto) {
//        AccountBalance accountBalance = toEntity(accountBalanceDto);
//        this.save(accountBalance);
    }

    /**
     * 修改
     *
     * @param accountBalanceDto
     */
    @Override
    public void updateAccountBalance(AccountBalanceDto accountBalanceDto) {

    }

    @Override
    public Boolean verifyStatus(List<AccountBalance> accountBalances) {
        Boolean result = true;
        List<AccountBalance> statusList = accountBalances.stream().filter(
                x -> {
                    if (x.getStatus().equals(Constants.Status.SUCCESS) ||
                            x.getStatus().equals(Constants.Status.TIMEOUT)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(statusList)) {
            result = false;
        }
        return result;
    }


    /**
     * 个人账号转换实体
     *
     * @param orderNo
     * @param accountBalance
     * @return
     */
    private ChangeAmountDto changeAmountToEntity(String orderNo, AccountBalance accountBalance) {
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        if (accountBalance.getBalanceType().equals(Constants.Retype.IN)) {
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.ADMIN_ACCOUNT_IN.getCode());
            changeAmountDto.setReType(Constants.Retype.IN);
        } else if (accountBalance.getBalanceType().equals(Constants.Retype.OUT)) {
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.ADMIN_ACCOUNT_OUT.getCode());
            changeAmountDto.setReType(Constants.Retype.OUT);
        }
        changeAmountDto.setCustomerNo(accountBalance.getCustomerNo());
        changeAmountDto.setOrderNo(orderNo);
        changeAmountDto.setChangeAmount(accountBalance.getBalance());
        return changeAmountDto;
    }


    /**
     * 商城账号转换实体
     *
     * @param orderNo
     * @param accountBalance
     * @return
     */
    private ChangeCustomerBeanDto toChangeCustomerBeanDto(String orderNo, AccountBalance accountBalance) {
        ChangeCustomerBeanDto changeCustomerBeanDto = new ChangeCustomerBeanDto();
        changeCustomerBeanDto.setOrderNo(orderNo);
        if (accountBalance.getBalanceType().equals(Constants.Retype.IN)) {
            changeCustomerBeanDto.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_IN.getCode());
            changeCustomerBeanDto.setReType(Constants.Retype.IN);
            changeCustomerBeanDto.setCustomerBeanType(Constants.BeanType.MALLACCOUNT);
        } else if (accountBalance.getBalanceType().equals(Constants.Retype.OUT)) {
            changeCustomerBeanDto.setTradeType(RecordBeanTradeTypeEnum.MALLACCOUNT_OUT.getCode());
            changeCustomerBeanDto.setReType(Constants.Retype.OUT);
            changeCustomerBeanDto.setCustomerBeanType(Constants.BeanType.MALLACCOUNT);
        }
        changeCustomerBeanDto.setCustomerNo(accountBalance.getCustomerNo());
        changeCustomerBeanDto.setOrderNo(orderNo);
        changeCustomerBeanDto.setChangeAmount(accountBalance.getBalance());
        return changeCustomerBeanDto;
    }

    /**
     * 数据转换
     *
     * @return
     */
    private ChangeMultipleFundDto dateConversion(String orderNo, AccountBalance accountBalance) {
        ChangeMultipleFundDto changeMultipleFundDto = new ChangeMultipleFundDto();
        changeMultipleFundDto.setOrderNo(orderNo);
        changeMultipleFundDto.setCustomerNo(accountBalance.getCustomerNo());
        List<ChangeMultipleFundType> changeMultipleFundTypeList = new ArrayList<>();
        if (accountBalance.getBalanceType().equals(Constants.Retype.IN)) {
            ChangeMultipleFundType changeMultipleFundType = new ChangeMultipleFundType();
            changeMultipleFundType.setChangeAmount(accountBalance.getBalance());
            changeMultipleFundType.setTradeType(TradeMoneyTradeTypeEnum.ADMIN_ACCOUNT_IN.getCode());
            changeMultipleFundType.setRetype(Constants.Retype.IN);
            changeMultipleFundType.setFundType(Constants.BeanType.MONEY);
            changeMultipleFundTypeList.add(changeMultipleFundType);
        } else if (accountBalance.getBalanceType().equals(Constants.Retype.OUT)) {
            // 判断资金为负数,不能执行
            ChangeMultipleFundType changeMultipleFundType = new ChangeMultipleFundType();
            changeMultipleFundType.setChangeAmount(accountBalance.getBalance());
            changeMultipleFundType.setTradeType(TradeMoneyTradeTypeEnum.ADMIN_ACCOUNT_OUT.getCode());
            changeMultipleFundType.setRetype(Constants.Retype.OUT);
            changeMultipleFundType.setFundType(Constants.BeanType.MONEY);
            changeMultipleFundTypeList.add(changeMultipleFundType);
        }
        log.info("当前用户数据转换为{}", JSONObject.toJSONString(changeMultipleFundDto));
        changeMultipleFundDto.setChangeMultipleFundTypeList(changeMultipleFundTypeList);
        return changeMultipleFundDto;
    }

    public String generateOrderNo(String prefix) {
        StringBuilder orderNoStr = new StringBuilder();
        orderNoStr.append(prefix);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        orderNoStr.append(dtf.format(LocalDateTime.now()));
        orderNoStr.append(String.format("%04d", RandomUtils.getRandom(1, 10000)));
        String orderNo = orderNoStr.toString();
        Condition condition = new Condition(AccountBalance.class);
        condition.createCriteria().andEqualTo("orderNo", orderNo);
        List<AccountBalance> result = this.findByCondition(condition);
        if (result.size() > 0) {
            return generateOrderNo(prefix);
        }
        return orderNo;
    }


    private class AccountBalanceFileImport implements Callable<List<AccountBalanceError>> {

        private List<AccountBalanceExportVo> accountBalanceVos;

        private String createBy;

        private String batchNo;

        public AccountBalanceFileImport(List<AccountBalanceExportVo> accountBalances, String createBy, String batchNo) {
            this.accountBalanceVos = accountBalances;
            this.createBy = createBy;
            this.batchNo = batchNo;
        }

        @Override
        public List<AccountBalanceError> call() throws Exception {
            List<AccountBalanceError> accountBalanceErrors = new ArrayList<>();
            for (AccountBalanceExportVo accountBalanceVo : accountBalanceVos) {
                if (StringUtils.isEmpty(accountBalanceVo.getCustomerNo()) && StringUtils.isEmpty(accountBalanceVo.getBalance())
                        && StringUtils.isEmpty(accountBalanceVo.getPhone())) {
                    log.info("当前数据手机号或者客户编号或者余额不能为空{}", JSONObject.toJSONString(accountBalanceVo));
                    continue;
                }
                ApiResult result = formatCheck(accountBalanceVo);
                accountBalanceVo.setBatchNo(batchNo);
                if (result.hasFail()) {
                    AccountBalanceError accountBalanceError = toAccountErrorEntity(accountBalanceVo, result.getMsg());
                    accountBalanceErrors.add(accountBalanceError);
                    continue;
                }
                CustomerNoDto customerNoDto = new CustomerNoDto();
                customerNoDto.setCustomerNo(accountBalanceVo.getCustomerNo());
                ApiResult<CustomerVo> apiResult = customerFeign.findUserByCustomerNo(customerNoDto);
                if (apiResult.hasFail()) {
                    AccountBalanceError accountBalanceError = toAccountErrorEntity(accountBalanceVo, apiResult.getMsg());
                    accountBalanceErrors.add(accountBalanceError);
                    continue;
                }
                CustomerVo customerVo = apiResult.getData();
                if (!customerVo.getMobile().equals(accountBalanceVo.getPhone())) {
                    AccountBalanceError accountBalanceError = toAccountErrorEntity(accountBalanceVo, "手机号不正确");
                    accountBalanceErrors.add(accountBalanceError);
                    continue;
                }
                try {
                    accountBalanceVo.setCreateBy(createBy); // 设置创建人
                    AccountBalance accountBalance = toEntity(accountBalanceVo);
                    AccountBalanceServiceImpl.this.save(accountBalance);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.info("当前客户{}保存失败,失败消息为{}", JSONObject.toJSONString(accountBalanceVo), ex.getMessage());
                    AccountBalanceError accountBalanceError = toAccountErrorEntity(accountBalanceVo, "系统错误");
                    accountBalanceErrors.add(accountBalanceError);
                }
            }
            return accountBalanceErrors;
        }

    }

    /**
     * 校验格式
     *
     * @param accountBalanceExportVo
     * @return
     */
    private ApiResult formatCheck(AccountBalanceExportVo accountBalanceExportVo) {
        if (StringUtils.isEmpty(accountBalanceExportVo.getCustomerNo())) {
            return ApiResult.error("客户编号不能为空");
        }
        if (StringUtils.isEmpty(accountBalanceExportVo.getPhone())) {
            return ApiResult.error("手机号不能为空");
        }

        if (StringUtils.isEmpty(accountBalanceExportVo.getBalance())) {
            return ApiResult.error("余额不能为空");
        }

        if (StringUtils.isEmpty(accountBalanceExportVo.getBalanceTypeText())) {
            return ApiResult.error("类型不能为空");
        }
        if (!StringUtils.isEmpty(accountBalanceExportVo.getBalance())) {
            Pattern pattern = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
            String balanceFormat = accountBalanceExportVo.getBalance().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            if (!pattern.matcher(balanceFormat).matches()) {
                return ApiResult.error("余额格式不正确");
            }
        }
        if (!StringUtils.isEmpty(accountBalanceExportVo.getBalanceTypeText())) {
            if (!(accountBalanceExportVo.getBalanceTypeText().equals("转入") || accountBalanceExportVo.getBalanceTypeText().equals("转出"))) {
                return ApiResult.error("类型不是转入或转出");
            }
        }

        return ApiResult.success();
    }


    private AccountBalanceError toAccountErrorEntity(AccountBalanceExportVo accountBalanceVo, String errorMsg) {
        AccountBalanceError error = new AccountBalanceError();
        error.setId(IdWorker.getId());
        error.setBalance(accountBalanceVo.getBalance());
        error.setCustomerNo(accountBalanceVo.getCustomerNo());
        error.setErrorMsg(errorMsg);
        error.setBalanceType(toBalanceTypeConversion(accountBalanceVo.getBalanceTypeText()));
        error.setPhone(accountBalanceVo.getPhone());
        error.setBatchNo(accountBalanceVo.getBatchNo());
        return error;
    }

    private AccountBalance toEntity(AccountBalanceExportVo accountBalanceVo) {
        AccountBalance accountBalance = new AccountBalance();
        accountBalance.setId(IdWorker.getId());
        accountBalance.setCustomerNo(accountBalanceVo.getCustomerNo()); // 客户编号
        accountBalance.setCreateBy(accountBalanceVo.getCreateBy());// 创建人
        accountBalance.setBalance(accountBalanceVo.getBalance()); // 余额
        accountBalance.setCreateTime(new Date()); // 创建者
        accountBalance.setPhone(accountBalanceVo.getPhone()); // 手机号
        accountBalance.setModifyTime(new Date()); // 修改时间
        accountBalance.setExecutionBy(accountBalanceVo.getExecuteBy()); //执行人
        accountBalance.setFlag(new Byte(Constants.Flag.VALID)); //是否删除
        accountBalance.setRemarks(accountBalanceVo.getRemark()); //备注
        accountBalance.setBalanceType(toBalanceTypeConversion(accountBalanceVo.getBalanceTypeText()));
        accountBalance.setStatus(Constants.MallOrderStatus.WAIT); //设置状态待执行
        accountBalance.setBatchNo(accountBalanceVo.getBatchNo()); //批次号
        return accountBalance;
    }

    private String toBalanceTypeConversion(String balanceTypeText) {
        if (StringUtils.isEmpty(balanceTypeText)) {
            return null;
        }
        if (balanceTypeText.equals("转入")) {
            return Constants.Retype.IN;
        } else if (balanceTypeText.equals("转出")) {
            return Constants.Retype.OUT;
        }
        return null;
    }
}
