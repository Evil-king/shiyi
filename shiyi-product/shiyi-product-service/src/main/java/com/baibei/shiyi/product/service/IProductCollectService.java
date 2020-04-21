package com.baibei.shiyi.product.service;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.ProductCollectDto;
import com.baibei.shiyi.product.feign.bean.vo.ProductCollectVo;
import com.baibei.shiyi.product.model.ProductCollect;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/08/08 16:48:01
 * @description: ProductCollect服务接口
 */
public interface IProductCollectService extends Service<ProductCollect> {

    MyPageInfo<ProductCollectVo> pageList(CustomerBaseAndPageDto customerBaseAndPageDto);

    void save(ProductCollectDto productCollectDto);

    void batchDeleteProduct(ProductCollectDto productGroupDto);

    /**
     * 查询用户收藏商品的数量
     *
     * @param customerNO
     * @return
     */
    Integer countCustomerByProduct(String customerNO);

    /**
     * 判断用户是否收藏该商品
     *
     * @param customerNo
     * @param shelfId
     * @return
     */
    ProductCollect checkCollect(String customerNo, Long shelfId);


}
