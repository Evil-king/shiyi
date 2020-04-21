package com.baibei.shiyi.trade.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.EntrustOrderResultEnum;
import com.baibei.shiyi.trade.common.dto.BatchRevokeDto;
import com.baibei.shiyi.trade.common.dto.RevokeByDirectionDto;
import com.baibei.shiyi.trade.common.dto.RevokeDto;
import com.baibei.shiyi.trade.model.EntrustOrder;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.IRevokeService;
import com.baibei.shiyi.trade.utils.TradeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/30 18:33
 * @description:
 */
@Component
@Slf4j
public class RevokeBiz {
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private IRevokeService revokeService;
    @Autowired
    private TradeUtil tradeUtil;

    /**
     * 全部撤单
     *
     * @param dto
     * @return
     */
    public ApiResult allRevoke(RevokeDto dto) {
        List<EntrustOrder> list = entrustOrderService.listByParam(dto.getCustomerNo(), null,
                dto.getProductTradeNo(), EntrustOrderResultEnum.WAIT_DEAL.getCode());
        for (EntrustOrder entrustOrder : list) {
            try {
                revoke(entrustOrder, EntrustOrderResultEnum.CUSTOMER_REVOKE.getCode());
            } catch (Exception e) {
                log.error(String.format("委托单【%s】撤单异常", entrustOrder.getEntrustNo()), e);
            }
        }
        return ApiResult.success();
    }

    /**
     * 根据方向撤单
     *
     * @param dto
     * @return
     */
    public ApiResult revokeByDirection(RevokeByDirectionDto dto) {
        List<EntrustOrder> list = entrustOrderService.listByParam(dto.getCustomerNo(), dto.getDirection(),
                dto.getProductTradeNo(), EntrustOrderResultEnum.WAIT_DEAL.getCode());
        for (EntrustOrder entrustOrder : list) {
            try {
                revoke(entrustOrder, EntrustOrderResultEnum.CUSTOMER_REVOKE.getCode());
            } catch (Exception e) {
                log.error(String.format("委托单【%s】撤单异常", entrustOrder.getEntrustNo()), e);
            }
        }
        return ApiResult.success();
    }

    /**
     * 系统撤单
     *
     * @return
     */
    public ApiResult systemRevoke() {
        long start = System.currentTimeMillis();
        log.info("开始系统撤单");
        List<EntrustOrder> list = entrustOrderService.listByResult(EntrustOrderResultEnum.WAIT_DEAL.getCode());
        for (EntrustOrder entrustOrder : list) {
            try {
                revoke(entrustOrder, EntrustOrderResultEnum.SYSTEM_REVOKE.getCode());
            } catch (Exception e) {
                log.error(String.format("委托单【%s】撤单异常", entrustOrder.getEntrustNo()), e);
            }
        }
        log.info("系统撤单结束，耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * 客户批量撤单
     *
     * @param dto
     * @return
     */
    public ApiResult batchRevoke(BatchRevokeDto dto) {
        List<String> entrustNoList = dto.getEntrustNoList();
        for (String entrustNo : entrustNoList) {
            EntrustOrder entrustOrder = entrustOrderService.findByOrderNo(entrustNo);
            if (entrustOrder == null) {
                log.info("委托单【{}】不存在", entrustNo);
                continue;
            }
            if (!dto.getCustomerNo().equals(entrustOrder.getCustomerNo())) {
                log.info("委托单归属错误");
                continue;
            }
            // 撤单
            try {
                revoke(entrustOrder, EntrustOrderResultEnum.CUSTOMER_REVOKE.getCode());
            } catch (Exception e) {
                log.error(String.format("委托单【%s】撤单异常", entrustOrder.getEntrustNo()), e);
            }
        }
        return ApiResult.success();
    }

    /**
     * 针对委托单进行撤单
     *
     * @param entrustOrder
     * @param result
     */
    public void revoke(EntrustOrder entrustOrder, String result) {
        if (!EntrustOrderResultEnum.WAIT_DEAL.getCode().equals(entrustOrder.getResult())) {
            log.info("委托单状态不支持撤单，entrustOrder={}", JSON.toJSONString(entrustOrder));
            return;
        }
        if (Constants.TradeDirection.BUY.equals(entrustOrder.getDirection())) {
            // 撤单
            revokeService.revokeBuyOrder(entrustOrder, result);
            // 发送行情数据
            tradeUtil.pushTradeOrder(Constants.QuotationOperateType.REVOKE_BUY, entrustOrder.getProductTradeNo(),
                    entrustOrder.getPrice(), entrustOrder.getWaitCount(), entrustOrder.getEntrustNo(), new Date());
        } else if (Constants.TradeDirection.SELL.equals(entrustOrder.getDirection())) {
            // 撤单
            revokeService.revokeSellOrder(entrustOrder, result);
            // 发送行情数据
            tradeUtil.pushTradeOrder(Constants.QuotationOperateType.REVOKE_SELL, entrustOrder.getProductTradeNo(),
                    entrustOrder.getPrice(), entrustOrder.getWaitCount(), entrustOrder.getEntrustNo(), new Date());
        } else {
            log.info("错误的委托单方向");
        }
    }
}