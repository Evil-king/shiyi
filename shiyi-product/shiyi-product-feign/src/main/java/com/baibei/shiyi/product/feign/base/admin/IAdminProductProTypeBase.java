package com.baibei.shiyi.product.feign.base.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.page.PageParam;
import com.baibei.shiyi.product.feign.bean.dto.AddProTypeDto;
import com.baibei.shiyi.product.feign.bean.dto.DeleteIdsDto;
import com.baibei.shiyi.product.feign.bean.dto.UpdateProTypeDto;
import com.baibei.shiyi.product.feign.bean.vo.ProTypeVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/9/6 16:19
 * @description:
 */
public interface IAdminProductProTypeBase {

    /**
     * 后台类目列表
     * @param pageParam
     * @return
     */
    @PostMapping("/admin/product/proType/list")
    @ResponseBody
    ApiResult<MyPageInfo<ProTypeVo>> list(@Validated @RequestBody PageParam pageParam);

    /**
     * 获取所有后台类目
     * @param
     * @return
     */
    @PostMapping("/admin/product/proType/getAll")
    @ResponseBody
    ApiResult<List<ProTypeVo>> getAll();

    /**
     * 添加类目
     * @param addProTypeDto
     * @return
     */
    @PostMapping("/admin/product/proType/add")
    @ResponseBody
    ApiResult add(@Validated @RequestBody AddProTypeDto addProTypeDto);

    /**
     * 修改类目
     * @param updateProTypeDto
     * @return
     */
    @PostMapping("/admin/product/proType/update")
    @ResponseBody
    ApiResult update(@Validated @RequestBody UpdateProTypeDto updateProTypeDto);

    /**
     * 删除
     * @param deleteIdsDto
     * @return
     */
    @PostMapping("/admin/product/proType/delete")
    @ResponseBody
    ApiResult delete(@Validated @RequestBody DeleteIdsDto deleteIdsDto);


}
