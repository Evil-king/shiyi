package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.ParameterListDto;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.model.ParameterKey;

import java.util.List;

public interface ParameterKeyMapper extends MyMapper<ParameterKey> {
    List<ParameterListVo> findByListDto(ParameterListDto parameterListDto);
}