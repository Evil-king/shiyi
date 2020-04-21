package com.baibei.shiyi.product.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.order.feign.base.dto.ShiyiCartDto;
import com.baibei.shiyi.order.feign.client.IShiyiCartFeign;
import com.baibei.shiyi.product.feign.bean.dto.CheckCollectDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductCollectDto;
import com.baibei.shiyi.product.feign.bean.vo.ProductCollectVo;
import com.baibei.shiyi.product.model.ProductCollect;
import com.baibei.shiyi.product.service.IProductCollectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 商品收藏
 */
@RestController
@RequestMapping("/auth/api/product/collect")
@Slf4j
public class ApiProductCollectController {


    @Autowired
    private IShiyiCartFeign cartFeign;

    private final IProductCollectService productCollectService;

    public ApiProductCollectController(IProductCollectService productCollectService) {
        this.productCollectService = productCollectService;
    }

    @PostMapping(path = "/pageList")
    public ApiResult<MyPageInfo<ProductCollectVo>> pageList(@Valid @RequestBody CustomerBaseAndPageDto customerBaseAndPageDto) {
        return ApiResult.success(productCollectService.pageList(customerBaseAndPageDto));
    }

    /**
     * 收藏商品成功,
     * 移除购物车的商品失败
     * 判断这商品是否已经被移除购物车,如果已经被移除,就跳过
     * 1、保存收藏商品成功，却没有移除购物车,会有问题的，就会还在购物车当中，也会在收藏商品
     * 2、保存收藏商品成功，移除购物车成功
     *
     * @return
     */
    @PostMapping(path = "/save")
    public ApiResult<ProductCollectVo> save(@Valid @RequestBody ProductCollectDto productCollectDto) {
        if (productCollectDto.getShelfIds().isEmpty()) {
            return ApiResult.error("收藏商品不能为空");
        }
        productCollectService.save(productCollectDto);
        if (!StringUtils.isEmpty(productCollectDto.getOrderCartIds())) {
            ShiyiCartDto shiyiCartDto = new ShiyiCartDto();
            shiyiCartDto.setCartIds(productCollectDto.getOrderCartIds());
            ApiResult result = cartFeign.deleteCardProduct(shiyiCartDto);
            return result;
        }
        return ApiResult.success();
    }

    /**
     * 批量移除收藏
     *
     * @return
     */
    @PostMapping(path = "/deleteByIds")
    public ApiResult<ProductCollectVo> deleteByIds(@RequestBody ProductCollectDto productCollectDto) {
        if (productCollectDto.getShelfIds().isEmpty()) {
            ApiResult.error("商品收藏不能为空");
        }
        productCollectService.batchDeleteProduct(productCollectDto);
        return ApiResult.success();
    }

    /**
     * 判断用户是否已收藏该商品
     *
     * @return
     */
    @PostMapping(path = "/checkCollect")
    public ApiResult checkCollect(@RequestBody @Validated CheckCollectDto checkCollectDto) {
        ProductCollect productCollect = productCollectService.checkCollect(checkCollectDto.getCustomerNo(), checkCollectDto.getShelfId());
        return ApiResult.success(productCollect);
    }


}
