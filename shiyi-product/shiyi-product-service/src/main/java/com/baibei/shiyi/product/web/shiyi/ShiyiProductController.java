package com.baibei.shiyi.product.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.product.feign.base.shiyi.IProductBase;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductSkuVo;
import com.baibei.shiyi.product.model.ProductShelf;
import com.baibei.shiyi.product.model.ProductSku;
import com.baibei.shiyi.product.service.IProductShelfService;
import com.baibei.shiyi.product.service.IProductSkuService;
import com.baibei.shiyi.product.service.IProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Longer
 * @date 2019/08/1
 */
@Controller
public class ShiyiProductController implements IProductBase {

    @Autowired
    private IProductShelfService productShelfService;
    @Autowired
    private IProductSkuService productSkuService;
    @Autowired
    private IProductStockService productStockService;

    @Override
    public ApiResult<BaseShelfVo> getBaseShelfProductInfo(@RequestBody @Validated ShelfRefDto shelfRefDto) {
        BaseShelfVo baseShelfProductInfo = productShelfService.getBaseShelfProductInfo(shelfRefDto);
        return ApiResult.success(baseShelfProductInfo);
    }

    @Override
    public ApiResult<List<BaseShelfVo>> getBatchShelfProductInfo(@RequestBody @Validated List<ShelfRefDto> shelfRefDtoList) {
        List<BaseShelfVo> baseShelfVoList = new ArrayList<>();
        for (ShelfRefDto shelfRefDto : shelfRefDtoList) {
            BaseShelfVo baseShelfProductInfo = productShelfService.getBaseShelfProductInfo(shelfRefDto);
            baseShelfVoList.add(baseShelfProductInfo);
        }
        return ApiResult.success(baseShelfVoList);
    }

    @Override
    public ApiResult<ProductSkuVo> getProductSkuBySkuId(@RequestParam("skuId") Long skuId) {
        ProductSku productSku = productSkuService.getBySkuId(skuId);
        ProductSkuVo productSkuVo = BeanUtil.copyProperties(productSku, ProductSkuVo.class);
        return ApiResult.success(productSkuVo);
    }

    @Override
    public ApiResult changeStock(@RequestBody @Validated  ChangeStockDto changeStockDto) {
        productStockService.changeStock(changeStockDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult batchChangeStock(@RequestBody @Validated List<ChangeStockDto> changeStockDtoList) {
        productStockService.batchChangeStock(changeStockDtoList);
        return ApiResult.success();
    }

    @Override
    public ApiResult<BaseShelfVo> getShelfInfo(@RequestBody @Validated ShelfRefDto shelfRefDto) {
        ProductShelf productShelf = productShelfService.getShelfById(shelfRefDto);
        BaseShelfVo baseShelfVo = new BaseShelfVo();
        baseShelfVo.setSpuNo(productShelf.getSpuNo());
        return ApiResult.success(baseShelfVo);
    }
}
