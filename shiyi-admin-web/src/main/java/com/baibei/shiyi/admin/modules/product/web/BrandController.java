package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.admin.modules.product.ProductDemo;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.AddOrUpdateBrandDto;
import com.baibei.shiyi.product.feign.bean.dto.BrandListDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.vo.BrandListVo;
import com.baibei.shiyi.product.feign.client.admin.AdminBrandFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/12 10:35
 * @description:
 */
@RestController
@RequestMapping("/admin/product/brand")
public class BrandController {
    @Autowired
    private AdminBrandFeign adminBrandFeign;
    @Autowired
    private ExcelUtils excelUtils;
    /**
     * 品牌列表
     * @param brandListDto
     * @return
     */
    @PostMapping("/brandList")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.find('COMMON_BRAND'))")
    ApiResult<MyPageInfo<BrandListVo>> brandList(@Validated @RequestBody BrandListDto brandListDto){
        return adminBrandFeign.brandList(brandListDto);
    }

    /**
     * 新增品牌
     * @param addOrUpdateBrandDto
     * @return
     */
    @PostMapping("/addBrand")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.add('COMMON_BRAND'))")
    ApiResult addBrand(@Validated @RequestBody AddOrUpdateBrandDto addOrUpdateBrandDto){
        return adminBrandFeign.addOrUpdateBrand(addOrUpdateBrandDto);
    }

    /**
     * 新增或编辑品牌
     * @param addOrUpdateBrandDto
     * @return
     */
    @PostMapping("/updateBrand")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('COMMON_BRAND'))")
    ApiResult updateBrand(@Validated @RequestBody AddOrUpdateBrandDto addOrUpdateBrandDto){
        return adminBrandFeign.addOrUpdateBrand(addOrUpdateBrandDto);
    }

    /**
     * 删除
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/deleteBrand")
    @ResponseBody
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('COMMON_BRAND'))")
    ApiResult deleteBrand(@Validated @RequestBody DeleteIdsDto deleteIdsDto){
        return adminBrandFeign.deleteBrand(deleteIdsDto);
    }

    /**
     * 获取品牌列表
     * @param
     * @return
     */
    @PostMapping("/getBrandList")
    @ResponseBody
    ApiResult<List<BrandListVo>> getBrandList(){
        return adminBrandFeign.getBrandList();
    }


    //导入excel
    @RequestMapping(value = "excelImport", method = {RequestMethod.GET, RequestMethod.POST })
    public ApiResult excelImport(HttpServletRequest request, @RequestParam("uploadFile") MultipartFile[] files) throws Exception {
        if(files != null && files.length > 0){
            MultipartFile file = files[0];
            List<Object> list = excelUtils.readExcel(file, new ProductDemo(),1,1);
            if(list != null && list.size() > 0){
                for(Object o : list){
                    ProductDemo xfxx = (ProductDemo) o;
                    System.out.println(xfxx.getXm()+"/"+xfxx.getWxh()+"/"+xfxx.getSjh());
                }
            }
        }
        return ApiResult.success();
    }
}
