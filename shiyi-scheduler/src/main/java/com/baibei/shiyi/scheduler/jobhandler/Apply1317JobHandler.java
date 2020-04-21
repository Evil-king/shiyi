package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/11/4 16:11
 * @description:定时调用1317接口
 */
@JobHandler(value = "apply1317JobHandler")
@Component
public class Apply1317JobHandler extends IJobHandler {
    @Autowired
    private IShiyiWithdrawFeign shiyiWithdrawFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("调用1318接口定时任务开始执行");
        ApiResult apiResult = shiyiWithdrawFeign.applySchedule1317();
        XxlJobLogger.log("调用1318接口定时任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}