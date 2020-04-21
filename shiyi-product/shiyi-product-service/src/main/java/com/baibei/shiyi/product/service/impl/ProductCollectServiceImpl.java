package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.bean.CustomerBaseAndPageDto;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.ProductCollectMapper;
import com.baibei.shiyi.product.feign.bean.dto.ProductCollectDto;
import com.baibei.shiyi.product.feign.bean.vo.ProductCollectVo;
import com.baibei.shiyi.product.feign.bean.vo.ShelfBeanVo;
import com.baibei.shiyi.product.model.ProductCollect;
import com.baibei.shiyi.product.model.ShelfBeanRef;
import com.baibei.shiyi.product.service.IProductCollectService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IShelfBeanRefService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author: uqing
 * @date: 2019/08/08 16:48:01
 * @description: ProductCollect服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductCollectServiceImpl extends AbstractService<ProductCollect> implements IProductCollectService {

    @Autowired
    private ProductCollectMapper tblProProductCollectMapper;
    @Autowired
    private IShelfBeanRefService shelfBeanRefService;


    @Override
    public MyPageInfo<ProductCollectVo> pageList(CustomerBaseAndPageDto customerBaseAndPageDto) {
        PageHelper.startPage(customerBaseAndPageDto.getCurrentPage(), customerBaseAndPageDto.getPageSize());
        List<ProductCollectVo> productCollectVoList = tblProProductCollectMapper.findByCustomerNo(customerBaseAndPageDto.getCustomerNo());
        for (ProductCollectVo productCollectVo : productCollectVoList) {
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(productCollectVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                productCollectVo.getShelfBeanVoList().add(shelfBeanVo);
            }
        }
        productCollectVoList.stream().forEach(result -> dataConversion(result));
        MyPageInfo<ProductCollectVo> result = new MyPageInfo<>(productCollectVoList);
        return result;
    }

    public void dataConversion(ProductCollectVo productCollectVo) {
        if (!StringUtils.isEmpty(productCollectVo.getSource())) {
            productCollectVo.setSourceText(Constants.SourceType.getMapTypeText().get(productCollectVo.getSource()));
        }
    }


    @Override
    public void save(ProductCollectDto productCollectDto) {
        Condition condition = new Condition(ProductCollect.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", productCollectDto.getCustomerNo());
        criteria.andIn("shelfId", productCollectDto.getShelfIds());
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        List<ProductCollect> productCollectList = this.findByCondition(condition);

        // stop 1过滤掉已经用户收藏的商品
        List<String> shelfIds = productCollectDto.getShelfIds().stream().filter(result -> {
                    long count = productCollectList.stream().filter(productCollect -> result.equals(productCollect.getShelfId().toString())).count();
                    if (count > 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
        ).collect(Collectors.toList());
        // stop2 获取用户已经收藏的商品设置最大限制
        Integer countCustomerByProduct = countCustomerByProduct(productCollectDto.getCustomerNo());
        if ((shelfIds.size() + countCustomerByProduct) > 100) {
            throw new ServiceException("收藏商品已经超过100个,请减少收藏商品数量");
        }
        // stop 3保存商品
        for (String shelfId : shelfIds) {
            ProductCollect productCollect = new ProductCollect();
            productCollect.setId(IdWorker.getId());
            productCollect.setCustomerNo(productCollectDto.getCustomerNo());
            productCollect.setShelfId(Long.valueOf(shelfId));
            productCollect.setFlag(new Byte(Constants.Flag.VALID));
            this.save(productCollect);
        }
    }

    @Override
    public void batchDeleteProduct(ProductCollectDto productGroupDto) {
        for (String shelfId : productGroupDto.getShelfIds()) {
            ProductCollect productCollect = new ProductCollect();
            productCollect.setShelfId(Long.valueOf(shelfId));
            productCollect.setCustomerNo(productGroupDto.getCustomerNo());
            tblProProductCollectMapper.delete(productCollect);
        }
    }

    @Override
    public Integer countCustomerByProduct(String customerNO) {
        Condition condition = new Condition(ProductCollect.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNO);
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        List<ProductCollect> productCollectList = this.findByCondition(condition);
        return productCollectList.size();
    }

    @Override
    public ProductCollect checkCollect(String customerNo, Long shelfId) {
        Condition condition = new Condition(ProductCollect.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(customerNo)) {
            criteria.andEqualTo("customerNo", customerNo);
        }
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId", shelfId);
        }
        criteria.andEqualTo("flag", 1);
        List<ProductCollect> productCollectList = tblProProductCollectMapper.selectByCondition(condition);
        if (productCollectList.size() > 1) {
            throw new ServiceException("收藏异常，系统错误");
        }
        ProductCollect productCollect = new ProductCollect();
        if (productCollectList.size() > 0) {
            productCollect = productCollectList.get(0);
        }
        return productCollect;
    }

}
