package com.baibei.shiyi.order.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSettingVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderSettingFeign;
import com.baibei.shiyi.order.model.OrderSetting;
import com.baibei.shiyi.order.service.IOrderSettingService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orderSetting")
public class AdminOrderSettingFeignController implements IAdminOrderSettingFeign {


    private final IOrderSettingService orderSettingService;

    public AdminOrderSettingFeignController(IOrderSettingService orderSettingService) {
        this.orderSettingService = orderSettingService;
    }

    @Override
    public ApiResult setting(@RequestBody  AdminOrderSettingDto adminOrderSettingDto) {
        orderSettingService.setting(adminOrderSettingDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<AdminOrderSettingVo> findBySetting() {
        OrderSetting orderSetting = orderSettingService.findAll().get(0);
        AdminOrderSettingVo result = BeanUtil.copyProperties(orderSetting, AdminOrderSettingVo.class);
        return ApiResult.success(result);
    }
}
