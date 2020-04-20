package com.baibei.shiyi.cash.feign.client.shiyi;

import com.baibei.shiyi.cash.feign.base.dto.SignInBackDto;
import com.baibei.shiyi.cash.feign.base.vo.SignInBackVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${shiyi-cash:shiyi-cash}", path = "/shiyi/cash")
public interface IShiyiSigningInBackFeign {

    /**
     * 签到
     *
     * @param
     * @return
     */
    @PostMapping(path = "/signIn")
    ApiResult<SignInBackVo> signIn();

    /**
     * 签退
     *
     * @param
     * @return
     */
    @PostMapping(path = "/signBack")
    ApiResult<SignInBackVo> signBack();

    /**
     * 今天是否签到和签退
     *
     * @param status
     * @return
     */
    @PostMapping(path = "/isToDaySignInBack")
    ApiResult<Boolean> isToDaySignInBack(@RequestParam("status") String status);

    /**
     * 最后系统的状态
     *
     * @return
     */
    @PostMapping(path = "/lastSignStatus")
    ApiResult<SignInBackDto> lastSignStatus();
}
