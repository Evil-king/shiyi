package com.baibei.shiyi.product.service;

import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.model.ProductGroupRef;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: productGroupRef服务接口
 */
public interface IProductGroupRefService extends Service<ProductGroupRef> {
    /**
     * 获取组商品
     *
     * @param groupRefDto
     * @return
     */
    MyPageInfo<GroupProductVo> pageList(GroupRefDto groupRefDto);

    /**
     * 删除分组信息
     *
     * @param groupId
     */
    void deleteByGroupId(Long groupId);

    /**
     * 删除分组某商品信息
     *
     * @param productGroupDto
     */
    void deleteByProductGroup(ProductGroupDto productGroupDto);

    /**
     * 获取分组的商品数量
     *
     * @param productGroupRef
     * @return
     */
    int countProductGroup(ProductGroupRef productGroupRef);


}
