package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.base.admin.IAdminProductProTypeBase;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.service.IProTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/6 9:41
 * @description:
 */
@RestController
public class AdminProTypeController implements IAdminProductProTypeBase {
    @Autowired
    private IProTypeService proTypeService;

    /**
     * 后台类目列表
     * @param pageParam
     * @return
     */
    @Override
    public ApiResult<MyPageInfo<ProTypeVo>> list(@Validated @RequestBody PageParam pageParam){
        return ApiResult.success(proTypeService.adminList(pageParam));
    }

    @Override
    public ApiResult<List<ProTypeVo>> getAll() {
        return ApiResult.success(proTypeService.findAllAndFlag());
    }

    /**
     * 添加类目
     * @param addProTypeDto
     * @return
     */
    @Override
    public ApiResult add(@Validated @RequestBody AddProTypeDto addProTypeDto){
        if(addProTypeDto.getTypeName().length()>10){
            return ApiResult.badParam("最多只能填写10个字");
        }
        return proTypeService.insertProType(addProTypeDto);
    }

    /**
     * 修改类目
     * @param updateProTypeDto
     * @return
     */
    @Override
    public ApiResult update(@Validated @RequestBody UpdateProTypeDto updateProTypeDto){
        if(updateProTypeDto.getTypeName().length()>10){
            return ApiResult.badParam("最多只能填写10个字");
        }
        return proTypeService.updateProType(updateProTypeDto);
    }

    /**
     * 删除或批量删除后台类目
     * @param deleteIdsDto
     * @return
     */
    @Override
    public ApiResult delete(@Validated @RequestBody DeleteIdsDto deleteIdsDto){
        return proTypeService.deleteProType(deleteIdsDto);
    }
}
