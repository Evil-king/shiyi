package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.model.ProductShelfRef;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductShelfRef服务接口
*/
public interface IProductShelfRefService extends Service<ProductShelfRef> {

    void deleteProductShelf(Long shelfId,Long skuId);

    /**
     * 获取上架属性列表
     * @param shelfId
     * @param skuId
     * @return
     */
    List<ProductShelfRef> getShelfSkus(Long shelfId,Long skuId);
}
