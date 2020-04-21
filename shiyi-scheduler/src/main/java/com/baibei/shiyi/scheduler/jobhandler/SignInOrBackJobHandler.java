package com.baibei.shiyi.scheduler.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.cash.feign.base.vo.SignInBackVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiSigningInBackFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时签到
 */
@JobHandler(value = "SignInOrBackJobHandler")
@Service
@Slf4j
public class SignInOrBackJobHandler extends IJobHandler {

    @Autowired
    private IShiyiSigningInBackFeign shiyiSigningInBackFeign;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("开始签到");
        ApiResult<SignInBackVo> signInBackVoApiResult = shiyiSigningInBackFeign.signIn();
        if (signInBackVoApiResult.hasFail()) {
            log.info("签到结束———>签到失败:结果为code->{},msg——>{}",signInBackVoApiResult.getCode(),signInBackVoApiResult.getData());
            return ReturnT.FAIL;
        }
        log.info("签到成功,签到结果为{}", JSONObject.toJSONString(signInBackVoApiResult.getData()));
        return ReturnT.SUCCESS;
    }
}
