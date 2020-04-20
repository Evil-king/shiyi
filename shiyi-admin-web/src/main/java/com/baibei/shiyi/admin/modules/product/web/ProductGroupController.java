package com.baibei.shiyi.admin.modules.product.web;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.admin.IGroupFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/product/group")
public class ProductGroupController {

    @Autowired
    private IGroupFeign groupFeign;

    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('PRODUCT_MANAGE_GROUP'))")
    public ApiResult<MyPageInfo<GroupVo>> pageList(@RequestBody GroupDto groupDto) {
        return groupFeign.pageList(groupDto);
    }

    @PostMapping("/findByList")
    public ApiResult<List<GroupVo>> findByList(@RequestBody GroupDto groupDto) {
        return groupFeign.findByList(groupDto);
    }


    @PostMapping("/getById")
    public ApiResult<GroupVo> getById(@RequestBody GroupCurdDto groupDto) {
        return groupFeign.getById(groupDto);
    }

    @PostMapping(path = "/deleteById")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_MANAGE_GROUP'))")
    public ApiResult deleteById(@RequestBody GroupCurdDto groupDto) {
        return groupFeign.deleteById(groupDto);
    }

    @PostMapping(path = "/save")
    @PreAuthorize("hasAnyRole(@authorityExpression.add('PRODUCT_MANAGE_GROUP'))")
    public ApiResult save(@Validated @RequestBody GroupCurdDto groupDto) {
        return groupFeign.save(groupDto);
    }

    @PostMapping(path = "/update")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_MANAGE_GROUP'))")
    public ApiResult update(@Validated @RequestBody GroupCurdDto groupDto) {
        return groupFeign.update(groupDto);
    }

    @PostMapping(path = "/deleteProductGroupRef")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('PRODUCT_MANAGE_GROUP'))")
    public ApiResult deleteProductGroupRef(@RequestBody ProductGroupDto productGroupDto) {
        return groupFeign.deleteProductGroupRef(productGroupDto);
    }

    @PostMapping(path = "/findGroupProduct")
    public ApiResult<MyPageInfo<GroupProductVo>> findGroupProduct(@RequestBody ProductGroupDto productGroupDto) {
        return groupFeign.findGroupProduct(productGroupDto);
    }

    @PostMapping(path = "/findNoExistProductGroup")
    public ApiResult<MyPageInfo<GroupProductVo>> findNoExistProductGroup(@RequestBody ProductGroupDto productGroupDto) {
        return groupFeign.findNoExistProductGroup(productGroupDto);
    }

    @PostMapping(path = "/batchDelete")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('PRODUCT_MANAGE_GROUP'))")
    public ApiResult batchDelete(@RequestBody GroupCurdDto groupCurdDto) {
        return groupFeign.batchDelete(groupCurdDto);
    }

    /**
     * 首页预览，商品组
     * @param groupRefDto
     * @return
     */
    @PostMapping("/indexGroupProduct")
    public ApiResult<MyPageInfo<GroupProductVo>> indexGroupProduct(@Validated @RequestBody GroupRefDto groupRefDto) {
        return groupFeign.indexGroupProduct(groupRefDto);
    }

}
