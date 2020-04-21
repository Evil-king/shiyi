package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.bo.ListBo;
import com.baibei.shiyi.trade.common.dto.EntrustOrderListDto;
import com.baibei.shiyi.trade.common.dto.MyEntrustDetailsDto;
import com.baibei.shiyi.trade.common.dto.PcEntrustListAndCustomerDto;
import com.baibei.shiyi.trade.common.dto.PcEntrustListDto;
import com.baibei.shiyi.trade.common.vo.EntrustOrderListVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustDetailsVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustOrderVo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerEntrustOrderListVo;
import com.baibei.shiyi.trade.model.EntrustOrder;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/24 16:03:43
 * @description: EntrustOrder服务接口
 */
public interface IEntrustOrderService extends Service<EntrustOrder> {

    /**
     * 创建委托单
     *
     * @param listBo
     */
    void save(ListBo listBo);

    /**
     * 修改委托单状态
     *
     * @param entrustNo
     * @param status
     */
    void updateEntrustStatus(String entrustNo, String status);

    /**
     * 摘牌修改委托单状态
     *
     * @param entrustOrder
     * @param count
     */
    void updateEntrustByDelist(EntrustOrder entrustOrder, Integer count);

    /**
     * 当前挂牌
     *
     * @param dto
     * @return
     */
    MyPageInfo<EntrustOrderListVo> entrustOrderList(EntrustOrderListDto dto);


    /**
     * 我的委托单列表
     *
     * @param customerNo
     * @return
     */
    ApiResult<List<MyEntrustOrderVo>> myEntrustList(String customerNo);

    /**
     * 委托单详情
     *
     * @param myEntrustDetailsDto
     * @return
     */
    ApiResult<MyEntrustDetailsVo> myEntrustDetails(MyEntrustDetailsDto myEntrustDetailsDto);

    /**
     * 根据委托单号查询信息
     *
     * @param entrustOrderNo
     * @return
     */
    EntrustOrder findByOrderNo(String entrustOrderNo);


    MyPageInfo<CustomerEntrustOrderListVo> mypageList(CustomerEntrustOrderListDto customerEntrustOrderListDto);

    List<CustomerEntrustOrderListVo> export(CustomerEntrustOrderListDto customerEntrustOrderListDto);

    /**
     * 根据委托结果查询
     *
     * @param result
     * @return
     */
    List<EntrustOrder> listByResult(String result);


    /**
     * 根据条件查询
     *
     * @param customerNo
     * @param direction
     * @param productTradeNo
     * @param result
     * @return
     */
    List<EntrustOrder> listByParam(String customerNo, String direction, String productTradeNo, String result);

    /**
     * 撤单
     *
     * @param entrustOrder
     * @param result
     */
    void revoke(EntrustOrder entrustOrder, String result);

    /**
     * PC端挂单列表
     * @param pcEntrustListDto
     * @return
     */
    ApiResult<List<MyEntrustOrderVo>> myPcEntrustList(PcEntrustListDto pcEntrustListDto);

    MyPageInfo<MyEntrustOrderVo> myPcEntrustListAndCustomerNo(PcEntrustListAndCustomerDto pcEntrustListAndCustomerDto);
}
