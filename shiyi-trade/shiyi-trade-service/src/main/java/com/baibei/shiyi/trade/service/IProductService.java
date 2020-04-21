package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.vo.TradeIndexVo;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import com.baibei.shiyi.trade.model.Product;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 10:36:53
 * @description: Product服务接口
 */
public interface IProductService extends Service<Product> {

    /**
     * 查询当前时间有效的交易商品
     *
     * @param productTradeNo
     * @return
     */
    Product findEffective(String productTradeNo);

    /**
     * 判断交易商品是否在上市中
     *
     * @param productTradeNo
     * @return true=上市中，false=未上市
     */
    boolean isTrading(String productTradeNo);


    /**
     * 交易商品列表数据
     *
     * @param tradeProductDto
     * @return
     */
    MyPageInfo<TradeProductListVo> listPage(TradeProductDto tradeProductDto);

    /**
     * 新增交易商品
     *
     * @param tradeProductAddDto
     * @return
     */
    ApiResult add(TradeProductAddDto tradeProductAddDto);

    /**
     * 编辑
     *
     * @param tradeProductAddDto
     * @return
     */
    ApiResult editOperator(TradeProductAddDto tradeProductAddDto);

    /**
     * 查看数据
     *
     * @param tradeProductLookDto
     * @return
     */
    ApiResult<TradeProductVo> look(TradeProductLookDto tradeProductLookDto);

    /**
     * 定时扫面状态为wait的商品信息
     *
     * @return
     */
    ApiResult modifyStatus();

    /**
     * 查询当前正在上市交易的商品
     *
     * @param dto
     * @return
     */
    List<Product> listTradeingProduct(ProductDto dto);

    /**
     * 首页行情数据
     * @return
     */
    ApiResult<List<TradeIndexVo>> getIndex();
}
