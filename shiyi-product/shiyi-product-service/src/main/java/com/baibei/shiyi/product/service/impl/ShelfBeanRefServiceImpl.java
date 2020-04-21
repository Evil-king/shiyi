package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.product.dao.ShelfBeanRefMapper;
import com.baibei.shiyi.product.model.ShelfBeanRef;
import com.baibei.shiyi.product.service.IShelfBeanRefService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: Longer
* @date: 2019/10/30 18:15:41
* @description: ShelfBeanRef服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ShelfBeanRefServiceImpl extends AbstractService<ShelfBeanRef> implements IShelfBeanRefService {

    @Autowired
    private ShelfBeanRefMapper shelfBeanRefMapper;

    @Override
    public void deleteShelfBean(Long shelfId, String beanType) {
        Condition condition = new Condition(ShelfBeanRef.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId",shelfId);
        }
        if (!StringUtils.isEmpty(beanType)) {
            criteria.andEqualTo("beanType",beanType);
        }
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        shelfBeanRefMapper.deleteByCondition(condition);
    }

    @Override
    public List<ShelfBeanRef> getShelfBean(Long shelfId, String beanType) {
        Condition condition = new Condition(ShelfBeanRef.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(shelfId)) {
            criteria.andEqualTo("shelfId",shelfId);
        }
        if (!StringUtils.isEmpty(beanType)) {
            criteria.andEqualTo("beanType",beanType);
        }
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        List<ShelfBeanRef> shelfBeanRefList = shelfBeanRefMapper.selectByCondition(condition);
        return shelfBeanRefList;
    }
}
