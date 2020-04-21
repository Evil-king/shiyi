package com.baibei.shiyi.scheduler.jobhandler.trade;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.client.shiyi.ShiyiSchedulerFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/7 15:59
 * @description: 定时设置当前交易日配置至Redis
 */
@JobHandler(value = "updateTradeDayJobHandler")
@Component
public class UpdateTradeDayJobHandler extends IJobHandler {
    @Autowired
    private ShiyiSchedulerFeign shiyiSchedulerFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        /*XxlJobLogger.log("定时设置当前交易日配置至Redis任务开始执行");
        ApiResult apiResult = shiyiSchedulerFeign.updateTradeDay();
        XxlJobLogger.log("定时设置当前交易日配置至Redis任务执行完毕，执行结果为{}", apiResult.toString());*/
        return ReturnT.SUCCESS;
    }
}