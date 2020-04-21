package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.product.dao.ProductImgMapper;
import com.baibei.shiyi.product.model.ProductImg;
import com.baibei.shiyi.product.service.IProductImgService;
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
* @description: ProductImg服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductImgServiceImpl extends AbstractService<ProductImg> implements IProductImgService {

    @Autowired
    private ProductImgMapper productImgMapper;

    @Override
    public List<ProductImg> getProductImgs(Long productId) {
        Condition condition = new Condition(ProductImg.class);
        condition.setOrderByClause("seq asc");
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(productId)) {
            criteria.andEqualTo("productId",productId);
        }
        List<ProductImg> productImgs = productImgMapper.selectByCondition(condition);
        criteria.andEqualTo("flag",1);
        return productImgs;
    }

    @Override
    public void deleteImgsByProductId(Long productId) {
        Condition condition = new Condition(ProductImg.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productId",productId);
        productImgMapper.deleteByCondition(condition);
    }

    @Override
    public void softDeleteImgsByProductId(Long productId) {
        productImgMapper.softDeleteByProductId(productId);
    }
}
