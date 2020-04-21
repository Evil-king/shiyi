package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.account.feign.base.shiyi.IPassCardExtractOrderBase;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * T+N解锁
 */
@JobHandler(value = "apiPassCardExtractOrderHandler")
@Component
public class ApiPassCardExtractOrderHandler extends IJobHandler {

    @Autowired
    private IPassCardExtractOrderBase passCardExtractOrderBase;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("提取仓单自动审核定时任务开始执行");
        passCardExtractOrderBase.systemOperation();
        XxlJobLogger.log("提取仓单自动审核定时任务执行完毕");
        return SUCCESS;
    }
}
