package com.baibei.shiyi.trade.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.feign.bean.dto.ChangeHoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.dto.ProductDto;
import com.baibei.shiyi.trade.feign.bean.vo.ProductVo;
import com.baibei.shiyi.trade.feign.client.shiyi.TradeFeign;
import com.baibei.shiyi.trade.model.Product;
import com.baibei.shiyi.trade.service.IHoldDetailsService;
import com.baibei.shiyi.trade.service.IHoldPostionChangeService;
import com.baibei.shiyi.trade.service.IProductService;
import com.baibei.shiyi.trade.thread.CalculationCostThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: 会跳舞的机器人
 * @date: 2020/1/3 15:13
 * @description:
 */
@RestController
public class ShiyiTradeController implements TradeFeign {
    @Autowired
    private IProductService productService;
    @Autowired
    private IHoldPostionChangeService holdPostionChangeService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    /**
     * 异步计算持仓成本线程池
     */
    private ExecutorService costExecutorService = Executors.newFixedThreadPool(2);

    @Override
    public ApiResult<List<ProductVo>> productList(@RequestBody ProductDto dto) {
        List<Product> list = productService.listTradeingProduct(dto);
        return ApiResult.success(BeanUtil.copyProperties(list, ProductVo.class));
    }

    @Override
    public ApiResult changeHoldPosition(@RequestBody @Validated List<ChangeHoldPositionDto> changeHoldPositionDtoList) {
        List<ChangeHoldPositionBo> boList = BeanUtil.copyProperties(changeHoldPositionDtoList, ChangeHoldPositionBo.class);
        holdPostionChangeService.changeHoldPosition(boList);
        for (ChangeHoldPositionBo bo : boList) {
            if (Constants.Retype.IN.equals(bo.getReType())) {
                // 异步计算成本价
                costExecutorService.submit(new CalculationCostThread(bo.getCustomerNo(), bo.getProductTradeNo(), holdDetailsService));
            }
        }
        return ApiResult.success();
    }
}