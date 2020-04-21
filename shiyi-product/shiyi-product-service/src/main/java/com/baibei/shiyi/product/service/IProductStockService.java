package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.product.feign.bean.dto.ChangeStockDto;
import com.baibei.shiyi.product.feign.bean.dto.InitStockDto;
import com.baibei.shiyi.product.feign.bean.message.ChangeSellCountMessage;
import com.baibei.shiyi.product.model.ProductStock;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductStock服务接口
*/
public interface IProductStockService extends Service<ProductStock> {

    /**
     *  更改库存
     * @param changeStockDto
     */
    ApiResult changeStock(ChangeStockDto changeStockDto);

    /**
     * 批量修改库存
     * @param changeStockDtoList
     * @return
     */
    ApiResult batchChangeStock(List<ChangeStockDto> changeStockDtoList);

    /**
     * 初始化库存
     * @param initStockDto
     * @return
     */
    ApiResult initStock(InitStockDto initStockDto);

    /**
     * 修改商品销量
     * @param changeSellCountMessage
     * @return
     */
    ApiResult changeSellCount(ChangeSellCountMessage changeSellCountMessage);

    /**
     * 批量修改商品销量
     * @param changeSellCountMessageList
     * @return
     */
    ApiResult batchChangeSellCount(List<ChangeSellCountMessage> changeSellCountMessageList);

    /**
     * 查看库存
     * @param productStock
     * @return
     */
    ProductStock getStock(ProductStock productStock);


    /**
     * 最热商品数量
     */
    Long countHotProduct();

    /**
     * 根据商品id软删除
     * @param productId
     */
    void softDeleteByProductId(Long productId);

}
