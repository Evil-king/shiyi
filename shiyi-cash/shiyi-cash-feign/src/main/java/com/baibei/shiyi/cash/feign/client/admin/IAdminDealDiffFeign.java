package com.baibei.shiyi.cash.feign.client.admin;

import com.baibei.shiyi.cash.feign.base.dto.BankOrderDto;
import com.baibei.shiyi.cash.feign.base.dto.DealDiffDto;
import com.baibei.shiyi.cash.feign.base.vo.BankOrderVo;
import com.baibei.shiyi.cash.feign.client.hystrix.DealDiffHystrix;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/admin/diff", fallbackFactory = DealDiffHystrix.class)
public interface IAdminDealDiffFeign {


    /**
     * 出入金对账
     * batchNo 批次号20191011
     * @return
     */
    @RequestMapping("/diffList")
    ApiResult diffList(String batchNo);

    /**
     * 冲正
     * @param dealDiffDto
     * @return
     */
    @RequestMapping("/dealDiff")
    ApiResult dealDiff(@RequestBody @Validated DealDiffDto dealDiffDto);



    @RequestMapping(value = "/bankOrder/pageList", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<MyPageInfo<BankOrderVo>> bankOrderPageList(@RequestBody BankOrderDto bankOrderDto);

    @RequestMapping(value = "/bankOrder/excelExport", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<List<BankOrderVo>> bankOrderExcelExport(@RequestBody BankOrderDto bankOrderDto);


}
