package com.baibei.shiyi.product.service;

import com.baibei.shiyi.product.feign.bean.dto.AddCategoryProduct;
import com.baibei.shiyi.product.feign.bean.dto.DeleteOneCategoryProductDto;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.model.ShelfCategoryRef;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: Longer
 * @date: 2019/09/11 14:50:57
 * @description: ShelfCategoryRef服务接口
 */
public interface IShelfCategoryRefService extends Service<ShelfCategoryRef> {

    /**
     * 判断类目下是否存在商品
     *
     * @param categoryId
     * @return
     */
    boolean hasProductsInCategory(Long categoryId);

    /**
     * 清空指定类目下的所有商品
     *
     * @param categoryId
     */
    void clearCategroyProduct(Long categoryId);

    /**
     * 删除指定类目下的某个商品
     *
     * @param categoryId
     * @param shelfId
     */
    void deleteCategoryProduct(Long categoryId, Long shelfId);

    /**
     * 批量添加前台类目商品
     *
     * @param addCategoryProductList
     */
    void batchAddCategoryProduct(List<AddCategoryProduct> addCategoryProductList);

    /**
     * 查询商品的分类
     *
     * @param
     * @return
     */
    List<CategoryVo> findByCategory(Long shelfId);

    /**
     * 批量删除类目商品
     * @param deleteOneCategoryProductDtoList
     */
    void batchDeleteCategoryProduct(List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList);

    /**
     * 判断类目是否有商品。true=有；false=无
     * @return
     */
    boolean checkHasProduct(Long categoryId);
}
