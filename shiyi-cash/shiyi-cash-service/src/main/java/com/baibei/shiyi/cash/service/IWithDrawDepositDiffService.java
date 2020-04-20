package com.baibei.shiyi.cash.service;

import com.baibei.shiyi.cash.feign.base.dto.DealDiffDto;
import com.baibei.shiyi.cash.feign.base.dto.WithDrawDepositDiffDto;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.cash.feign.base.vo.WithDrawDepositDiffVo;
import com.baibei.shiyi.cash.model.WithDrawDepositDiff;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: Longer
* @date: 2019/11/06 13:51:22
* @description: WithDrawDepositDiff服务接口
*/
public interface IWithDrawDepositDiffService extends Service<WithDrawDepositDiff> {

    /**
     * 出入金对账
     * @param batchNo 批次号
     * @return
     */
    ApiResult withDrawDepositDiff(String batchNo);

    int deleteByBatchNo(String batchNo);

    /**
     * 出入金调账
     * @param dealDiffDto
     * @return
     */
    ApiResult<DealDiffMessage> dealDiff(DealDiffDto dealDiffDto);


    /**
     * 更新，乐观锁处理（只能更新 状态为 wait 的记录）
     * @param withDrawDepositDiff
     * @return
     */
    int updateDiffWithWait(WithDrawDepositDiff withDrawDepositDiff);

    /**
     * 调账异步修改资金结果执行逻辑
     * @param dealDiffMessage
     */
    void dealDiffAck(DealDiffMessage dealDiffMessage);

    MyPageInfo<WithDrawDepositDiffVo> pageList(WithDrawDepositDiffDto withDrawDepositDiffDto);

    List<WithDrawDepositDiffVo> WithDrawDepositDiffVoList(WithDrawDepositDiffDto withDrawDepositDiffDto);

}
