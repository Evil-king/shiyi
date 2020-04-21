package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoPageDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.dto.HoldPositionListDto;
import com.baibei.shiyi.trade.common.vo.HoldDetailListVo;
import com.baibei.shiyi.trade.common.vo.TotalMarketValueVo;
import com.baibei.shiyi.trade.service.IHoldPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/11/14 19:09
 * @description:
 */
@RestController
@RequestMapping("/auth/api/trade/holdPosition")
public class AuthApiHoldPositionController {

    @Autowired
    private IHoldPositionService holdPositionService;

   @RequestMapping("/getTotalMarketValue")
    public ApiResult<TotalMarketValueVo> getTotalMarketValue(@RequestBody @Validated  CustomerNoDto customerNoDto){
        return holdPositionService.getTotalMarketValue(customerNoDto);
    }

    @RequestMapping("/getPageList")
    public ApiResult<MyPageInfo<HoldDetailListVo>> getPageList(@RequestBody @Validated CustomerNoPageDto customerNoDto){
        return ApiResult.success(holdPositionService.getPageList(customerNoDto));
    }

    /**
     * PC端
     * @param holdPositionListDto
     * @return
     */
    @RequestMapping("/getPcPageList")
    public ApiResult<MyPageInfo<HoldDetailListVo>> getPcPageList(@RequestBody @Validated HoldPositionListDto holdPositionListDto){
        return ApiResult.success(holdPositionService.getPcPageList(holdPositionListDto));
    }
}
