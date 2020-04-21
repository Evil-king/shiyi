package com.baibei.shiyi.order.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.order.common.dto.ProductDto;
import com.baibei.shiyi.order.feign.base.vo.AdminOrderProductVo;
import com.baibei.shiyi.order.model.OrderProduct;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/31 18:16:01
 * @description: OrderProduct服务接口
 */
public interface IOrderProductService extends Service<OrderProduct> {

    void create(Long orderId, List<ProductDto> productList, List<BaseShelfVo> shelfVoList,String payWay);

    List<OrderProduct> findByOrderId(Long orderId);

    List<AdminOrderProductVo> findAdminByOrderId(Long orderId);
}
