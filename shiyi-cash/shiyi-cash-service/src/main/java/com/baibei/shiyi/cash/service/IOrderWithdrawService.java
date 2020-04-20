package com.baibei.shiyi.cash.service;
import com.baibei.shiyi.cash.feign.base.dto.AuditOrderDto;
import com.baibei.shiyi.cash.feign.base.dto.OrderWithdrawDto;
import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.dto.WithdrawListDto;
import com.baibei.shiyi.cash.feign.base.message.Apply1318ConsumerMessage;
import com.baibei.shiyi.cash.feign.base.message.WithdrawDetuchAccountMessage;
import com.baibei.shiyi.cash.feign.base.vo.AddMoneyFlagVo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: Longer
* @date: 2019/10/31 14:08:48
* @description: OrderWithdraw服务接口
*/
public interface IOrderWithdrawService extends Service<OrderWithdraw> {

    /**
     * 提现申请
     * @param orderWithdrawDto
     */
    OrderWithdrawDto withdrawApplication(OrderWithdrawDto orderWithdrawDto);

    /**
     * 获取已经审核过的出金订单集合（审核通过和审核不通过的出金订单）,用于调用1318接口请求出金
     * @return
     */
    List<OrderWithdraw> select1318OrderList();

    /**
     * 获取银行端发起的出金订单
     * @return
     */
    List<OrderWithdraw> select1317OrderList();
    /**
     * 调用执行1318接口
     * @param orderWithdraw
     */
    void apply1318(OrderWithdraw orderWithdraw);

    /**
     * 请求1318接口结果处理逻辑（异步调用1318接口出金后，平安代理服务发送异步调用结果消息置本服务后的处理逻辑）
     * @param apiResultApply1318ConsumerMessage
     */
    void apply1318Ack(Apply1318ConsumerMessage apiResultApply1318ConsumerMessage);

    /**
     * 查询交易网发起状态为3的订单
     * @return
     */
    List<OrderWithdraw> selectDoingList();

    /**
     * 根据状态查询订单
     * @return
     */
    List<OrderWithdraw> selectListByStatus(String status);

    /**
     * 交易网--->银行(1325接口)
     * @param orderWithdraw
     */
    ApiResult<Boolean> apply1325(OrderWithdraw orderWithdraw);



    /**
     * 根据订单号，更新状态
     */
    void updateStatusByOrderNo(String orderNo,String status);

    /**
     * 审核出金订单
     * @param auditOrderDto
     */
    ApiResult<AddMoneyFlagVo> auditOrder(AuditOrderDto auditOrderDto);

    OrderWithdraw getByOrderNo(String orderNo);

    MyPageInfo<WithdrawListVo> pageList(WithdrawListDto withdrawListDto);

    /**
     * 判断每个用户当天出金额
     * @param customerNo
     * @return
     */
    BigDecimal sumAmountOfCustomer(String customerNo);

    /**
     *  出金申请扣减余额，回调更新订单状态
     * @param withdrawDetuchAccountMessage
     */
    void accountAckUpdateStatus(WithdrawDetuchAccountMessage withdrawDetuchAccountMessage);


    /**
     * 银行发起出金请求(接口文档1312)
     *
     * @param message
     * @return
     */
    WithdrawForBank1312Vo withdrawForBank1312(String message);

    /**
     * 1317接口
     * @param orderWithdraw
     */
    void apply1317(OrderWithdraw orderWithdraw);

    /**
     * 根据订单号更新订单。（乐观锁实现）
     * @param updateEntity
     * @return
     */
    int safetyUpdateOrderBySelective(OrderWithdraw updateEntity,String orderNo);

    /**
     * 根据订单号更新订单2。（乐观锁实现）
     * @param updateEntity
     * @return
     */
    int safetyUpdateOrderBySelective2(OrderWithdraw updateEntity,OrderWithdraw resouceEntity,String orderNo);

    /**
     * 创建订单
     * @param orderWithdrawDto
     */
    void createOrder(OrderWithdrawDto orderWithdrawDto);


    /**
     * 出金流水
     * @return
     */
    List<OrderWithdraw> getPeriodOrderList(String batchNo);

    /**
     * 根据外部流水号查询流水(1005)
     *
     * @param externalNo 外部流水号
     * @return
     */
    OrderWithdraw getOrderByExternalNo(String externalNo);

    int deleteByOrderNo(String orderNo);

    /**
     * 根据状态获取列表
     * @return
     */
    List<OrderWithdraw> getListByStatus(String status);

    List<WithdrawListVo> getWithdrawList(WithdrawListDto withdrawListDto);

    /**
     * 签退的时候将提现申请中的订单置为审核失败
     */
    void operatorReview();

    /**
     * 请求福清接口出金
     * @param orderWithdraw
     */
    void applyFuqing315002(OrderWithdraw orderWithdraw);

    /**
     * 请求福清接口查询出金订单流水
     * @param orderWithdraw
     */
    void applyFuqing315003(OrderWithdraw orderWithdraw);
}
