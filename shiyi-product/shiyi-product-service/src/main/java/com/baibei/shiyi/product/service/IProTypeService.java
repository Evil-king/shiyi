package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.model.ProType;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ProType服务接口
*/
public interface IProTypeService extends Service<ProType> {

    MyPageInfo<ProTypeVo> adminList(PageParam pageParam);

    /**
     * 添加类目
     * @param addProTypeDto
     * @return
     */
    ApiResult insertProType(AddProTypeDto addProTypeDto);

    /**
     * 修改类目名称
     * @param updateProTypeDto
     * @return
     */
    ApiResult updateProType(UpdateProTypeDto updateProTypeDto);

    /**
     * 删除类目（软删除）
     * @param deleteProTypeDto
     * @return
     */
    ApiResult deleteProType(DeleteIdsDto deleteProTypeDto);

    /**
     *通过id获取后台类目
     * @param typeId
     * @return
     */
    List<ProType> findByTypeId(Long typeId);

    /**
     * 通过名称获取后台类目(模糊查询)
     * @param typeName
     * @return
     */
    List<ProType> findByTypeName(String typeName);

    /**
     * 获取所有后台类目（flag为1）
     * @return
     */
    List<ProTypeVo> findAllAndFlag();
}
