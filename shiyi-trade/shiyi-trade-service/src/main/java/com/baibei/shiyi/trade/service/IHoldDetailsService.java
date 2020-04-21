package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.common.dto.MyHoldDto;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.MyHoldVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldPageListVo;
import com.baibei.shiyi.trade.model.HoldDetails;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/05/28 19:42:41
 * @description: HoldDetails服务接口
 */
public interface IHoldDetailsService extends Service<HoldDetails> {
    /**
     * 新增持仓明细记录
     *
     * @param bo
     */
    void save(ChangeHoldPositionBo bo);

    /**
     * 我的持仓单
     *
     * @param myHoldDto
     * @return
     */
    ApiResult<List<MyHoldVo>> myHoldList(MyHoldDto myHoldDto);


    /**
     * 后台持仓列表
     *
     * @param customerHoldPageListDto
     * @return
     */
    MyPageInfo<CustomerHoldPageListVo> mypageList(CustomerHoldPageListDto customerHoldPageListDto);

    /**
     * 导出数据所用到的所有数据
     *
     * @param customerHoldPageListDto
     * @return
     */
    List<CustomerHoldPageListVo> export(CustomerHoldPageListDto customerHoldPageListDto);

    ApiResult<TotalMarketValueVo> getTotalMarketValue(CustomerNoDto customerNoDto);

    MyPageInfo<HoldDetailListVo> getPageList(CustomerNoPageDto customerNoDto);

    /**
     * 查询达到可交易日期的持仓详情列表
     *
     * @return
     */
    List<HoldDetails> findCanTradeList();

    /**
     * 计算持仓成本价
     *
     * @param customerNo
     * @param productTradeNo
     */
    void calculationCost(String customerNo, String productTradeNo);

}
