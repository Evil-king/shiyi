package com.baibei.shiyi.order.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.order.common.vo.ApiAfterSalePageListVo;
import com.baibei.shiyi.order.dao.AfterSaleGoodsMapper;
import com.baibei.shiyi.order.model.AfterSaleGoods;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.service.IAfterSaleGoodsService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: wenqing
 * @date: 2019/10/15 10:16:48
 * @description: AfterSaleGoods服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AfterSaleGoodsServiceImpl extends AbstractService<AfterSaleGoods> implements IAfterSaleGoodsService {

    @Autowired
    private AfterSaleGoodsMapper aftersaleGoodsMapper;

    @Override
    public AfterSaleGoods operatorAfterSaleGoods(OrderItem byOrderItemNo) {
        AfterSaleGoods afterSaleGoods = new AfterSaleGoods();
        afterSaleGoods.setId(IdWorker.getId());
        afterSaleGoods.setImage(byOrderItemNo.getProductImg());
        afterSaleGoods.setName(byOrderItemNo.getProductName());
        afterSaleGoods.setAmount(byOrderItemNo.getAmount());
        afterSaleGoods.setSkuproperty(byOrderItemNo.getSkuProperty());
        afterSaleGoods.setShelfType(byOrderItemNo.getShelfType());
        afterSaleGoods.setQuantity(byOrderItemNo.getQuantity());
        afterSaleGoods.setOrderItemNo(byOrderItemNo.getOrderItemNo());
        afterSaleGoods.setFlag((byte) 1);
        afterSaleGoods.setCreateTime(new Date());
        afterSaleGoods.setModifyTime(new Date());
        return afterSaleGoods;
    }

    @Override
    public int insertData(AfterSaleGoods afterSaleGoods) {
        return aftersaleGoodsMapper.insert(afterSaleGoods);
    }

    @Override
    public AfterSaleGoods selectByOrderItemNo(String orderItemNo) {
        Condition condition = new Condition(AfterSaleGoods.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", orderItemNo);
        List<AfterSaleGoods> afterSaleGoods = aftersaleGoodsMapper.selectByCondition(condition);
        if (!afterSaleGoods.isEmpty()) {
            return afterSaleGoods.get(0);
        }
        return null;
    }

    @Override
    public int updateByOrderItemNo(String orderItemNo, String type) {
        return aftersaleGoodsMapper.updateTypeByOrderItemNo(orderItemNo, type);
    }

    @Override
    public MyPageInfo<ApiAfterSalePageListVo> selectByOrderItemNoTypeIsNotNull(String customerNo, int currentPage, int pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<ApiAfterSalePageListVo> listVos = aftersaleGoodsMapper.selectByOrderItemNoTypeIsNotNull(customerNo);
        MyPageInfo<ApiAfterSalePageListVo> pageInfo = new MyPageInfo<>(listVos);
        return pageInfo;
    }

    @Override
    public AfterSaleGoods selectByOrderItemNoAndType(String orderItemNo, String type, String signer) {
        Condition condition = new Condition(AfterSaleGoods.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderItemNo", orderItemNo);
        if("details".equals(signer)){
            criteria.andEqualTo("type", type);
        }
        List<AfterSaleGoods> afterSaleGoods = aftersaleGoodsMapper.selectByCondition(condition);
        if (!afterSaleGoods.isEmpty()) {
            return afterSaleGoods.get(0);
        }
        return null;
    }

}
