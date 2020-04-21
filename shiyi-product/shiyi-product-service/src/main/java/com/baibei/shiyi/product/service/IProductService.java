package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.model.Product;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Product服务接口
*/
public interface IProductService extends Service<Product> {

    /**
     * 获取商品参数
     * @param shelfRefDto
     * @return
     */
    List<ProductParamVo> getParams(ShelfRefDto shelfRefDto);

    /**
     * 获取商品列表
     */
    MyPageInfo<AdmProductVo> pageList(AdmProductDto admProductDto);

    /**
     * 新增商品
     * @param addProductDto
     * @return
     */
    void addProduct(AddProductDto addProductDto);

    /**
     * 编辑商品
     * @param addProductDto
     * @return
     */
    void editProduct(AddProductDto addProductDto);

    void addOrEditProduct(AddProductDto addProductDto);

    /**
     * 根据后台类目id获取商品
     * @param typeId
     * @return
     */
    List<Product> findByTypeId(Long typeId);

    Product findBySpuNo(String spuNo);

    /**
     * 编辑商品页面
     * @param productId
     * @return
     */
    AdmEditProductVo editProductPage(Long productId);

    /**
     * 删除商品（软删除）
     * @param productId
     */
    ApiResult softDeleteProduct(Long productId);

    /**
     * 批量软删除
     * @param admDeleteProductDto
     */
    void batchSoftDeleteProduct(List<AdmDeleteProductDto> admDeleteProductDto);

    /**
     * 根据货号获取商品sku
     * @param spuNo
     * @return
     */
    List<BaseProductSkuVo> getProductSkuListBySpuNo(String spuNo);

}
