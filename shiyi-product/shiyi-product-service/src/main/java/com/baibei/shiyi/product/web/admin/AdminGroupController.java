package com.baibei.shiyi.product.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.admin.IGroupFeign;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.product.model.ProductGroupRef;
import com.baibei.shiyi.product.service.IGroupService;
import com.baibei.shiyi.product.service.IProductGroupRefService;
import com.baibei.shiyi.product.service.IProductShelfService;
import com.github.pagehelper.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.servlet.ServletException;
import java.util.List;


/**
 * @Classname ApiGroupProductController
 * @Description 商品组相关
 * @Date 2019/7/30 17:46
 * @Created by Longer
 */

@RestController
@RequestMapping("/admin/product/group")
public class AdminGroupController implements IGroupFeign {
    @Autowired
    private IGroupService groupService;

    @Autowired
    private IProductShelfService productShelfService;

    @Autowired
    private IProductGroupRefService productGroupRefService;


    /**
     * 分组列表
     *
     * @param groupDto
     * @return
     */
    public ApiResult<MyPageInfo<GroupVo>> pageList(@RequestBody GroupDto groupDto) {
        return ApiResult.success(groupService.pageList(groupDto));
    }

    /**
     * 获取商品分类信息
     *
     * @param groupDto
     * @return
     */
    @Override
    public ApiResult<GroupVo> getById(@RequestBody GroupCurdDto groupDto) {
        if (groupDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        GroupVo result = groupService.getById(groupDto);
        return ApiResult.success(result);
    }

    /**
     * 删除商品分组
     *
     * @param groupDto
     * @return
     */
    @Override
    public ApiResult deleteById(@RequestBody GroupCurdDto groupDto) {
        if (groupDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        this.groupService.deleteGroupById(groupDto.getId());
        return ApiResult.success();
    }

    /**
     * 保存商品分组的信息
     *
     * @param groupDto
     * @return
     */
    @Override
    public ApiResult save(@Validated @RequestBody GroupCurdDto groupDto) {
        if (this.groupService.checkTitleRepeat(groupDto)) {
            return ApiResult.error("分组名不能重复");
        }
        ProductGroupRef ref = new ProductGroupRef();
        ref.setGroupId(groupDto.getId());
        if (!groupDto.getShelfIds().isEmpty()) {
            if (groupDto.getShelfIds().size() > 50) {
                return ApiResult.error("商品的分组的数量不能大于50");
            }
        }
        groupService.save(groupDto);
        return ApiResult.success();
    }

    /**
     * 修改商品分组信息
     *
     * @param groupDto
     * @return
     */
    @Override
    public ApiResult update(@Validated @RequestBody GroupCurdDto groupDto) {
        if (groupDto.getId() == null) {
            return ApiResult.error("id不能为空");
        }
        if (this.groupService.checkTitleRepeat(groupDto)) {
            return ApiResult.error("分组名不能重复");
        }
        if (!groupDto.getShelfIds().isEmpty()) {
            if (groupDto.getShelfIds().size() > 50) {
                return ApiResult.error("商品的分组的数量不能大于50");
            }
        }
        groupService.update(groupDto);
        return ApiResult.success();
    }

    /**
     * 批量删除分组信息
     *
     * @param groupCurdDto
     * @return
     */
    @Override
    public ApiResult batchDelete(@RequestBody GroupCurdDto groupCurdDto) {
        if (groupCurdDto.getIds().isEmpty()) {
            return ApiResult.error("分组id不能为空");
        }
        groupService.batchDelete(groupCurdDto.getIds());
        return ApiResult.success();
    }

    /**
     * 删除分组中的商品信息
     *
     * @param productGroupDto
     * @return
     */
    @Override
    public ApiResult deleteProductGroupRef(@RequestBody ProductGroupDto productGroupDto) {
        if (productGroupDto.getShelfIds().isEmpty()) {
            return ApiResult.error("上架商品Id不能为空");
        }
        if (productGroupDto.getGroupId() == null) {
            return ApiResult.error("分组Id不能为空");
        }
        groupService.deleteProductGroupRef(productGroupDto);
        return ApiResult.success();
    }


    @Override
    public ApiResult<MyPageInfo<GroupProductVo>> findGroupProduct(@Validated @RequestBody ProductGroupDto
                                                                          productGroupDto) {
        MyPageInfo<GroupProductVo> pageInfo = productShelfService.findProductGroupList(productGroupDto);
        return ApiResult.success(pageInfo);
    }

    @Override
    public ApiResult<MyPageInfo<GroupProductVo>> findNoExistProductGroup(@Validated @RequestBody ProductGroupDto productGroupDto) {
        if (productGroupDto.getGroupId() == null) {
            return ApiResult.error("商品分组Id不存在");
        }
        return ApiResult.success(productShelfService.findNoExistProductGroup(productGroupDto));
    }

    @Override
    public ApiResult<List<GroupVo>> findByList(@RequestBody GroupDto groupDto) {
        return ApiResult.success(groupService.findByList(groupDto));
    }


    /**
     * 首页预览，商品组
     *
     * @param groupRefDto
     * @return
     */
    @PostMapping("/indexGroupProduct")
    public ApiResult<MyPageInfo<GroupProductVo>> indexGroupProduct(@Validated @RequestBody GroupRefDto groupRefDto) {
        return ApiResult.success(productGroupRefService.pageList(groupRefDto));
    }
}
