package com.baibei.shiyi.product.web.api;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ApiGroupProductController
 * @Description 商品组相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/api/product/groupProduct")
public class ApiGroupProductController {
    @Autowired
    private IProductGroupRefService productGroupRefService;

    /**
     * 分页列表
     * @param groupRefDto
     * @return
     */
    @PostMapping("/pageList")
    public ApiResult<MyPageInfo<GroupProductVo>> pageList(@Validated @RequestBody GroupRefDto groupRefDto) {
        return ApiResult.success(productGroupRefService.pageList(groupRefDto));
    }
}
