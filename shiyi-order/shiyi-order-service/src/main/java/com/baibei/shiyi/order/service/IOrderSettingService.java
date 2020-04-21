package com.baibei.shiyi.order.service;

import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.model.OrderSetting;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderSetting服务接口
 */
public interface IOrderSettingService extends Service<OrderSetting> {

    OrderSetting findBy();

    void setting(AdminOrderSettingDto adminOrderSettingDto);
}
