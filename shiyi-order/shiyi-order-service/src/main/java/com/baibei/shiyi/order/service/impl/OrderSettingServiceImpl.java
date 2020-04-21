package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.order.dao.OrderSettingMapper;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderSettingDto;
import com.baibei.shiyi.order.model.OrderSetting;
import com.baibei.shiyi.order.service.IOrderSettingService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderSetting服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderSettingServiceImpl extends AbstractService<OrderSetting> implements IOrderSettingService {

    @Autowired
    private OrderSettingMapper tblOrdSettingMapper;

    @Override
    public OrderSetting findBy() {
        Condition condition = new Condition(OrderSetting.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        List<OrderSetting> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     *
     * @param adminOrderSettingDto
     */
    @Override
    public void setting(AdminOrderSettingDto adminOrderSettingDto) {
        List<OrderSetting> orderSettingList = this.findAll();
        if (orderSettingList.size() > 1) {
            throw new ServiceException("当前订单设置数据错误，请联系管理员修改");
        }
        OrderSetting orderSetting = BeanUtil.copyProperties(adminOrderSettingDto, OrderSetting.class);
        if (orderSettingList.size() == 0) {
            orderSetting.setFlag(new Byte(Constants.Flag.VALID));
            orderSetting.setId(IdWorker.getId());
            this.save(orderSetting);
            return;
        }
        orderSetting.setId(orderSettingList.get(0).getId());
        orderSetting.setModifyTime(new Date());
        this.update(orderSetting);
    }
}
