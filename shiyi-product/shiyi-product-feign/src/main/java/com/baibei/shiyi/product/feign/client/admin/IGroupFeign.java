package com.baibei.shiyi.product.feign.client.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupRefDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupProductVo;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.feign.client.hystrix.GroupHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "${shiyi-product:shiyi-product}", path = "/admin/product/group", fallbackFactory = GroupHystrix.class)
public interface IGroupFeign {

    @PostMapping("/pageList")
    ApiResult<MyPageInfo<GroupVo>> pageList(@Validated @RequestBody GroupDto groupDto);

    @PostMapping("/getById")
    ApiResult<GroupVo> getById(@RequestBody GroupCurdDto groupDto);

    @PostMapping(path = "/deleteById")
    ApiResult deleteById(@RequestBody GroupCurdDto groupDto);

    @PostMapping(path = "/save")
    ApiResult save(@Validated @RequestBody GroupCurdDto groupDto);

    @PostMapping(path = "/update")
    ApiResult update(@Validated @RequestBody GroupCurdDto groupDto);

    @PostMapping(path = "/batchDelete")
    ApiResult batchDelete(@RequestBody GroupCurdDto groupCurdDto);


    @PostMapping(path = "/deleteProductGroupRef")
    ApiResult deleteProductGroupRef(@Validated @RequestBody ProductGroupDto productGroupDto);

    /**
     * 查询分组的商品
     *
     * @param productGroupDto
     * @return
     */
    @PostMapping(path = "/findGroupProduct")
    ApiResult<MyPageInfo<GroupProductVo>> findGroupProduct(@RequestBody ProductGroupDto productGroupDto);

    /**
     * 查询分组不存在的商品
     *
     * @param productGroupDto
     * @return
     */
    @PostMapping(path = "/findNoExistProductGroup")
    ApiResult<MyPageInfo<GroupProductVo>> findNoExistProductGroup(@RequestBody ProductGroupDto productGroupDto);

    /**
     * 查询商品分组
     *
     * @param groupDto
     * @return
     */
    @PostMapping(path = "/findByList")
    ApiResult<List<GroupVo>> findByList(@RequestBody GroupDto groupDto);


    @PostMapping("/indexGroupProduct")
    ApiResult<MyPageInfo<GroupProductVo>> indexGroupProduct(@Validated @RequestBody GroupRefDto groupRefDto);
}
