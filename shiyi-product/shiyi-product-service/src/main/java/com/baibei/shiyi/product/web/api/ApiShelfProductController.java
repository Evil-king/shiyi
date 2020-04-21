package com.baibei.shiyi.product.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.ProductShelfDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseIndexProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BuyProductVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductParamVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductShelfVo;
import com.baibei.shiyi.product.model.ProductContent;
import com.baibei.shiyi.product.service.IProductContentService;
import com.baibei.shiyi.product.service.IProductService;
import com.baibei.shiyi.product.service.IProductShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Classname ApiShelfProductController
 * @Description 上架商品相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/api/product/productShelf")
public class ApiShelfProductController {
    @Autowired
    private IProductShelfService productShelfService;
    @Autowired
    private IProductContentService productContentService;
    @Autowired
    private IProductService productService;

    /**
     * 购买商品的sku页面信息
     *
     * @param shelfRefDto
     * @return
     */
    @PostMapping("/buyProducts")
    public ApiResult<BuyProductVo> buyProducts(@Validated @RequestBody ShelfRefDto shelfRefDto) {
        return ApiResult.success(productShelfService.getBuyProductInfo(shelfRefDto));
    }

    /**
     * 商品详情--获取商品图片和商品基础信息
     *
     * @param shelfRefDto
     * @return
     */
    @PostMapping("/indexProductInfo")
    public ApiResult<BaseIndexProductVo> indexProductInfo(@Validated @RequestBody ShelfRefDto shelfRefDto) {
        return ApiResult.success(productShelfService.getIndexProductView(shelfRefDto));
    }

    /**
     * 商品详情--商品图文详情
     *
     * @param shelfRefDto
     * @return
     */
    @PostMapping("/content")
    public ApiResult<ProductContent> productContent(@Validated @RequestBody ShelfRefDto shelfRefDto) {
        return ApiResult.success(productContentService.getContent(shelfRefDto));
    }

    /**
     * 获取商品参数
     *
     * @param shelfRefDto
     * @return
     */
    @PostMapping("/parameter")
    public ApiResult<List<ProductParamVo>> productParameter(@Validated @RequestBody ShelfRefDto shelfRefDto) {
        return ApiResult.success(productService.getParams(shelfRefDto));
    }

    /**
     * 商品搜索
     *
     * @param productShelfDto
     * @return
     */
    @PostMapping("/searchProduct")
    public ApiResult<MyPageInfo<ProductShelfVo>> productSearch(@Validated @RequestBody ProductShelfDto productShelfDto) {
        return ApiResult.success(productShelfService.searchProduct(productShelfDto));
    }

    /**
     * 搜索商品名
     *
     * @param productShelfDto
     * @return
     */
    @PostMapping("/shelfNameSearch")
    public ApiResult<List<String>> shelfNameSearch(@Validated @RequestBody ProductShelfDto productShelfDto) {
        return ApiResult.success(productShelfService.searchShelfName(productShelfDto));
    }


}
