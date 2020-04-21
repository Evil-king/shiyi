package com.baibei.shiyi.publicc.service.impl;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.publicc.dao.SmsMapper;
import com.baibei.shiyi.publicc.model.Sms;
import com.baibei.shiyi.publicc.service.ISmsService;
import com.baibei.shiyi.publicc.sms.PropertiesVal;
import com.baibei.shiyi.publicc.sms.process.YXTProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/05/30 13:41:20
 * @description: Sms服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SmsServiceImpl extends AbstractService<Sms> implements ISmsService {

    @Autowired
    private SmsMapper smsMapper;
    @Autowired
    private YXTProcess yxt;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PropertiesVal propertiesVal;

    @Override
    public ApiResult<String> getSms(String phone, String message) {
        //验证参数
        ApiResult apiResult = validate(phone);
        if (!apiResult.hasSuccess()) {
            return apiResult;
        }
        boolean flag = yxt.doProcess(phone, message);
        if (!flag) {
            apiResult.setCode(ResultEnum.PUBLIC_SMS_CODE.getCode());
            apiResult.setMsg(ResultEnum.PUBLIC_SMS_CODE.getMsg());
            return apiResult;
        }
        return apiResult;
    }

    @Override
    public ApiResult validateCode(String phone, String code, String type) {
        ApiResult apiResult = new ApiResult();
        //判断短信码5分钟之内有效
        String key = MessageFormat.format(RedisConstant.SMS_USER_PHONE, phone + type);
        String redisValue = redisUtil.get(key);
        log.info("code={}", code);
        log.info("redisValue={}",redisValue);
        if (redisValue == null || !code.equalsIgnoreCase(redisValue)) {
            apiResult.setMsg(ResultEnum.SMS_TIME_MAX.getMsg());
            apiResult.setCode(ResultEnum.SMS_TIME_MAX.getCode());
            return apiResult;
        }
        return ApiResult.success();
    }

    private ApiResult validate(String phone) {
        ApiResult apiResult = new ApiResult();
        if (phone.length() != 11) {
            apiResult.setMsg(ResultEnum.MOBILE_NUM_CHECKOUT.getMsg());
            apiResult.setCode(ResultEnum.MOBILE_NUM_CHECKOUT.getCode());
            return apiResult;
        }
        /**
         * 判断该手机号一天之内的次数是否大于20
         */
        String nowDay = DateUtil.get1DayByNowTime();
        Condition condition = new Condition(Sms.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("mobile", phone);
        criteria.andGreaterThanOrEqualTo("createTime", nowDay);
        int count = smsMapper.selectCountByCondition(condition);
        if (count > propertiesVal.getSmsMaxCount()) {
            apiResult.setMsg(ResultEnum.SMS_COUNT_CHECKOUT.getMsg());
            apiResult.setCode(ResultEnum.SMS_COUNT_CHECKOUT.getCode());
            return apiResult;
        }

        Condition condition1 = new Condition(Sms.class);
        condition1.orderBy("createTime").desc();
        Example.Criteria criteria1 = condition1.createCriteria();
        criteria1.andEqualTo("mobile", phone);
        List<Sms> smsList = smsMapper.selectByCondition(condition1);
        if (!CollectionUtils.isEmpty(smsList)) {
            long d = new Date().getTime() - smsList.get(0).getCreateTime().getTime();
            if (d <= propertiesVal.getSmsTimeOut()) { // 120秒后才能重新发送
                apiResult.setMsg(ResultEnum.SMS_TIME_OUT.getMsg());
                apiResult.setCode(ResultEnum.SMS_TIME_OUT.getCode());
                return apiResult;
            }
        }
        return apiResult;
    }

}
