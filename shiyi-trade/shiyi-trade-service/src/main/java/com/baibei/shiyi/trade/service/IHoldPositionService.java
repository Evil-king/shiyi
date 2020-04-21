package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.dto.HoldPositionListDto;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.model.HoldPosition;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/27 11:27:48
 * @description: HoldPosition服务接口
 */
public interface IHoldPositionService extends Service<HoldPosition> {

    /**
     * 查询客户指定商品持仓信息
     *
     * @param customerNo
     * @return
     */
    List<HoldPosition> selectByCustomer(String customerNo);

    HoldPosition selectByCustomerAndProductTradeNo(String customerNo, String productTradeNo);

    ApiResult<TotalMarketValueVo> getTotalMarketValue(CustomerNoDto customerNoDto);

    MyPageInfo<HoldDetailListVo> getPageList(CustomerNoPageDto customerNoDto);

    HoldPosition find(String customerNo, String productTradeNo);

    MyPageInfo<HoldPositionVo> pageList(HoldPositionDto holdPositionDto);

    /**
     * 更新成本价
     *
     * @param customerNo
     * @param productTradeNo
     * @param cost
     */
    void updateCost(String customerNo, String productTradeNo, BigDecimal cost);

    /**
     * 统计所有用户可卖量的总和
     * @param productTradeNo
     * @return
     */
    Integer sumCanSellCount(String productTradeNo);

    MyPageInfo<HoldDetailListVo> getPcPageList(HoldPositionListDto holdPositionListDto);
}
