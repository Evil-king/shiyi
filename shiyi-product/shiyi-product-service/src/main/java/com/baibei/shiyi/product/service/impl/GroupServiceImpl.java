package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.product.dao.GroupMapper;
import com.baibei.shiyi.product.dao.ProductGroupRefMapper;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.product.model.ProductGroupRef;
import com.baibei.shiyi.product.service.IGroupService;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import com.baibei.shiyi.product.service.IProductShelfService;
import com.baibei.shiyi.product.service.IProductStockService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: Group服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl extends AbstractService<Group> implements IGroupService {

    @Autowired
    private GroupMapper proGroupMapper;

    @Autowired
    private IProductGroupRefService groupRefService;

    @Autowired
    private IProductShelfService productShelfService;

    @Autowired
    private IProductStockService productStockService;

    @Autowired
    private ProductGroupRefMapper productGroupRefMapper;

    @Override
    public MyPageInfo<GroupVo> pageList(GroupDto groupDto) {
        PageHelper.startPage(groupDto.getCurrentPage(), groupDto.getPageSize());
        List<GroupVo> list = proGroupMapper.myList(groupDto);
        list.stream().forEach(result -> {
            if (result.getGroupType().equals(Constants.GroupType.HOT)) {
                result.setProductCount(productStockService.countHotProduct().intValue());
            }
            if (result.getGroupType().equals(Constants.GroupType.NEW)) {
                result.setProductCount(productShelfService.countLatestProduct().intValue());
            }
        });
        MyPageInfo<GroupVo> myPageInfo = new MyPageInfo<>(list);
        return myPageInfo;
    }

    @Override
    public void save(GroupCurdDto groupDto) {
        Group group = BeanUtil.copyProperties(groupDto, Group.class);
        group.setId(IdWorker.getId());
        this.save(group);
        if (groupDto.getGroupType().equals(Constants.GroupType.COMMON)) {
            if (groupDto.getShelfIds() != null || !CollectionUtils.isEmpty(groupDto.getShelfIds())) {
                for (String productId : groupDto.getShelfIds()) {
                    this.groupRefService.save(this.toEntity(group.getId(), Long.valueOf(productId)));
                }
            }
        }
    }

    @Override
    public boolean checkTitleRepeat(GroupCurdDto groupCurdDto) {
        Condition condition = new Condition(Group.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("title", groupCurdDto.getTitle().replaceAll(" ","")).andEqualTo("flag", new Byte(Constants.Flag.VALID));
        if (groupCurdDto.getId() != null) {
            criteria.andNotEqualTo("id", groupCurdDto.getId());
        }
        List<Group> groupList = this.findByCondition(condition);
        if (groupList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int sumGroupProduct(Long groupId) {
        Condition condition = new Condition(ProductGroupRef.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("groupId",groupId);
        int count = productGroupRefMapper.selectCountByCondition(condition);
        return count;
    }

    @Override
    public void update(GroupCurdDto groupDto) {
        Group group = BeanUtil.copyProperties(groupDto, Group.class);
        group.setModifyTime(new Date());
        this.update(group);
        this.groupRefService.deleteByGroupId(groupDto.getId());
        if (groupDto.getGroupType().equals(Constants.GroupType.COMMON)) {
            if (groupDto.getShelfIds() != null || CollectionUtils.isEmpty(groupDto.getShelfIds())) {
                for (String productId : groupDto.getShelfIds()) {
                    this.groupRefService.save(this.toEntity(groupDto.getId(), Long.valueOf(productId)));
                }
            }
        }
    }

    @Override
    public void deleteProductGroupRef(ProductGroupDto productGroupDto) {
        groupRefService.deleteByProductGroup(productGroupDto);
    }

    @Override
    public Group findByTitle(String title) {
        return this.findBy("title", title);
    }

    @Override
    public GroupVo getById(GroupCurdDto groupCurdDto) {
        Group group = this.findById(groupCurdDto.getId());
        List<GroupProductVo> groupProductVos = new ArrayList<>();
        if (group != null) {
            if (group.getGroupType().equals(Constants.GroupType.COMMON)) {
                ProductGroupDto productGroupDto = new ProductGroupDto();
                productGroupDto.setGroupId(group.getId());
                groupProductVos = productShelfService.findByProductGroup(productGroupDto);
            }
            if (group.getGroupType().equals(Constants.GroupType.HOT)) {
                ProductGroupDto productGroupDto = new ProductGroupDto();
                productGroupDto.setCurrentPage(1);
                productGroupDto.setPageSize(50);
                groupProductVos = productShelfService.findLastSellCountByProduct(productGroupDto);
            }
            if (group.getGroupType().equals(Constants.GroupType.NEW)) {
                ProductGroupDto productGroupDto = new ProductGroupDto();
                productGroupDto.setCurrentPage(1);
                productGroupDto.setPageSize(50);
                groupProductVos = productShelfService.findLastShelfTimeByProduct(productGroupDto);
            }
        }
        GroupVo result = BeanUtil.copyProperties(group, GroupVo.class);
        result.setGroupProductVoList(groupProductVos);
        return result;
    }

    @Override
    public void deleteGroupById(Long groupId) {
        this.softDeleteById(groupId);
        this.groupRefService.deleteByGroupId(groupId);
    }

    @Override
    public List<GroupVo> findByList(GroupDto groupDto) {
        ProductGroupDto productGroupDto = new ProductGroupDto();
        productGroupDto.setCurrentPage(1);
        productGroupDto.setPageSize(50);
        List<GroupProductVo> lastSellCountByProduct = productShelfService.findLastSellCountByProduct(productGroupDto);

        List<GroupProductVo> lastShelfTimeByProduct = productShelfService.findLastShelfTimeByProduct(productGroupDto);

        List<GroupVo> productVoList = proGroupMapper.myList(groupDto);
        productVoList.stream().forEach(x -> {
            if (Constants.GroupType.HOT.equals(x.getGroupType())) {
                x.setProductCount(lastSellCountByProduct.size());
            }
            if (Constants.GroupType.NEW.equals(x.getGroupType())) {
                x.setProductCount(lastShelfTimeByProduct.size());
            }
        });
        return productVoList;
    }

    public ProductGroupRef toEntity(Long groupId, Long shelfId) {
        ProductGroupRef groupRef = new ProductGroupRef();
        groupRef.setId(IdWorker.getId());
        groupRef.setGroupId(groupId);
        groupRef.setShelfId(shelfId);
        return groupRef;
    }

    @Override
    public void batchDelete(Iterable<String> idSet) {
        super.batchDelete(idSet);
        for (String groupId : idSet) {
            groupRefService.deleteByGroupId(Long.valueOf(groupId));
        }
    }
}
