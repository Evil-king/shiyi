package com.baibei.shiyi.trade.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.trade.common.dto.TradeInfoDto;
import com.baibei.shiyi.trade.common.vo.PCTradeInfoVo;
import com.baibei.shiyi.trade.common.vo.TradeInfoVo;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.ProductVo;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.service.IProductService;
import com.baibei.shiyi.trade.service.ITradeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/2 10:31
 * @description:
 */
@RestController
@RequestMapping("/auth/api/trade")
public class ApiTradeInfoController {
    @Autowired
    private ITradeInfoService tradeInfoService;
    @Autowired
    private IProductService productService;


    /**
     * 获取挂买/挂卖页面信息
     *
     * @param dto
     * @return
     */
    @PostMapping("/listInfo")
    public ApiResult<TradeInfoVo> tradeInfo(@RequestBody @Validated TradeInfoDto dto) {
        return ApiResult.success(tradeInfoService.tradeInfo(dto));
    }

    /**
     * 获取挂买/挂卖页面信息，PC版本
     *
     * @param dto
     * @return
     */
    @PostMapping("/listInfoForPc")
    public ApiResult<PCTradeInfoVo> listInfoForPc(@RequestBody @Validated TradeInfoDto dto) {
        return ApiResult.success(tradeInfoService.pcTradeInfoVo(dto));
    }

    /**
     * 交易商品列表
     *
     * @param dto
     * @return
     */
    @PostMapping("/productList")
    public ApiResult<List<ProductVo>> productList(@RequestBody ProductDto dto) {
        List<Product> list = productService.listTradeingProduct(dto);
        return ApiResult.success(BeanUtil.copyProperties(list, ProductVo.class));
    }

}