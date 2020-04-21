package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.AdmEditProductVo;
import com.baibei.shiyi.product.feign.bean.vo.AdmProductVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmProductFeign;
import com.baibei.shiyi.product.feign.client.admin.IProductStockFeign;
import com.baibei.shiyi.product.service.IProductService;
import com.baibei.shiyi.product.service.IProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/product/stock")
public class AdmProductStockController implements IProductStockFeign {

    @Autowired
    private IProductStockService productStockService;

    @Override
    public ApiResult changeStock(@Validated @RequestBody ChangeStockDto changeStockDto) {
        productStockService.changeStock(changeStockDto);
        return ApiResult.success();
    }
}
