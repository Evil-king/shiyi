package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.model.ProductGroupRef;

import java.util.List;

public interface ProductGroupRefMapper extends MyMapper<ProductGroupRef> {
    /**
     * 获取所有类型组的商品(新品上架，热门推荐，普通组)
     * @param groupRefDto
     * @return
     */
    List<GroupProductVo> myList(GroupRefDto groupRefDto);

}