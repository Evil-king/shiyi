package com.baibei.shiyi.product.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/24 9:49 AM
 * @description:
 */
@RestController
@RequestMapping("/shiyi/demo")
public class ShiyiDemoController {
    @Autowired
    private IProductGroupRefService productGroupRefService;

    @PostMapping("/test")
    public ApiResult<MyPageInfo<GroupProductVo>> test(@Validated @RequestBody GroupRefDto groupRefDto){
        MyPageInfo<GroupProductVo> groupProductVoMyPageInfo = productGroupRefService.pageList(groupRefDto);
        return ApiResult.success(groupProductVoMyPageInfo);
    }
}
