package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddParameterDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.ParameterListDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateParameterDto;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.model.ParameterKey;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: ParameterKey服务接口
*/
public interface IParameterKeyService extends Service<ParameterKey> {
    /**
     * 根据后台类目id获取参数
     * @param id
     * @return
     */
    List<ParameterKey> findByTypeId(Long id);

    /**
     * 后台参数列表
     * @param parameterListDto
     * @return
     */
    MyPageInfo<ParameterListVo> parameterPageList(ParameterListDto parameterListDto);

    /**
     * 后台添加参数
     * @param addParameterDto
     * @return
     */
    ApiResult addParameter(AddParameterDto addParameterDto);

    /**
     * 后台修改参数
     * @param updateParameterDto
     * @return
     */
    ApiResult updateParameter(UpdateParameterDto updateParameterDto);

    /**
     * 后台删除参数（批量删除）
     * @param deleteIdsDto
     * @return
     */
    ApiResult deleteParameter(DeleteIdsDto deleteIdsDto);

    /**
     * 根据后台类目获取参数以及参数值
     * @param typeId
     * @return
     */
    List<ParameterListVo> list(Long typeId);

    /**
     * 更新编辑状态
     * @param paramId
     * @param editFlag 是否可编辑标识
     */
    void modifyEditFlag(Long paramId,String editFlag);
}
