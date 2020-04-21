package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.feign.bean.vo.BaseProductSkuVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Classname AdmProductController
 * @Description 后台管理商品相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/admin/product/product")
public class AdmProductController implements IAdmProductFeign {

    @Autowired
    private IProductService productService;

    @Override
    public ApiResult<MyPageInfo<AdmProductVo>> pageList(@Validated @RequestBody AdmProductDto admProductDto) {
        MyPageInfo<AdmProductVo> admProductVoMyPageInfo = productService.pageList(admProductDto);
        return ApiResult.success(admProductVoMyPageInfo);
    }

    @Override
    public ApiResult addOrEditProduct(@Validated @RequestBody AddProductDto addProductDto) {
        productService.addOrEditProduct(addProductDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<AdmEditProductVo> editProductPage(@Validated @RequestBody AdmEditProductPageDto admEditProductPageDto) {
        AdmEditProductVo admEditProductVo = productService.editProductPage(admEditProductPageDto.getProductId());
        return ApiResult.success(admEditProductVo);
    }

    @Override
    public ApiResult deleteProduct(@Validated @RequestBody AdmDeleteProductDto AdmDeleteProductDto) {
        return productService.softDeleteProduct(AdmDeleteProductDto.getProductId());
    }

    @Override
    public ApiResult batchDeleteProduct(@Validated @RequestBody List<AdmDeleteProductDto> admDeleteProductDtoList) {
        String allSuccessFlag="success";
        if (StringUtils.isEmpty(admDeleteProductDtoList)||admDeleteProductDtoList.size()==0) {
            return ApiResult.error("未选择商品");
        }
        for (AdmDeleteProductDto admDeleteProductDto : admDeleteProductDtoList) {
            ApiResult apiResult = productService.softDeleteProduct(admDeleteProductDto.getProductId());
            if (apiResult.getCode()!=200) {
                allSuccessFlag="fail";
            }
        }
        if(allSuccessFlag.equals("success")){
            return ApiResult.success();
        }else{
            return ApiResult.error("部分商品删除失败，商品已上架");
        }
    }

    @Override
    public ApiResult<List<BaseProductSkuVo>> getProductSkuList(@Validated @RequestBody SpuDto spuDto) {
        List<BaseProductSkuVo> productSkuListBySpuNoList = productService.getProductSkuListBySpuNo(spuDto.getSpuNo());
        return ApiResult.success(productSkuListBySpuNoList);
    }
}
