package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductAddDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductDto;
import com.baibei.shiyi.trade.feign.bean.dto.TradeProductLookDto;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TradeProductVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTradeProductFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/trade/product")
public class TradeProductController {

    @Autowired
    private IAdminTradeProductFeign adminTradeProductFeign;


    @RequestMapping("/listPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('TRANSACTION_PRODUCT_LIST'))")
    public ApiResult<MyPageInfo<TradeProductListVo>> listPage(@RequestBody TradeProductDto tradeProductDto){
        return adminTradeProductFeign.listPage(tradeProductDto);
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('TRANSACTION_PRODUCT_LIST'))")
    public ApiResult add(@RequestBody TradeProductAddDto tradeProductAddDto){
        tradeProductAddDto.setCreator(SecurityUtils.getUsername());
        return adminTradeProductFeign.add(tradeProductAddDto);
    }

    @RequestMapping("/editOperator")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('TRANSACTION_PRODUCT_LIST'))")
    public ApiResult editOperator(@RequestBody TradeProductAddDto tradeProductAddDto){
        tradeProductAddDto.setCreator(SecurityUtils.getUsername());
        tradeProductAddDto.setModifier(SecurityUtils.getUsername());
        return adminTradeProductFeign.editOperator(tradeProductAddDto);
    }

    @RequestMapping("/look")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('TRANSACTION_PRODUCT_LIST'))")
    public ApiResult<TradeProductVo> look(@RequestBody TradeProductLookDto tradeProductLookDto){
        return adminTradeProductFeign.look(tradeProductLookDto);
    }
}
