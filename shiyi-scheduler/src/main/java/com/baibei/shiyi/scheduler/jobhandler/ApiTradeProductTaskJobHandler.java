package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.trade.feign.client.admin.IAdminTradeProductFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时扫描交易商品是否到达上市时间
 */
@JobHandler(value = "apiTradeProductTaskJobHandler")
@Component
public class ApiTradeProductTaskJobHandler extends IJobHandler {

    @Autowired
    private IAdminTradeProductFeign tradeProductFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("定时扫描交易商品是否到达上市时间定时任务开始执行");
        tradeProductFeign.modifyStatus();
        XxlJobLogger.log("定时扫描交易商品是否到达上市时间定时任务执行完毕");
        return SUCCESS;
    }
}
