package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.feign.client.admin.AdminParameterFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/11 14:57
 * @description:
 */
@RestController
@RequestMapping("/admin/product/parameter")
public class ParameterController {
    @Autowired
    private AdminParameterFeign adminParameterFeign;
    /**
     * 参数列表
     * @param parameterListDto
     * @return
     */
    @PostMapping("/parameterList")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.find('COMMON_PARAM'))")
    ApiResult<MyPageInfo<ParameterListVo>> parameterPageList(@Validated @RequestBody ParameterListDto parameterListDto){
        return adminParameterFeign.parameterPageList(parameterListDto);
    }

    /**
     * 新增参数
     * @param addParameterDto
     * @return
     */
    @PostMapping("/addParameter")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.add('COMMON_PARAM'))")
    ApiResult addParameter(@Validated @RequestBody AddParameterDto addParameterDto){
        return adminParameterFeign.addParameter(addParameterDto);
    }

    /**
     * 修改参数
     * @param updateParameterDto
     * @return
     */
    @PostMapping("/updateParameter")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('COMMON_PARAM'))")
    ApiResult updateParameter(@Validated @RequestBody UpdateParameterDto updateParameterDto){
        return adminParameterFeign.updateParameter(updateParameterDto);
    }

    /**
     * 删除
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/deleteParameter")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('COMMON_PARAM'))")
    ApiResult deleteParameter(@Validated @RequestBody DeleteIdsDto deleteIdsDto){
        return adminParameterFeign.deleteParameter(deleteIdsDto);
    }

    /**
     * 根据后台类目id获取所有参数
     * @param proTypeIdDto
     * @return
     */
    @PostMapping("/getParameterList")
    @ResponseBody
    ApiResult<List<ParameterListVo>> getParameterList(@Validated @RequestBody ProTypeIdDto proTypeIdDto){
        return adminParameterFeign.getParameterList(proTypeIdDto);
    }
}
