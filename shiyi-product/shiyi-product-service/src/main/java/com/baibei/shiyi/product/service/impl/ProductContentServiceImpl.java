package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.product.dao.ProductContentMapper;
import com.baibei.shiyi.product.feign.bean.dto.ShelfRefDto;
import com.baibei.shiyi.product.model.ProductContent;
import com.baibei.shiyi.product.model.ProductShelf;
import com.baibei.shiyi.product.service.IProductContentService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IProductShelfService;
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
* @description: ProductContent服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductContentServiceImpl extends AbstractService<ProductContent> implements IProductContentService {

    @Autowired
    private ProductContentMapper productContentMapper;
    @Autowired
    private IProductShelfService productShelfService;

    @Override
    public ProductContent getContent(ShelfRefDto shelfRefDto) {
        ProductShelf shelf = productShelfService.getShelfById(shelfRefDto);
        if(StringUtils.isEmpty(shelf)){
            throw new ServiceException("获取不到该上架商品信息");
        }
        if (StringUtils.isEmpty(shelf.getProductId())) {
            throw new ServiceException("获取商品图文详情失败，获取不到商品id");
        }
        Condition condition = new Condition(ProductContent.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productId",shelf.getProductId());
        List<ProductContent> productContentList = productContentMapper.selectByCondition(condition);
        if(!StringUtils.isEmpty(productContentList)&&productContentList.size()>1){
            throw new ServiceException("should select one but more");
        }
        return productContentList.size()>0?productContentList.get(0):null;
    }

    @Override
    public void modifyContent(ProductContent productContent) {
        Condition condition = new Condition(ProductContent.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(productContent.getProductId())) {
            criteria.andEqualTo("productId",productContent.getProductId());
        }
        if (!StringUtils.isEmpty(productContent.getId())) {
            criteria.andEqualTo("id",productContent.getId());
        }
        productContentMapper.updateByConditionSelective(productContent,condition);
    }
}
