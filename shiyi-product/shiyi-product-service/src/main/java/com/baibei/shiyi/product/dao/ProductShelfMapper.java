package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.ProductShelfDto;
import com.baibei.shiyi.product.feign.bean.vo.ProductShelfVo;
import com.baibei.shiyi.product.feign.bean.dto.AdmSelectProductShelfDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductShelfSkuVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseIndexProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.model.ProductShelf;

import java.util.List;

public interface ProductShelfMapper extends MyMapper<ProductShelf> {
    List<BaseShelfVo> selectBaseShelfProductInfo(ShelfRefDto shelfRefDto);

    List<GroupProductVo> findGroupProduct(ProductGroupDto productGroupDto);

    List<GroupProductVo> findNoExistProductGroup(ProductGroupDto productGroupDto);

    BaseIndexProductVo selectBaseIndexProductInfo(ShelfRefDto shelfRefDto);

    /**
     * 最热商品的数量
     *
     * @return
     */
    Long countLatestProduct();

    /**
     * 查询最新上架商品
     *
     * @param productGroupDto
     * @return
     */
    List<GroupProductVo> findLastByShelfTime(ProductGroupDto productGroupDto);

    /**
     * 查询最热销的商品
     *
     * @param productGroupDto
     * @return
     */
    List<GroupProductVo> findLastSellCountByProduct(ProductGroupDto productGroupDto);

    List<AdmProductShelfSkuVo> selectAdmShelfSkuInfoList(Long shelfId);

    List<BaseShelfVo> selectProductShelfList(AdmSelectProductShelfDto admSelectProductShelfDto);

    /**
     * 查询上架的商品
     *
     * @return
     */
    List<ProductShelfVo> findProductShelf(ProductShelfDto productShelfDto);

    List<ProductShelf> findByShelfName(ProductShelfDto productShelfDto);

    int updateById(ProductShelf productShelf);
}