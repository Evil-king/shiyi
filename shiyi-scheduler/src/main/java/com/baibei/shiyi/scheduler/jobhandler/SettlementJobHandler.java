package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.settlement.feign.client.ISettlementFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/22 15:32
 * @description: 生成清算数据
 */
@JobHandler(value = "settlementJobHandler")
@Component
public class SettlementJobHandler extends IJobHandler {
    @Autowired
    private ISettlementFeign settlementFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("生成清算数据定时任务开始执行");
        ApiResult apiResult = settlementFeign.generate();
        XxlJobLogger.log("生成清算数据定时任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}