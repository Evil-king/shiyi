package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import com.baibei.shiyi.product.feign.client.admin.AdminProductProTypeFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/6 16:28
 * @description:
 */
@RestController
@RequestMapping("/admin/product/proType")
public class ProductProTypeController {
    @Autowired
    private AdminProductProTypeFeign productProTypeFeign;

    /**
     * 后台类目列表
     * @param pageParam
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_CATEGORY_BACKGROUND'))")
    public ApiResult<MyPageInfo<ProTypeVo>> list(@Validated @RequestBody PageParam pageParam){
        return productProTypeFeign.list(pageParam);
    }

    /**
     * 添加类目
     * @param addProTypeDto
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.add('PRODUCT_CATEGORY_BACKGROUND'))")
    public ApiResult add(@Validated @RequestBody AddProTypeDto addProTypeDto){
        return productProTypeFeign.add(addProTypeDto);
    }

    /**
     * 修改类目
     * @param updateProTypeDto
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_CATEGORY_BACKGROUND'))")
    public ApiResult update(@Validated @RequestBody UpdateProTypeDto updateProTypeDto){
        return productProTypeFeign.update(updateProTypeDto);
    }

    /**
     * 删除
     * @param deleteProTypeDto
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    @NoRepeatSubmit
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_CATEGORY_BACKGROUND'))")
    public ApiResult delete(@Validated @RequestBody DeleteIdsDto deleteProTypeDto){
        return productProTypeFeign.delete(deleteProTypeDto);
    }

    /**
     * 获取所有后台类目列表
     * @return
     */
    @PostMapping("/getAll")
    @ResponseBody
    public ApiResult<List<ProTypeVo>> getAll(){
        return productProTypeFeign.getAll();
    }
}
