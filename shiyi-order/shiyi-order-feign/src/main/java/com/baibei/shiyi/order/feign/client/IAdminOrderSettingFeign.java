package com.baibei.shiyi.order.feign.client;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSettingVo;
import com.baibei.shiyi.order.feign.client.hystrix.AdminOrderSettingHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${shiyi-order:shiyi-order}", path = "/admin/orderSetting",fallbackFactory = AdminOrderSettingHystrix.class)
public interface IAdminOrderSettingFeign {

    /**
     * 订单设置
     */
    @PostMapping(path = "/setting")
    ApiResult setting(@RequestBody AdminOrderSettingDto adminOrderSettingDto);


    /**
     * 查询订单设置
     *
     * @return
     */
    @PostMapping(path = "/findBySetting")
    ApiResult<AdminOrderSettingVo> findBySetting();
}
