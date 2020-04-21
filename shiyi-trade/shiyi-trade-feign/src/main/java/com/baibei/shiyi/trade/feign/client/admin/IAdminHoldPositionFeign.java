package com.baibei.shiyi.trade.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.feign.client.hystrix.AdminHoldPositionHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${shiyi-trade:shiyi-trade}", path = "/shiyi/admin/hold/position", fallbackFactory = AdminHoldPositionHystrix.class)
public interface IAdminHoldPositionFeign {

    @PostMapping("/pageList")
    ApiResult<MyPageInfo<HoldPositionVo>> pageList(@Validated @RequestBody HoldPositionDto holdPositionDto);

    @PostMapping(path = "exportList")
    ApiResult<List<HoldPositionVo>> exportList(@RequestBody HoldPositionDto holdPositionDto);
}
