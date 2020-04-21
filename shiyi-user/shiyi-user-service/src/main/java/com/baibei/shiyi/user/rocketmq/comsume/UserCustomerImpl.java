package com.baibei.shiyi.user.rocketmq.comsume;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCustomerImpl implements IConsumer<PABCustomerVo> {


    @Autowired
    private ICustomerService customerService;

    @Override
    public ApiResult execute(PABCustomerVo pabCustomerVo) {
        if (pabCustomerVo == null || pabCustomerVo.getCustomerNo() == null) {
            log.info("获取签约用户的信息失败{}", JSONObject.toJSONString(pabCustomerVo));
            return ApiResult.error();
        }
        log.info("监听消费的用户的信息为{}", JSONObject.toJSONString(pabCustomerVo));
        customerService.updateCustomerDetail(pabCustomerVo);
        return ApiResult.success();
    }
}
