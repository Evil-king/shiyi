package com.baibei.shiyi.admin.modules.order;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderSettingVo;
import com.baibei.shiyi.order.feign.client.IAdminOrderSettingFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orderSetting")
public class OrderSettingController {

    @Autowired
    private IAdminOrderSettingFeign adminOrderSetting;

    @PostMapping("/setting")
    public ApiResult setting(@RequestBody AdminOrderSettingDto orderSettingDto) {
        return adminOrderSetting.setting(orderSettingDto);
    }

    @PostMapping("/findBySetting")
    public ApiResult<AdminOrderSettingVo> findBySetting() {
        return adminOrderSetting.findBySetting();
    }

}
