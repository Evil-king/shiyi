package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.CategoryMapper;
import com.baibei.shiyi.product.dao.ShelfCategoryRefMapper;
import com.baibei.shiyi.product.feign.bean.dto.AddCategoryProduct;
import com.baibei.shiyi.product.feign.bean.dto.DeleteOneCategoryProductDto;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.model.Category;
import com.baibei.shiyi.product.model.ShelfCategoryRef;
import com.baibei.shiyi.product.service.IShelfCategoryRefService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/09/11 14:50:57
 * @description: ShelfCategoryRef服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ShelfCategoryRefServiceImpl extends AbstractService<ShelfCategoryRef> implements IShelfCategoryRefService {

    @Autowired
    private ShelfCategoryRefMapper shelfCategroyRefMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public boolean hasProductsInCategory(Long categoryId) {
        Condition condition = new Condition(ShelfCategoryRef.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("categoryId", categoryId);
        int count = shelfCategroyRefMapper.selectCountByCondition(condition);
        return count > 0 ? true : false;
    }

    @Override
    public void clearCategroyProduct(Long categoryId) {
        if (StringUtils.isEmpty(categoryId)) {
            throw new ServiceException("清空失败，未指定类目id");
        }
        Condition condition = new Condition(ShelfCategoryRef.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("categoryId", categoryId);
        shelfCategroyRefMapper.deleteByCondition(condition);
    }

    @Override
    public void deleteCategoryProduct(Long categoryId, Long shelfId) {
        Condition condition = new Condition(ShelfCategoryRef.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(categoryId)) {
            criteria.andEqualTo("categoryId", categoryId);
        }
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId", shelfId);
        }
        shelfCategroyRefMapper.deleteByCondition(condition);
    }

    @Override
    public void batchAddCategoryProduct(List<AddCategoryProduct> addCategoryProductList) {
        if (StringUtils.isEmpty(addCategoryProductList) || addCategoryProductList.size() == 0) {
            throw new ServiceException("未选择商品");
        }
        Category category = categoryMapper.selectByPrimaryKey(addCategoryProductList.get(0).getCategoryId());
        if (!Constants.CategoryEnd.ISEND.equals(category.getEnd().toString())) {
            throw new ServiceException("末级类目下才能挂载商品");
        }
        Date currentDate = new Date();
        for (AddCategoryProduct addCategoryProduct : addCategoryProductList) {
            if (!StringUtils.isEmpty(addCategoryProduct.getCategoryId()) && !StringUtils.isEmpty(addCategoryProduct.getShelfId())) {
                ShelfCategoryRef shelfCategoryRef = new ShelfCategoryRef();
                shelfCategoryRef.setId(IdWorker.getId());
                shelfCategoryRef.setCategoryId(addCategoryProduct.getCategoryId());
                shelfCategoryRef.setShelfId(addCategoryProduct.getShelfId());
                shelfCategoryRef.setCreateTime(currentDate);
                shelfCategoryRef.setModifyTime(currentDate);
                shelfCategoryRef.setFlag(new Byte(Constants.Flag.VALID));
                shelfCategroyRefMapper.insertSelective(shelfCategoryRef);
            }
        }
    }

    @Override
    public List<CategoryVo> findByCategory(Long shelfId) {
        return shelfCategroyRefMapper.findByCategory(shelfId);
    }

    @Override
    public void batchDeleteCategoryProduct(List<DeleteOneCategoryProductDto> deleteOneCategoryProductDtoList) {
        if (StringUtils.isEmpty(deleteOneCategoryProductDtoList)||deleteOneCategoryProductDtoList.size()==0) {
            throw new ServiceException("未指定删除的商品");
        }
        for (DeleteOneCategoryProductDto deleteOneCategoryProductDto : deleteOneCategoryProductDtoList) {
            if (StringUtils.isEmpty(deleteOneCategoryProductDto.getCategoryId())||StringUtils.isEmpty(deleteOneCategoryProductDto.getShelfId())) {
                throw new ServiceException("参数异常");
            }
            this.deleteCategoryProduct(deleteOneCategoryProductDto.getCategoryId(),deleteOneCategoryProductDto.getShelfId());
        }
    }

    @Override
    public boolean checkHasProduct(Long categoryId) {
        Condition condition = new  Condition(ShelfCategoryRef.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        int i = shelfCategroyRefMapper.selectCountByCondition(condition);
        return i>0?true:false;
    }
}
