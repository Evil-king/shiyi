package com.baibei.shiyi.product.service;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ProductShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.*;
import com.baibei.shiyi.product.model.ProductShelf;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: ProductShelf服务接口
 */
public interface IProductShelfService extends Service<ProductShelf> {

    /**
     * 获取最小单位小的商品信息（根据shelfNo 和 skuNo获取指定一条商品信息）
     */
    BaseShelfVo getBaseShelfProductInfo(ShelfRefDto shelfRefDto);

    /**
     * 根据分组Id查询分组商品
     *
     * @return
     */
    MyPageInfo<GroupProductVo> findProductGroupList(ProductGroupDto productGroupDto);

    /**
     * 查询分组的商品
     *
     * @param productGroupDto
     * @return
     */
    List<GroupProductVo> findByProductGroup(ProductGroupDto productGroupDto);

    /**
     * 查询分组没有的商品
     *
     * @param productGroupDto
     * @return
     */
    MyPageInfo<GroupProductVo> findNoExistProductGroup(ProductGroupDto productGroupDto);

    /**
     * 获取上架商品的信息。包括多维的sku
     *
     * @param shelfRefDto
     * @return
     */
    BuyProductVo getBuyProductInfo(ShelfRefDto shelfRefDto);


    /**
     * 商品详情，基础信息获取
     *
     * @param shelfRefDto
     * @return
     */
    BaseIndexProductVo getIndexProductView(ShelfRefDto shelfRefDto);

    ProductShelf getShelfById(ShelfRefDto shelfRefDto);


    /**
     * 最新商品数量
     *
     * @return
     */
    Long countLatestProduct();

    /**
     * 查询最新上架时间的商品
     */
    List<GroupProductVo> findLastShelfTimeByProduct(ProductGroupDto productGroupDto);

    /**
     * 查询最热商品
     */
    List<GroupProductVo> findLastSellCountByProduct(ProductGroupDto productGroupDto);

    /**
     * 上架商品
     *
     * @param admProductShelfDto
     */
    void shelfProduct(AdmProductShelfDto admProductShelfDto);

    /**
     * 后台编辑上架商品（数据回显）
     *
     * @param shelfId
     * @return
     */
    AdmEditProductShelfVo admEditShelfProductPage(Long shelfId);

    /**
     * 跳转到新增上架商品列表
     * @return
     */
    AdmToAddProductShelfVo admToAddProductShelfPage();


    /**
     * 获取上架商品属性列表信息
     *
     * @param shelfId
     * @return
     */
    List<AdmProductShelfSkuVo> getAdmShelfSkuInfoList(Long shelfId);

    /**
     * 获取上架商品列表
     *
     * @param admSelectProductShelfDto
     * @return
     */
    MyPageInfo<BaseShelfVo> getProductShelfListForPage(AdmSelectProductShelfDto admSelectProductShelfDto);

    /**
     * 获取上架商品
     *
     * @return
     */
    List<ProductShelfVo> findProductShelf();

    /**
     * 搜索商品
     *
     * @return
     */
    MyPageInfo<ProductShelfVo> searchProduct(ProductShelfDto productShelfDto);

    /**
     * 根据商品名获取
     * @param productShelfDto
     * @return
     */
    List<String> searchShelfName(ProductShelfDto productShelfDto);

    /**
     * 上下架商品
     * @param shelfProductDto
     * @return
     */
    ApiResult shelf(ShelfProductDto shelfProductDto);

    /**
     * 批量上下架商品
     * @param shelfProductDtos
     * @return
     */
    ApiResult batchShelf(List<ShelfProductDto> shelfProductDtos);

    /**
     * 批量软删除
     * @param batchDeleteShelfDtos
     * @return
     */
    ApiResult batchSoftDelete(List<BatchDeleteShelfDto> batchDeleteShelfDtos);

    /**
     * 判断商品是否已上架.true=已上架，false=非上架
     * @param productId
     * @return
     */
    boolean checkShelf(Long productId);
}
