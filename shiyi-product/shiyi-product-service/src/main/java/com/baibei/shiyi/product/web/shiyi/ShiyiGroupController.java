package com.baibei.shiyi.product.web.shiyi;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.*;
import com.baibei.shiyi.product.feign.bean.vo.BaseShelfVo;
import com.baibei.shiyi.product.feign.bean.vo.CategoryVo;
import com.baibei.shiyi.product.feign.client.admin.IAdmCategoryFeign;
import com.baibei.shiyi.product.feign.client.shiyi.GroupFeign;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.product.service.ICategoryService;
import com.baibei.shiyi.product.service.IGroupService;
import com.baibei.shiyi.product.service.IShelfCategoryRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @Classname AdmProductController
 * @Description 后台管理商品相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@Controller
public class ShiyiGroupController implements GroupFeign {
    @Value("${product.group.viewcount}")
    private String groupViewCount;
    @Autowired
    private IGroupService groupService;

    @Override
    public ApiResult<Integer> sumGroupProduct(@Validated @RequestBody SumGroupProductDto sumGroupProductDto) {
        Group group = groupService.findById(sumGroupProductDto.getGroupId());
        if (StringUtils.isEmpty(group)) {
            throw new ServiceException("未找到指定分组");
        }
        if (Constants.GroupType.HOT.equals(group.getGroupType())||Constants.GroupType.NEW.equals(group.getGroupType())) {
            return ApiResult.success(Integer.parseInt(groupViewCount));
        }
        int i = groupService.sumGroupProduct(sumGroupProductDto.getGroupId());
        return ApiResult.success(i);
    }
}
