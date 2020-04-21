package com.baibei.shiyi.user.feign.client.shiyi;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.hystrix.PABCustomerHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "${shiyi-user:shiyi-user}", path = "/shiyi/customer", fallbackFactory = PABCustomerHystrix.class)
public interface ICustomerFeign {

    /**
     * 修改用户信息
     *
     * @param pabCustomerVo
     * @return
     */
    @PostMapping(path = "/update")
    ApiResult update(@RequestBody PABCustomerVo pabCustomerVo);

    /**
     * 查询用户签约信息
     *
     * @param customerNoDto
     * @return
     */
    @PostMapping(path = "/findCustomerNo")
    ApiResult<PABCustomerVo> findCustomerNo(@RequestBody CustomerNoDto customerNoDto);

    /**
     * 获取用户实名信息
     *
     * @param customerBaseDto
     * @return
     */
    @PostMapping("/realnameInfo")
    ApiResult<RealnameInfoVo> realnameInfo(@RequestBody @Validated CustomerBaseDto customerBaseDto);

}
