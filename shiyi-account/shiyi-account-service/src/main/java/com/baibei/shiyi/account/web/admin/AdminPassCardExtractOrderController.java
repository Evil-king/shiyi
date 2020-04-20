package com.baibei.shiyi.account.web.admin;

import com.baibei.shiyi.account.feign.base.admin.IAdminPassCardExtractOrderBase;
import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.service.IPassCardExtractOrderService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/12 15:24
 * @description:
 */
@RestController
public class AdminPassCardExtractOrderController implements IAdminPassCardExtractOrderBase {
    @Autowired
    private IPassCardExtractOrderService passCardExtractOrderService;

    @Override
    public ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(@RequestBody PassCardExtractOrderListDto passCardExtractOrderListDto) {
        return passCardExtractOrderService.getPageList(passCardExtractOrderListDto);
    }

    @Override
    public ApiResult operation(@RequestBody @Validated OperationDto operationDto) {
        return passCardExtractOrderService.operation(operationDto);
    }

    @Override
    public ApiResult<List<PassCardExtractOrderListVo>> export(@RequestBody PassCardExtractOrderListDto passCardExtractOrderListDto) {
        return passCardExtractOrderService.export(passCardExtractOrderListDto);
    }
}
