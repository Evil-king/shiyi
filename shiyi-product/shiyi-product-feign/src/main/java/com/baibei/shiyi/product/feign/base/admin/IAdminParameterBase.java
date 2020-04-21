package com.baibei.shiyi.product.feign.base.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.feign.bean.vo.PropertyKeyVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/11 9:58
 * @description:
 */
public interface IAdminParameterBase {
    /**
     * 参数列表
     * @param parameterListDto
     * @return
     */
    @PostMapping("/admin/product/parameter/parameterList")
    @ResponseBody
    ApiResult<MyPageInfo<ParameterListVo>> parameterPageList(@Validated @RequestBody ParameterListDto parameterListDto);

    /**
     * 新增参数
     * @param addParameterDto
     * @return
     */
    @PostMapping("/admin/product/parameter/addParameter")
    @ResponseBody
    ApiResult addParameter(@Validated @RequestBody AddParameterDto addParameterDto);

    /**
     * 修改参数
     * @param updateParameterDto
     * @return
     */
    @PostMapping("/admin/product/parameter/updateParameter")
    @ResponseBody
    ApiResult updateParameter(@Validated @RequestBody UpdateParameterDto updateParameterDto);

    /**
     * 删除
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/admin/product/parameter/deleteParameter")
    @ResponseBody
    ApiResult deleteParameter(@Validated @RequestBody DeleteIdsDto deleteIdsDto);
    /**
     * 根据后台类目id获取所有参数
     * @param proTypeIdDto
     * @return
     */
    @PostMapping("/admin/product/parameter/getParameterList")
    @ResponseBody
    ApiResult<List<ParameterListVo>> getParameterList(ProTypeIdDto proTypeIdDto);
}
