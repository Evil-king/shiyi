package com.baibei.shiyi.account.rocketmq.comsume;

import com.alibaba.fastjson.JSON;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeMultipleFundType;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.KeyValue;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.order.feign.base.message.SendIntegralMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/11/6 14:14
 * @description: 购买商城商品赠送积分
 */
@Component
@Slf4j
public class OrderSendIntegralConsumerImpl implements IConsumer<SendIntegralMsg> {
    @Autowired
    private IAccountService accountService;

    @Override
    public ApiResult execute(SendIntegralMsg sendIntegralMsg) {
        log.info("商城订单赠送积分，sendIntegralMsg={}", JSON.toJSONString(sendIntegralMsg));
        List<KeyValue> integralList = sendIntegralMsg.getIntegralList();
        List<ChangeMultipleFundType> changeMultipleFundTypeList = new ArrayList<>();
        for (KeyValue kv : integralList) {
            ChangeMultipleFundType changeMultipleFundType = new ChangeMultipleFundType();
            changeMultipleFundType.setRetype(Constants.Retype.IN);

            if(kv.getKey().equals(Constants.BeanType.EXCHANGE)){
                changeMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.EXCHANGE_BUY_GIVE.getCode());
            }else if(kv.getKey().equals(Constants.BeanType.SHIYI)){
                changeMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.SHIYI_BUY_GIVE.getCode());
            }else if(kv.getKey().equals(Constants.BeanType.CONSUMPTION)){
                changeMultipleFundType.setTradeType(RecordBeanTradeTypeEnum.CONSUMPTION_BUY_GIVE.getCode());
            }
            changeMultipleFundType.setChangeAmount(new BigDecimal(kv.getValue()));
            changeMultipleFundType.setFundType(kv.getKey());
            changeMultipleFundTypeList.add(changeMultipleFundType);
        }
        ChangeMultipleFundDto changeMultipleFundDto = new ChangeMultipleFundDto();
        changeMultipleFundDto.setCustomerNo(sendIntegralMsg.getCustomerNo());
        changeMultipleFundDto.setOrderNo(sendIntegralMsg.getOrderNo());
        changeMultipleFundDto.setChangeMultipleFundTypeList(changeMultipleFundTypeList);
        List<ChangeMultipleFundDto> changeMultipleFundDtoList = new ArrayList<>();
        changeMultipleFundDtoList.add(changeMultipleFundDto);
        return accountService.changeMultipleFund(changeMultipleFundDtoList);
    }
}