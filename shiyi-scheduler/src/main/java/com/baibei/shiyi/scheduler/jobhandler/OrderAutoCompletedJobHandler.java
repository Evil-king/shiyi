package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.client.IShiyiAutoDeliveryOrderFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/22 15:32
 * @description:
 */
@JobHandler(value = "orderAutoCompletedJobHandler")
@Component
public class OrderAutoCompletedJobHandler extends IJobHandler {
    @Autowired
    private IShiyiAutoDeliveryOrderFeign shiyiAutoDeliveryOrderFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("订单自动收货定时任务开始执行");
        ApiResult apiResult = shiyiAutoDeliveryOrderFeign.autoDelivery();
        XxlJobLogger.log("订单自动收货定时任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}