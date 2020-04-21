package com.baibei.shiyi.order.service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.common.vo.ApiAfterSalePageListVo;
import com.baibei.shiyi.order.model.AfterSaleGoods;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.order.model.OrderItem;


/**
* @author: wenqing
* @date: 2019/11/12 20:57:45
* @description: AfterSaleGoods服务接口
*/
public interface IAfterSaleGoodsService extends Service<AfterSaleGoods> {

    AfterSaleGoods operatorAfterSaleGoods(OrderItem byOrderItemNo);

    int insertData(AfterSaleGoods afterSaleGoods);

    AfterSaleGoods selectByOrderItemNo(String orderItemNo);

    int updateByOrderItemNo(String orderItemNo,String type);

    MyPageInfo<ApiAfterSalePageListVo> selectByOrderItemNoTypeIsNotNull(String orderItemNo, int currentPage, int pageSize);

    AfterSaleGoods selectByOrderItemNoAndType(String orderItemNo, String type, String sign);

}
