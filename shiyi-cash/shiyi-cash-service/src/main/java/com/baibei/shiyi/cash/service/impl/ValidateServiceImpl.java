package com.baibei.shiyi.cash.service.impl;

import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.account.feign.bean.dto.CheckByFundTypes;
import com.baibei.shiyi.account.feign.bean.dto.CheckFundType;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.dto.SignInBackDto;
import com.baibei.shiyi.cash.feign.base.vo.SingingFirstVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiSigningInBackFeign;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.service.ISignInBackService;
import com.baibei.shiyi.cash.service.IValidateService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.cash.util.Utils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.client.CustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hwq
 * @date 2019/06/06
 * <p>
 * 出金前的一系列操作(验证参数、创建订单)
 * </p>
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ValidateServiceImpl implements IValidateService {
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private CustomerFeign customerFeign;
    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private ISigningRecordFeign signingRecordFeign;
    @Autowired
    private IAccountBase accountBase;
    @Autowired
    private ISignInBackService signInBackService;
    private SimpleDateFormat yyyyMMddWithLine = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();


    @Override
    public void validateWithdraw(OrderWithdrawDto orderWithdrawDto) {
        if (!"on".equals(propertiesVal.getWithdrawSwitch())) {
            log.info("提现开关未打开");
            throw new ServiceException("提现错误，请联系客服");
        }
        //出金金额参数校验（非负数，大于0）
        if(orderWithdrawDto.getOrderAmt().toString().indexOf("-") != -1
                && orderWithdrawDto.getOrderAmt().compareTo(new BigDecimal(0)) == -1){
            throw new ServiceException("提现金额不符合要求");
        }
        //判断是否是出入金时间
        boolean b = Utils.compareTime(propertiesVal.getDepositTime(), propertiesVal.getWithdrawTime());
        if (!b) {
            throw new ServiceException("非提现时间");
        }
        if (StringUtils.isEmpty(orderWithdrawDto.getPassword())) {
            throw new ServiceException("资金密码不能为空");
        }

        //获取用户信息
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(orderWithdrawDto.getCustomerNo());
        ApiResult<CustomerVo> customerVoApiResult = customerFeign.findUserByCustomerNo(customerNoDto);
        CustomerVo customerVo = customerVoApiResult.getData();

        if (StringUtils.isEmpty(customerVo)) {
            throw new ServiceException("用户信息异常");
        }

        //判断用户是否已开户
        if("0".equals(customerVo.getSigning())){
            log.info("用户未开户，sign={}", customerVo.getSigning());
            throw new ServiceException("提现失败，未开户");
        }

        //判断是否被限制提现
        if (customerVo.getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_WITHDRAW.getCode()) != -1) {
            throw new ServiceException("提现失败，请联系客服");
        }

        //判断资金密码是否正确
        ApiResult pwdApiResult = accountFeign.
                checkFundPassword(orderWithdrawDto.getCustomerNo(), orderWithdrawDto.getPassword());
        if (pwdApiResult.getCode()!=200) {
            throw new ServiceException("密码错误");
        }

        //判断可提金额是否足够
        CustomerNoDto cusDto = new CustomerNoDto();
        cusDto.setCustomerNo(orderWithdrawDto.getCustomerNo());
        ApiResult<AccountVo> accountVoApiResult = accountBase.findAccount(cusDto);
        if (accountVoApiResult.hasFail()) {
            log.info("获取账户信息失败，用户编码为："+orderWithdrawDto.getCustomerNo());
            throw new ServiceException("获取账户信息失败");
        }
        AccountVo accountVo = accountVoApiResult.getData();
        if (accountVo.getWithdrawableCash().compareTo(orderWithdrawDto.getOrderAmt())<0) {
            log.info("用户可提金额不足，用户编码为："+orderWithdrawDto.getCustomerNo());
            throw new ServiceException("可提资金不足");
        }

       /* CheckByFundTypes checkByFundTypes = new CheckByFundTypes();
        List<CheckFundType> checkFundTypeList = new ArrayList<>();
        CheckFundType checkFundType = new CheckFundType();
        checkFundType.setChangeAmount(orderWithdrawDto.getOrderAmt());
        checkFundType.setFundType(Constants.BeanType.MONEY);
        checkFundTypeList.add(checkFundType);
        checkByFundTypes.setCustomerNo(orderWithdrawDto.getCustomerNo());
        checkByFundTypes.setCheckFundTypes(checkFundTypeList);
        ApiResult apiResult = accountFeign.checkByFundType(checkByFundTypes);
        if (apiResult.getCode()!=200) {
            throw new ServiceException("余额不足");
        }*/

    }

}
