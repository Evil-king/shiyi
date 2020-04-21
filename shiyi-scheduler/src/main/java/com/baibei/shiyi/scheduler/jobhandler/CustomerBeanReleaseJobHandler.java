package com.baibei.shiyi.scheduler.jobhandler;

import com.baibei.shiyi.account.feign.client.CustomerBeanFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: hyc
 * @date: 2019/11/19 15:57
 * @description:
 */
@JobHandler(value="customerBeanReleaseJobHandler")
@Component
public class CustomerBeanReleaseJobHandler extends IJobHandler {
    @Autowired
    private CustomerBeanFeign customerBeanFeign;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("每日释放待赋能积分定时任务开始");
        ApiResult apiResult = customerBeanFeign.release();
        XxlJobLogger.log("调用每日释放待赋能积分定时任务执行完毕，apiResult={}", apiResult.toString());
        return SUCCESS;
    }
}
