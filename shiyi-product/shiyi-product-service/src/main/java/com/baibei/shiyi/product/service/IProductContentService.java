package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.model.ProductContent;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductContent服务接口
*/
public interface IProductContentService extends Service<ProductContent> {

    /**
     * 获取商品图文详情
     */
    ProductContent getContent(ShelfRefDto shelfRefDto);

    void modifyContent(ProductContent productContent);
}
