package com.baibei.shiyi.cash.web.shiyi;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiWithdrawFeign;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shiyi/withdraw")
public class ShiyiOrderWithdrawController implements IShiyiWithdrawFeign {

    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private PropertiesVal propertiesVal;

    /**
     * 定时扫描处理已经审核通过过的出金订单，调用1318接口进行出金
     * @return
     */
    @Override
    public ApiResult applySchedule1318(){
        long start = System.currentTimeMillis();
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService.select1318OrderList();
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行1318发起出金，此次处理订单数量为{}", size);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (int i = 0; i < orderWithdrawList.size(); i++) {
                OrderWithdraw orderWithdraw = orderWithdrawList.get(i);
                try {
                    orderWithdrawService.apply1318(orderWithdraw);
                } catch (Exception e) {
                    log.error("调用1318接口进行出金报错：",e);
                    e.printStackTrace();
                }
            }
        } else {
            log.info("applySchedule1318：无出金订单需要处理");
        }
        log.info("结束执行1318发起出金，耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * applySchedule1325
     * 交易网--->银行(定时任务--1325接口)
     *
     * @return
     */
    @Override
    public ApiResult applySchedule1325() {
        long start = System.currentTimeMillis();
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService.selectDoingList();
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行1325订单查询，此次处理订单数量为{}", size);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (OrderWithdraw orderWithdraw : orderWithdrawList) {
                try {
                    ApiResult<Boolean> booleanApiResult = orderWithdrawService.apply1325(orderWithdraw);
                    Boolean addMoneyFlag = booleanApiResult.getData();
                    if(addMoneyFlag){//需要异步加钱
                        log.info("1325：异步加钱消息发送....");
                        //异步加钱
                        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                        changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                        changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
                        changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
                        changeAmountDto.setReType(Constants.Retype.IN);
                        changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_BACK.getCode());
                        //发送加钱消息
                        rocketMQUtil.sendMsg(propertiesVal.getWithdrawAddMoneyTopic(),
                                JacksonUtil.beanToJson(changeAmountDto),changeAmountDto.getOrderNo());
                    }
                    Thread.sleep(5 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("结束执行1325订单查询，耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * 监听银行端发起出金调用方法(监听1312接口)
     * @param withdrawForBank1312Dto
     * @return
     */
    @Override
    public ApiResult<WithdrawForBank1312Vo> withdrawForBank1312(@RequestBody @Validated WithdrawForBank1312Dto withdrawForBank1312Dto) {
        WithdrawForBank1312Vo withdrawForBank1312Vo = orderWithdrawService.withdrawForBank1312(withdrawForBank1312Dto.getMessage());
        return ApiResult.success(withdrawForBank1312Vo);
    }

    /**
     * 定时扫描处理银行端发起的出金订单，调用1317接口
     * @return
     */
    @Override
    public ApiResult applySchedule1317(){
        long start = System.currentTimeMillis();
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService.select1317OrderList();
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行1317，此次处理订单数量为{}", size);
        if (!CollectionUtils.isEmpty(orderWithdrawList)) {
            for (int i = 0; i < orderWithdrawList.size(); i++) {
                OrderWithdraw orderWithdraw = orderWithdrawList.get(i);
                try {
                    orderWithdrawService.apply1317(orderWithdraw);
                    Thread.sleep(5 * 1000);
                } catch (Exception e) {
                    log.error("调用1317接口进行出金报错：",e);
                    e.printStackTrace();
                }
            }
        } else {
            log.info("applySchedule1317：无出金订单需要处理");
        }
        log.info("结束执行1317，耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    @Override
    public ApiResult operatorReview() {
        ApiResult apiResult;
        try{
            orderWithdrawService.operatorReview();
            apiResult=ApiResult.success();
        }catch (Exception e){
            log.info("签退的时候将提现申请中的订单置为审核失败报错",e);
            apiResult=ApiResult.error();
        }
        return apiResult;
    }

    /**
     * 定时扫描审核通过的订单，调用福清接口进行出金
     * @return
     */
    @Override
    public ApiResult applyScheduleFuqing315002() {
        long start = System.currentTimeMillis();
        //获取审核通过的提现订单
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService
                .selectListByStatus(Constants.OrderWithdrawStatus.WITHDRAW_PASS);
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行applyScheduleFuqing315002出金，此次处理订单数量为{}", size);
        for (OrderWithdraw orderWithdraw : orderWithdrawList) {
            try{
                //请求福清
                orderWithdrawService.applyFuqing315002(orderWithdraw);
            }catch (Exception e){
                log.info("applyFuqing315002报错：{}",e);
            }
        }
        log.info("结束执行applyScheduleFuqing315002出金，耗时{}ms", (System.currentTimeMillis() - start));
        return ApiResult.success();
    }

    /**
     * 定时扫描查询处理中的订单，查看银行处理结果
     * @return
     */
    @Override
    public ApiResult applyScheduleFuqing315003() {
        //获取审核通过的提现订单
        List<OrderWithdraw> orderWithdrawList = orderWithdrawService
                .selectListByStatus(Constants.OrderWithdrawStatus.WITHDRAW_DOING);
        int size = CollectionUtils.isEmpty(orderWithdrawList) ? 0 : orderWithdrawList.size();
        log.info("正在执行applyScheduleFuqing315003查询出金流水，此次查询订单数量为{}", size);
        for (OrderWithdraw orderWithdraw : orderWithdrawList) {
            try{
                orderWithdrawService.applyFuqing315003(orderWithdraw);
                Thread.sleep(2 * 1000);
            }catch (Exception e){
                log.info("applyFuqing315003报错：{}",e);
            }
        }
        return ApiResult.success();
    }


}
