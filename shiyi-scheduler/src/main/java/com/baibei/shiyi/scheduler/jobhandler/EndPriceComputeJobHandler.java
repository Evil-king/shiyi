package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.quotation.feign.client.shiyi.IShiyiQuotationFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/12/30 15:24
 * @description:定时计算（当天的收盘价）
 */
@JobHandler(value = "endPriceComputeJobHandler")
@Component
public class EndPriceComputeJobHandler extends IJobHandler {

    @Autowired
    private IShiyiQuotationFeign quotationFeign;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("定时计算（当日收盘价）任务开始执行");
        ApiResult apiResult = quotationFeign.endPrice();
        XxlJobLogger.log("定时计算（当日收盘价）任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}