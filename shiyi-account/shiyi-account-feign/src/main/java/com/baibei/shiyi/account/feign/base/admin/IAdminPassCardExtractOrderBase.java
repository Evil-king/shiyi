package com.baibei.shiyi.account.feign.base.admin;

import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/12 15:20
 * @description:
 */
public interface IAdminPassCardExtractOrderBase {
    /**
     * 后台列表
     * @param passCardExtractOrderListDto
     * @return
     */
    @RequestMapping("/admin/account/passCardExtractOrder/getPageList")
    @ResponseBody
    ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(@RequestBody  PassCardExtractOrderListDto passCardExtractOrderListDto);

    /**
     * 批量操作订单
     * @param operationDto
     * @return
     */
    @RequestMapping("/admin/account/passCardExtractOrder/operation")
    @ResponseBody
    ApiResult operation(@RequestBody @Validated OperationDto operationDto);

    @RequestMapping("/admin/account/passCardExtractOrder/export")
    @ResponseBody
    ApiResult<List<PassCardExtractOrderListVo>> export(@RequestBody  PassCardExtractOrderListDto passCardExtractOrderListDto);
}
