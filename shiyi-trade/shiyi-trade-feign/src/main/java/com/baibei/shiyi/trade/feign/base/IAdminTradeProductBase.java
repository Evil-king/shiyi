package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import org.springframework.web.bind.annotation.RequestMapping;


public interface IAdminTradeProductBase {

    /**
     * 交易商品列表页
     * @param tradeProductDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/product/pageList")
    ApiResult<MyPageInfo<TradeProductListVo>> listPage(TradeProductDto tradeProductDto);

    /**
     * 交易商品新增
     * @param tradeProductAddDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/product/add")
    ApiResult add(TradeProductAddDto tradeProductAddDto);

    /**
     * 交易编辑商品
     * @param tradeProductAddDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/product/editOperator")
    ApiResult editOperator(TradeProductAddDto tradeProductAddDto);

    /**
     * 查看
     * @param tradeProductLookDto
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/product/look")
    ApiResult<TradeProductVo> look(TradeProductLookDto tradeProductLookDto);

    /**
     * 定时更细交易商品状态
     * @return
     */
    @RequestMapping("/shiyi/admin/trade/product/modifyStatus")
    ApiResult modifyStatus();
}
