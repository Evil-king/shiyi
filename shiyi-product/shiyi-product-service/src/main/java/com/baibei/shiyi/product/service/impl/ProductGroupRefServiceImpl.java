package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.dao.ProductGroupRefMapper;
import com.baibei.shiyi.product.feign.bean.vo.ShelfBeanVo;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.product.model.ProductGroupRef;
import com.baibei.shiyi.product.model.ShelfBeanRef;
import com.baibei.shiyi.product.service.IGroupService;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.product.service.IShelfBeanRefService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: productGroupRef服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductGroupRefServiceImpl extends AbstractService<ProductGroupRef> implements IProductGroupRefService {

    @Autowired
    private ProductGroupRefMapper productGroupRefMapper;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IShelfBeanRefService shelfBeanRefService;

    @Override
    public MyPageInfo<GroupProductVo> pageList(GroupRefDto groupRefDto) {
        //根据groupId查询组信息
        Group group = groupService.findById(groupRefDto.getGroupId());
        groupRefDto.setGroupType(group.getGroupType());
        PageHelper.startPage(groupRefDto.getCurrentPage(), groupRefDto.getPageSize());
        List<GroupProductVo> list = productGroupRefMapper.myList(groupRefDto);
        for (GroupProductVo groupProductVo : list) {
            List<ShelfBeanRef> shelfBeanList = shelfBeanRefService.getShelfBean(groupProductVo.getShelfId(), null);
            for (ShelfBeanRef shelfBeanRef : shelfBeanList) {
                ShelfBeanVo shelfBeanVo = new ShelfBeanVo();
                shelfBeanVo.setShelfId(shelfBeanRef.getShelfId());
                shelfBeanVo.setBeanType(shelfBeanRef.getBeanType());
                shelfBeanVo.setUnit(shelfBeanRef.getUnit());
                shelfBeanVo.setValue(shelfBeanRef.getValue());
                groupProductVo.getShelfBeanVoList().add(shelfBeanVo);
            }
        }
        MyPageInfo<GroupProductVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public void deleteByGroupId(Long groupId) {
        ProductGroupRef productGroupRef = new ProductGroupRef();
        productGroupRef.setGroupId(groupId);
        this.productGroupRefMapper.delete(productGroupRef);
    }

    @Override
    public void deleteByProductGroup(ProductGroupDto productGroupDto) {
        for (String shelfId : productGroupDto.getShelfIds()) {
            ProductGroupRef productGroupRef = new ProductGroupRef();
            productGroupRef.setGroupId(productGroupDto.getGroupId());
            productGroupRef.setShelfId(Long.valueOf(shelfId));
            this.productGroupRefMapper.delete(productGroupRef);
        }

    }

    @Override
    public int countProductGroup(ProductGroupRef productGroupRef) {
        return productGroupRefMapper.selectCount(productGroupRef);
    }


}
