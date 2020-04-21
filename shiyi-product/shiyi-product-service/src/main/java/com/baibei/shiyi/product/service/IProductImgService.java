package com.baibei.shiyi.product.service;
import com.baibei.shiyi.product.model.ProductImg;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductImg服务接口
*/
public interface IProductImgService extends Service<ProductImg> {

    List<ProductImg> getProductImgs(Long productId);

    /**
     * 根据商品id删除图片
     */
    void deleteImgsByProductId(Long productId);

    void softDeleteImgsByProductId(Long productId);
}
