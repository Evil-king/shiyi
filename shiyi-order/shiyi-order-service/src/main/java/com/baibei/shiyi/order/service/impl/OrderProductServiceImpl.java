package com.baibei.shiyi.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.ShelfTypeUtil;
import com.baibei.shiyi.order.common.dto.ProductDto;
import com.baibei.shiyi.order.common.vo.KeyValue;
import com.baibei.shiyi.order.dao.OrderProductMapper;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderProductVo;
import com.baibei.shiyi.order.model.OrderProduct;
import com.baibei.shiyi.order.service.IOrderProductService;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.ShelfBeanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderProduct服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderProductServiceImpl extends AbstractService<OrderProduct> implements IOrderProductService {

    @Autowired
    private OrderProductMapper tblOrdOrderProductMapper;

    @Override
    public void create(Long orderId, List<ProductDto> productList, List<BaseShelfVo> shelfVoList,String payWay) {
        for (BaseShelfVo shelfVo : shelfVoList) {
            for (ProductDto item : productList) {
                if (item.getShelfId().equals(shelfVo.getShelfId()) && item.getSkuId().equals(shelfVo.getSkuId())) {
                    OrderProduct orderProduct = new OrderProduct();
                    orderProduct.setId(IdWorker.getId());
                    orderProduct.setOrderId(orderId);
                    orderProduct.setShelfId(shelfVo.getShelfId());
                    orderProduct.setSkuId(shelfVo.getSkuId());
                    orderProduct.setProductImg(shelfVo.getProductImg());
                    orderProduct.setProductName(shelfVo.getProductName());
                    orderProduct.setShelfType(shelfVo.getShelfType());
                    orderProduct.setSkuProperty(shelfVo.getSkuProperty());
                    orderProduct.setAmount(shelfVo.getShelfPrice());
                    orderProduct.setQuantity(item.getNum().intValue());
                    orderProduct.setTotalAmount(shelfVo.getShelfPrice().multiply(item.getNum()));
                    if (Constants.ShelfType.SEND_INTEGRAL.equals(shelfVo.getShelfType())&&Constants.BeanType.MALLACCOUNT.equals(payWay)) {
                        orderProduct.setPayWay(payWay);
                    }else{
                        orderProduct.setPayWay(ShelfTypeUtil.getPayWay(shelfVo.getShelfType()));
                    }
                    orderProduct.setCreateTime(new Date());
                    orderProduct.setModifyTime(new Date());
                    // 赠送积分商品需要计算积分赠送数额
                    if (Constants.ShelfType.SEND_INTEGRAL.equals(shelfVo.getShelfType())) {
                        List<ShelfBeanVo> shelfBeanVoList = shelfVo.getShelfBeanVoList();
                        if (!CollectionUtils.isEmpty(shelfBeanVoList)) {
                            List<KeyValue> kvList = new ArrayList<>();
                            for (ShelfBeanVo shelfBeanVo : shelfBeanVoList) {
                                KeyValue kv = new KeyValue();
                                kv.setKey(shelfBeanVo.getBeanType());
                                if (Constants.Unit.PERCENT.equals(shelfBeanVo.getUnit())) {
                                    BigDecimal value = shelfVo.getShelfPrice().multiply(shelfBeanVo.getValue().divide(new BigDecimal(100)));
                                    kv.setValue(value.toPlainString());
                                }
                                if (Constants.Unit.BEAN.equals(shelfBeanVo.getUnit())) {
                                    kv.setValue(shelfBeanVo.getValue().toPlainString());
                                }
                                kvList.add(kv);
                            }
                            orderProduct.setSendIntegralJson(JSON.toJSONString(kvList));
                        }
                    }
                    save(orderProduct);
                }
            }
        }
    }


    @Override
    public List<OrderProduct> findByOrderId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId can not be null");
        }
        Condition condition = new Condition(OrderProduct.class);
        condition.setOrderByClause("amount asc");
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("orderId", orderId);
        return findByCondition(condition);
    }

    @Override
    public List<AdminOrderProductVo> findAdminByOrderId(Long orderId) {
        List<AdminOrderProductVo> adminOrderProductVoList = this.tblOrdOrderProductMapper.findAdminByOrderId(orderId);
        return adminOrderProductVoList;
    }


}
