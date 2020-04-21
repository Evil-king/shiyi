package com.baibei.shiyi.product.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.product.dao.ProductSkuMapper;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductSkuVo;
import com.baibei.shiyi.product.model.ProductSku;
import com.baibei.shiyi.product.service.IProductSkuService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.bouncycastle.operator.SymmetricKeyUnwrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProductSku服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductSkuServiceImpl extends AbstractService<ProductSku> implements IProductSkuService {

    @Autowired
    private ProductSkuMapper productSkuMapper;



    @Override
    public ProductSku getBySkuId(Long skuId) {
        if (StringUtils.isEmpty(skuId)) {
            throw new ServiceException("规格id不能为空");
        }
        ProductSku productSku = new ProductSku();
        productSku.setId(skuId);
        ProductSku ps = productSkuMapper.selectByPrimaryKey(productSku);
        return ps;
    }

    @Override
    public void deleteSkuByProductId(Long productId) {
        Condition condition = new Condition(ProductSku.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productId",productId);
        productSkuMapper.deleteByCondition(condition);
    }

    @Override
    public void softDeleteByProductId(Long productId) {
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("删除sku失败，未指定商品");
        }
        productSkuMapper.softDelete(productId);
    }

    @Override
    public void deleteSku(Long productId, String skuNo) {
        Condition condition = new Condition(ProductSku.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(productId)) {
            criteria.andEqualTo("productId",productId);
        }
        if (!StringUtils.isEmpty(skuNo)) {
            criteria.andEqualTo("skuNo",skuNo);
        }
        productSkuMapper.deleteByCondition(condition);
    }

    @Override
    public ProductSku getBySkuNo(String skuNo) {
        if (StringUtils.isEmpty(skuNo)) {
            throw new ServiceException("非法查询，未指定规格编码");
        }
        Condition condition = new Condition(ProductSku.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("skuNo",skuNo);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<ProductSku> productSkuList = productSkuMapper.selectByCondition(condition);
        if (productSkuList.size()>1) {
            throw new ServiceException("非法查询");
        }
        return productSkuList.size()==0?null:productSkuList.get(0);
    }

    @Override
    public List<ProductSku> getSkuList(Long productId) {
        Condition condition = new Condition(ProductSku.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("productId",productId);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<ProductSku> productSkuList = productSkuMapper.selectByCondition(condition);
        return productSkuList;
    }

    @Override
    public List<AdmEditProductSkuVo> getSkuStockList(Long productId) {
        List<AdmEditProductSkuVo> admEditProductSkuVos = productSkuMapper.selectSkuStockList(productId);
        return admEditProductSkuVos;
    }


}
