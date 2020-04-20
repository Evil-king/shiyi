package com.baibei.shiyi.cash.rocketmq.comsume;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.cash.bean.dto.FuqingDepositDto;
import com.baibei.shiyi.cash.model.FuqingAccountDeposit;
import com.baibei.shiyi.cash.service.IFuqingAccountDepositService;
import com.baibei.shiyi.cash.util.SerialNumberComponent;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class FuqingDepositAddMoneyConsumerImpl implements IConsumer<FuqingDepositDto> {

    @Autowired
    private IFuqingAccountDepositService fuqingAccountDepositService;

    @Autowired
    private IAccountBase accountService;

    @Autowired
    private SerialNumberComponent serialNumberComponent;

    @Override
    public ApiResult execute(FuqingDepositDto fuqingDepositDto) {
        log.info("福清入金的消息体为{}，sendIntegralMsg={}", JSON.toJSONString(fuqingDepositDto));
        // step1 入金记录
        List<FuqingAccountDeposit> depositList = findSerialNo(fuqingDepositDto.getSerialNo());
        if (depositList.size() > 0) { //说面当前有serialNo 已经存在
            log.info("当前serialNo已经在今天存在{},取消重复入金", fuqingDepositDto.getSerialNo());
            return ApiResult.success();
        }
        FuqingAccountDeposit accountDeposit = BeanUtil.copyProperties(fuqingDepositDto, FuqingAccountDeposit.class);
        BigDecimal occurAmount = fuqingDepositDto.getOccurAmount().divide(new BigDecimal(100));
        accountDeposit.setOccurAmount(occurAmount);
        accountDeposit.setId(IdWorker.getId());
        accountDeposit.setStatus(Constants.Status.FAIL);
        accountDeposit.setFlag(new Byte(Constants.Flag.VALID));
        String orderNo = serialNumberComponent.generateOrderNo(FuqingAccountDeposit.class, fuqingAccountDepositService, "F", "orderNo");
        accountDeposit.setOrderNo(orderNo);
        fuqingAccountDepositService.save(accountDeposit);

        // step2 开始入金
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(fuqingDepositDto.getExchangeFundAccount());
        changeAmountDto.setReType(Constants.Retype.IN);
        changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.RECHARGE.getCode());
        changeAmountDto.setChangeAmount(accountDeposit.getOccurAmount());
        changeAmountDto.setOrderNo(accountDeposit.getOrderNo());
        ApiResult apiResult = accountService.changeAmount(changeAmountDto);
        if (apiResult.hasFail()) {
            log.info("当前入金失败,code 是{},msg 是{}", apiResult.getCode(), apiResult.getMsg());
            return ApiResult.error();
        }
        accountDeposit.setStatus(Constants.Status.SUCCESS);
        fuqingAccountDepositService.update(accountDeposit);
        return ApiResult.success();
    }

    public List<FuqingAccountDeposit> findSerialNo(String serialNo) {
        Condition condition = new Condition(FuqingAccountDeposit.class);
        Example.Criteria criteria = fuqingAccountDepositService.buildValidCriteria(condition);
        criteria.andEqualTo("serialNo", serialNo);
        criteria.andBetween("createTime", DateUtil.getBeginDay(new Date()), DateUtil.getEndDay(new Date()));
        return fuqingAccountDepositService.findByCondition(condition);
    }


}
