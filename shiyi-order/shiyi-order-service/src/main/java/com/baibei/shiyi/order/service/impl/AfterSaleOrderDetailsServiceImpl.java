package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.order.dao.AfterSaleOrderDetailsMapper;
import com.baibei.shiyi.order.model.AfterSaleOrderDetails;
import com.baibei.shiyi.order.service.IAfterSaleOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/10/15 10:16:48
 * @description: AfterSaleOrderDetails服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AfterSaleOrderDetailsServiceImpl extends AbstractService<AfterSaleOrderDetails> implements IAfterSaleOrderDetailsService {

    @Autowired
    private AfterSaleOrderDetailsMapper aftersaleOrderDetailsMapper;

    @Override
    public AfterSaleOrderDetails createAfterSaleOrderDetails(String orderItemNo, String serverNo) {
        AfterSaleOrderDetails afterSaleOrderDetails = new AfterSaleOrderDetails();
        afterSaleOrderDetails.setServerNo(serverNo);
        afterSaleOrderDetails.setOrderItemNo(orderItemNo);
        afterSaleOrderDetails.setFlag((byte) 1);
        afterSaleOrderDetails.setCreateTime(new Date());
        afterSaleOrderDetails.setModifyTime(new Date());
        return afterSaleOrderDetails;
    }

    @Override
    public int insertData(AfterSaleOrderDetails afterSaleOrderDetails) {
        return aftersaleOrderDetailsMapper.insert(afterSaleOrderDetails);
    }

    @Override
    public int updateAfterSaleOrderDetails(AfterSaleOrderDetails afterSaleOrderDetails) {
        afterSaleOrderDetails.setModifyTime(new Date());
        Condition condition = new Condition(AfterSaleOrderDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", afterSaleOrderDetails.getOrderItemNo());
        return aftersaleOrderDetailsMapper.updateByConditionSelective(afterSaleOrderDetails, condition);
    }

    @Override
    public AfterSaleOrderDetails selectByOrderItemNo(String orderItemNo) {
        Condition condition = new Condition(AfterSaleOrderDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", orderItemNo);
        List<AfterSaleOrderDetails> afterSaleOrderDetails = aftersaleOrderDetailsMapper.selectByCondition(condition);
        if (!CollectionUtils.isEmpty(afterSaleOrderDetails)) {
            return afterSaleOrderDetails.get(0);
        }
        return null;
    }

    @Override
    public int updateAfterSaleOrderDetails(String serverNo, BigDecimal confirmAmount, String sendBackAddress, String remark) {
        AfterSaleOrderDetails afterSaleOrderDetails = new AfterSaleOrderDetails();
        afterSaleOrderDetails.setServerNo(serverNo);
        afterSaleOrderDetails.setRefuseMoney(confirmAmount);
        afterSaleOrderDetails.setSendbackAddress(sendBackAddress);
        afterSaleOrderDetails.setRemark(remark);
        afterSaleOrderDetails.setModifyTime(new Date());
        return aftersaleOrderDetailsMapper.updateByPrimaryKeySelective(afterSaleOrderDetails);
    }

    @Override
    public AfterSaleOrderDetails selectByServerNo(String serverNo) {
        return aftersaleOrderDetailsMapper.selectByParams(serverNo);
    }

    @Override
    public int updateByServerNoAndLogistics(String serverNo, String logisticsName, String logisticsNo) {
        AfterSaleOrderDetails afterSaleOrderDetails = new AfterSaleOrderDetails();
        afterSaleOrderDetails.setServerNo(serverNo);
        afterSaleOrderDetails.setLogisticsName(logisticsName);
        afterSaleOrderDetails.setLogisticsNo(logisticsNo);
        afterSaleOrderDetails.setModifyTime(new Date());
        return aftersaleOrderDetailsMapper.updateByPrimaryKeySelective(afterSaleOrderDetails);
    }

    @Override
    public int updateByOrderItemNoAndSendLogistics(String orderItemNo, String sendLogisticsNo, String sendLogisticsName) {
        return aftersaleOrderDetailsMapper.updateByOrderItemNoAndSendLogistics(orderItemNo,sendLogisticsNo,sendLogisticsName);
    }
}
