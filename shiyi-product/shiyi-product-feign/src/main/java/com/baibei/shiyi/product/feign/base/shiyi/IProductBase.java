package com.baibei.shiyi.product.feign.base.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.ProductSkuVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Longer
 * @date 2019/08/1
 */
public interface IProductBase {


    /**
     * 获取最小单位小的商品信息（根据shelfNo 和 skuNo获取指定一条商品信息）
     * @param shelfRefDto
     * @return
     */
    @RequestMapping(value = "/shiyi/product/baseShelfProductInfo", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<BaseShelfVo> getBaseShelfProductInfo(@RequestBody @Validated ShelfRefDto shelfRefDto);

    /**
     * 获取最小单位小的商品信息集合（根据shelfNo 和 skuNo获取指定商品信息）
     * @param shelfRefDtoList
     * @return
     */
    @RequestMapping(value = "/shiyi/product/getBatchShelfProductInfo", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<List<BaseShelfVo>> getBatchShelfProductInfo(@RequestBody List<ShelfRefDto> shelfRefDtoList);

    /**
     * 根据skuNo获取sku信息
     */
    @RequestMapping(value = "/shiyi/product/skuInfos", method = RequestMethod.GET)
    @ResponseBody
    ApiResult<ProductSkuVo> getProductSkuBySkuId(@RequestParam("skuId") Long skuId);

    /**
     * 更改库存
     * @param changeStockDto
     * @return
     */
    @PostMapping("/shiyi/product/changeStock")
    @ResponseBody
    ApiResult changeStock(ChangeStockDto changeStockDto);

    /**
     * 批量更改库存
     * @param changeStockDtoList
     * @return
     */
    @PostMapping("/shiyi/product/batchChangeStock")
    @ResponseBody
    ApiResult batchChangeStock(List<ChangeStockDto> changeStockDtoList);

    /**
     * 获取上架商品对应的商品货号
     * @param shelfRefDto
     * @return
     */
    @RequestMapping(value = "/shiyi/product/getShelfInfo", method = RequestMethod.POST)
    @ResponseBody
    ApiResult<BaseShelfVo> getShelfInfo(@RequestBody @Validated ShelfRefDto shelfRefDto);
}
