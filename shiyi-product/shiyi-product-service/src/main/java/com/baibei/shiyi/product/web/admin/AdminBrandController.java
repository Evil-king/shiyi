package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.base.admin.IAdminBrandBase;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/12 10:06
 * @description:
 */
@RestController
public class AdminBrandController implements IAdminBrandBase {
    @Autowired
    private IBrandService brandService;
    @Override
    public ApiResult<MyPageInfo<BrandListVo>> brandList(@Validated @RequestBody BrandListDto brandListDto) {
        return ApiResult.success(brandService.brandList(brandListDto));
    }

    @Override
    public ApiResult addOrUpdateBrand(@Validated @RequestBody AddOrUpdateBrandDto addOrUpdateBrandDto) {
        return brandService.addOrUpdateBrand(addOrUpdateBrandDto);
    }

    @Override
    public ApiResult deleteBrand(@Validated @RequestBody DeleteIdsDto deleteIdsDto) {
        return brandService.deleteBrand(deleteIdsDto);
    }

    @Override
    public ApiResult<List<BrandListVo>> getBrandList() {
        return ApiResult.success(brandService.getBrandList());
    }
}
