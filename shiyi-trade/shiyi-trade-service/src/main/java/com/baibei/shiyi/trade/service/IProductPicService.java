package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.trade.common.dto.TradeProductPicDto;
import com.baibei.shiyi.trade.common.vo.TradeProductSlideListVo;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.model.ProductPic;

import java.util.List;


/**
* @author: wenqing
* @date: 2019/12/20 16:06:04
* @description: ProductPic服务接口
*/
public interface IProductPicService extends Service<ProductPic> {

    ApiResult createObj(TradeProductAddDto tradeProductAddDto);

    List<ProductPic> selectByTradeNo(String productTradeNo);

    ApiResult<TradeProductSlideListVo> getTradeProductPic(TradeProductPicDto productPicDto);

    ApiResult updateByTradeProductNo(TradeProductAddDto tradeProductAddDto);
}
