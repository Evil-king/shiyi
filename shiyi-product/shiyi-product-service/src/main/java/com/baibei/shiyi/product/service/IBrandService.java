package com.baibei.shiyi.product.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.model.Brand;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: Longer
* @date: 2019/07/30 10:12:45
* @description: Brand服务接口
*/
public interface IBrandService extends Service<Brand> {

    MyPageInfo<BrandListVo> brandList(BrandListDto brandListDto);

    ApiResult addOrUpdateBrand(AddOrUpdateBrandDto addOrUpdateBrandDto);

    ApiResult deleteBrand(DeleteIdsDto deleteIdsDto);

    List<BrandListVo> getBrandList();
}
