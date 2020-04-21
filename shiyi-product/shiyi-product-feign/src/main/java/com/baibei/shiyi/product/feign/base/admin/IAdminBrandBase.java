package com.baibei.shiyi.product.feign.base.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.ParameterListDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.feign.bean.vo.ParameterListVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/12 9:52
 * @description:
 */
public interface IAdminBrandBase {
    /**
     * 品牌列表
     * @param brandListDto
     * @return
     */
    @PostMapping("/admin/product/brand/brandList")
    @ResponseBody
    ApiResult<MyPageInfo<BrandListVo>> brandList(@Validated @RequestBody BrandListDto brandListDto);

    /**
     * 新增或编辑品牌
     * @param addOrUpdateBrandDto
     * @return
     */
    @PostMapping("/admin/product/brand/addOrUpdateBrand")
    @ResponseBody
    ApiResult addOrUpdateBrand(@Validated @RequestBody AddOrUpdateBrandDto addOrUpdateBrandDto);

    /**
     * 删除
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/admin/product/brand/deleteBrand")
    @ResponseBody
    ApiResult deleteBrand(@Validated @RequestBody DeleteIdsDto deleteIdsDto);


    /**
     * 获取品牌列表
     * @param
     * @return
     */
    @PostMapping("/admin/product/brand/getBrandList")
    @ResponseBody
    ApiResult<List<BrandListVo>> getBrandList();
}
