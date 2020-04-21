package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.base.IAdminTradeProductBase;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import com.baibei.shiyi.trade.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminTradeProductController implements IAdminTradeProductBase {

    @Autowired
    private IProductService productService;


    @Override
    public ApiResult<MyPageInfo<TradeProductListVo>> listPage(@RequestBody TradeProductDto tradeProductDto) {
        return ApiResult.success(productService.listPage(tradeProductDto));
    }

    @Override
    public ApiResult add(@RequestBody TradeProductAddDto tradeProductAddDto) {
        return productService.add(tradeProductAddDto);
    }

    @Override
    public ApiResult editOperator(@RequestBody TradeProductAddDto tradeProductAddDto) {
        return productService.editOperator(tradeProductAddDto);
    }

    @Override
    public ApiResult<TradeProductVo> look(@RequestBody TradeProductLookDto tradeProductLookDto) {
        return productService.look(tradeProductLookDto);
    }

    @Override
    public ApiResult modifyStatus() {
        return productService.modifyStatus();
    }

}
