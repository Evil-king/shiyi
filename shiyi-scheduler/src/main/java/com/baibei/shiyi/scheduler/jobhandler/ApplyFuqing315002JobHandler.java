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
 * @date: 2019/12/05 19:51
 * @description:定时调用福清315002接口进行出金
 */
@JobHandler(value = "applyFuqing315002JobHandler")
@Component
public class ApplyFuqing315002JobHandler extends IJobHandler {
    @Autowired
    private IShiyiWithdrawFeign shiyiWithdrawFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("定时调用福清315002接口进行出金任务开始执行");
        ApiResult apiResult = shiyiWithdrawFeign.applyScheduleFuqing315002();
        XxlJobLogger.log("定时调用福清315002接口进行出金任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}