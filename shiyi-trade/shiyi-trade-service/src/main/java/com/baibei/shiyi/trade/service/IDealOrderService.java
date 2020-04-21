package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.bo.DeListBo;
import com.baibei.shiyi.trade.common.dto.DealOrderQueryDTO;
import com.baibei.shiyi.trade.common.dto.MyDealOorderListDto;
import com.baibei.shiyi.trade.common.vo.DealOrderHistoryVo;
import com.baibei.shiyi.trade.common.vo.MyDealOrderListVo;
import com.baibei.shiyi.trade.feign.bean.dto.DealOrderDto;
import com.baibei.shiyi.trade.feign.bean.vo.DealOrderVo;
import com.baibei.shiyi.trade.model.DealOrder;

import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/11/12 19:13:49
 * @description: DealOrder服务接口
 */
public interface IDealOrderService extends Service<DealOrder> {
    /**
     * 初始化成交单
     *
     * @param bo
     */
    void save(DeListBo bo);

    /**
     * 修改状态
     *
     * @param dealNo
     * @param status
     */
    void updateStatus(String dealNo, String status);

    MyPageInfo<MyDealOrderListVo> dealOrderList(MyDealOorderListDto myDealOorderListDto);

    MyPageInfo<DealOrderVo> myPageList(DealOrderDto dealOrderDto);

    List<DealOrderVo> List(DealOrderDto dealOrderDto);

    /**
     * 市场交易记录
     *
     * @param dealOrderQueryDTO
     * @return
     */
    MyPageInfo<DealOrderHistoryVo> customerHistoryList(DealOrderQueryDTO dealOrderQueryDTO);
}
