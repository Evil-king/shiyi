package com.baibei.shiyi.product.dao;

import com.baibei.shiyi.common.core.mybatis.MyMapper;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.model.Group;

import java.util.List;

public interface GroupMapper extends MyMapper<Group> {
    List<GroupVo> myList(GroupDto groupDto);

}