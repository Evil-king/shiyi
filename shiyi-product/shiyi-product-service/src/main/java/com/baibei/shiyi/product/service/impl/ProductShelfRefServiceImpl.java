package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.product.dao.ProductShelfRefMapper;
import com.baibei.shiyi.product.model.ProductShelfRef;
import com.baibei.shiyi.product.model.ShelfCategoryRef;
import com.baibei.shiyi.product.service.IProductShelfRefService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductShelfRef服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductShelfRefServiceImpl extends AbstractService<ProductShelfRef> implements IProductShelfRefService {

    @Autowired
    private ProductShelfRefMapper productShelfRefMapper;


    @Override
    public void deleteProductShelf(Long shelfId, Long skuId) {
        Condition condition = new Condition(ProductShelfRef.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId",shelfId);
        }
        if (!StringUtils.isEmpty(skuId)) {
            criteria.andEqualTo("skuId",skuId);
        }
        productShelfRefMapper.deleteByCondition(condition);
    }

    @Override
    public List<ProductShelfRef> getShelfSkus(Long shelfId, Long skuId) {
        Condition condition = new Condition(ProductShelfRef.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId",shelfId);
        }
        if (!StringUtils.isEmpty(skuId)) {
            criteria.andEqualTo("skuId",skuId);
        }
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<ProductShelfRef> productShelfRefList = productShelfRefMapper.selectByCondition(condition);
        return productShelfRefList;
    }
}
