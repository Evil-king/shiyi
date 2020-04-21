package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.PropertyKeyDto;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import com.baibei.shiyi.product.model.PropertyKey;

import java.util.List;

public interface PropertyKeyMapper extends MyMapper<PropertyKey> {
    List<PropertyKeyVo> findByKeyDto(PropertyKeyDto propertyKeyDto);
}