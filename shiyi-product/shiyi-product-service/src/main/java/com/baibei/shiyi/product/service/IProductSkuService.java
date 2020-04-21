package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductSkuVo;
import com.baibei.shiyi.product.model.ProductSku;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductSku服务接口
*/
public interface IProductSkuService extends Service<ProductSku> {
    /**
     * 根据skuNo获取sku信息
     */
    ProductSku getBySkuId(Long skuId);

    void deleteSkuByProductId(Long productId);

    void softDeleteByProductId(Long productId);

    void deleteSku(Long productId,String skuNo);


    ProductSku getBySkuNo(String skuNo);

    /**
     * 获取商品sku集合
     * @param productId
     * @return
     */
    List<ProductSku> getSkuList(Long productId);

    List<AdmEditProductSkuVo> getSkuStockList(Long productId);
}
