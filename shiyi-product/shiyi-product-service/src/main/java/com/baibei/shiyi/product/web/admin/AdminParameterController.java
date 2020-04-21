package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.base.admin.IAdminParameterBase;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import com.baibei.shiyi.product.service.IParameterKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/11 10:18
 * @description:
 */
@RestController
public class AdminParameterController implements IAdminParameterBase {
    @Autowired
    private IParameterKeyService parameterKeyService;
    @Override
    public ApiResult<MyPageInfo<ParameterListVo>> parameterPageList(@Validated @RequestBody ParameterListDto parameterListDto) {
        return ApiResult.success(parameterKeyService.parameterPageList(parameterListDto));
    }

    @Override
    public ApiResult addParameter(@Validated @RequestBody AddParameterDto addParameterDto) {
        return parameterKeyService.addParameter(addParameterDto);
    }

    @Override
    public ApiResult updateParameter(@Validated @RequestBody UpdateParameterDto updateParameterDto) {
        return parameterKeyService.updateParameter(updateParameterDto);
    }

    @Override
    public ApiResult deleteParameter(@Validated @RequestBody DeleteIdsDto deleteIdsDto) {
        return parameterKeyService.deleteParameter(deleteIdsDto);
    }

    @Override
    public ApiResult<List<ParameterListVo>> getParameterList(@Validated @RequestBody ProTypeIdDto proTypeIdDto) {
        return ApiResult.success(parameterKeyService.list(proTypeIdDto.getTypeId()));
    }

}
