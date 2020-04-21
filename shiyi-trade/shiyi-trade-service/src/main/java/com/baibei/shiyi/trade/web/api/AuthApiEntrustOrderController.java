package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.dto.*;
import com.baibei.shiyi.trade.common.vo.EntrustOrderListVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustDetailsVo;
import com.baibei.shiyi.trade.common.vo.MyEntrustOrderVo;
import com.baibei.shiyi.trade.common.vo.MyHoldVo;
import com.baibei.shiyi.trade.service.IDealOrderService;
import com.baibei.shiyi.trade.service.IEntrustOrderService;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth/api/trade/entrust")
public class AuthApiEntrustOrderController {
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IEntrustOrderService entrustOrderService;
    @Autowired
    private IDealOrderService dealOrderService;

    /**
     * 当前挂牌
     *
     * @param dto
     * @return
     */
    @PostMapping("/current")
    public ApiResult<MyPageInfo<EntrustOrderListVo>> current(@RequestBody @Validated EntrustOrderListDto dto) {
        return ApiResult.success(entrustOrderService.entrustOrderList(dto));
    }

    /**
     * 我的持仓单
     *
     * @return
     */
    @RequestMapping("/myHoldList")
    public ApiResult<List<MyHoldVo>> myHoldList(@RequestBody MyHoldDto myHoldDto) {
        return holdDetailsService.myHoldList(myHoldDto);
    }

    /**
     * 我的委托单列表
     *
     * @return
     */
    @RequestMapping("/myEntrustList")
    public ApiResult<List<MyEntrustOrderVo>> myEntrustList(@RequestBody CustomerBaseDto customerBaseDto) {
        return entrustOrderService.myEntrustList(customerBaseDto.getCustomerNo());
    }

    /**
     * 我的委托单列表PC
     *
     * @return
     */
    @RequestMapping("/myPcEntrustList")
    public ApiResult<List<MyEntrustOrderVo>> myPcEntrustList(@RequestBody PcEntrustListDto pcEntrustListDto) {
        return entrustOrderService.myPcEntrustList(pcEntrustListDto);
    }

    /**
     * 我的委托单列表PC参数为用户编号
     *
     * @return
     */
    @RequestMapping("/myPcEntrustListAndCustomer")
    public ApiResult<MyPageInfo<MyEntrustOrderVo>> myPcEntrustList(@RequestBody PcEntrustListAndCustomerDto pcEntrustListAndCustomerDto) {
        return ApiResult.success(entrustOrderService.myPcEntrustListAndCustomerNo(pcEntrustListAndCustomerDto));
    }


    /**
     * 我的委托单详情
     *
     * @return
     */
    @RequestMapping("/myEntrustDetails")
    public ApiResult<MyEntrustDetailsVo> myEntrustDetails(@RequestBody MyEntrustDetailsDto myEntrustDetailsDto) {
        return entrustOrderService.myEntrustDetails(myEntrustDetailsDto);
    }


    /**
     * 我的购销记录
     *
     * @param myDealOorderListDto
     * @return
     */
    @RequestMapping("/dealOrderList")
    public ApiResult dealOrderList(@RequestBody MyDealOorderListDto myDealOorderListDto) {
        return ApiResult.success(dealOrderService.dealOrderList(myDealOorderListDto));
    }
}
