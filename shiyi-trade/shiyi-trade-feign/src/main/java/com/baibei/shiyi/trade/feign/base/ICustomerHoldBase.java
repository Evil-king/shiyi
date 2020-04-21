package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerHoldDto;
import com.baibei.shiyi.trade.feign.bean.vo.CustomerHoldVo;
import com.baibei.shiyi.trade.feign.bean.vo.StatisticsVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/29 11:22 AM
 * @description:
 */
public interface ICustomerHoldBase {

    /**
     * 获取客户持仓商品数量信息
     *
     * @param customerHoldDto
     * @return
     */
    @PostMapping(value = "shiyi/customerHold/info")
    ApiResult<CustomerHoldVo> customerHoldInfo(@RequestBody @Validated CustomerHoldDto customerHoldDto);

    /**
     * 客户所有商品的持仓资金数据统计
     *
     * @param customerNo
     * @return
     */
    @GetMapping(value = "/shiyi/customerHold/statistics")
    ApiResult<StatisticsVo> marketValue(@RequestParam("customerNo") String customerNo);

}

