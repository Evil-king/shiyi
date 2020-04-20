package com.baibei.shiyi.cash.rocketmq.comsume;

import com.baibei.component.rocketmq.core.consumer.IConsumer;
import com.baibei.shiyi.cash.service.IBankOrderService;
import com.baibei.shiyi.cash.service.IWithDrawDepositDiffService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.pingan.feign.base.dto.ViewFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author: Longer
 * @date: 2019/11/1 10:41
 * @description: 获取出入金对账文件监听实现
 */
@Component
@Slf4j
public class FindFileConsumerImpl implements IConsumer<ViewFileDto> {
    @Autowired
    private IBankOrderService bankOrderService;
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;
    private SimpleDateFormat yyyyMMddNoLine = (SimpleDateFormat) DateUtil.yyyyMMddNoLine.get();

    @Override
    public ApiResult execute(ViewFileDto viewFileDto) {
        ApiResult apiResult;
        try{
            if (!"none.txt".equals(viewFileDto.getFileName())) {
                bankOrderService.bankOrder(viewFileDto);
            }
            apiResult=ApiResult.success();
            //发送出入金对账消息
            String batchNo = yyyyMMddNoLine.format(new Date());
            //进行出入金对账
            withDrawDepositDiffService.withDrawDepositDiff(batchNo);
          /*  CleanFlowPathDto cleanFlowPathDto = new CleanFlowPathDto();
            cleanFlowPathDto.setBatchNo(batchNo);
            cleanFlowPathDto.setProjectCode(Constants.CleanFlowPathCode.ACCOUNTCHECK_FILE);
            cleanFlowPathDto.setStatus(Constants.CleanFlowPathStatus.COMPLETED);*/
            //更新清算流程状态
            /*settlementBase.updateCleanFlow(cleanFlowPathDto);*/
        }catch (Exception e){
            log.error("获取出入金对账文件消息执行报错：",e);
            e.printStackTrace();
            apiResult=ApiResult.error();
        }
        return apiResult;
    }
}