package com.baibei.shiyi.admin.modules.trade.web;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.CustomerEntrustOrderListDto;
import com.baibei.shiyi.trade.feign.bean.dto.HoldPositionDto;
import com.baibei.shiyi.trade.feign.bean.vo.HoldPositionVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminHoldPositionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 持仓汇总查询
 */
@RestController
@RequestMapping("/admin/holdPosition")
public class HoldPositionController {

    @Autowired
    private IAdminHoldPositionFeign holdPositionFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('CUSTOMER_POSITION_DETAILS'))")
    public ApiResult<MyPageInfo<HoldPositionVo>> pageList(@Validated @RequestBody HoldPositionDto holdPositionDto) {
        return holdPositionFeign.pageList(holdPositionDto);
    }

    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('CUSTOMER_POSITION_DETAILS'))")
    public void export(@RequestBody HoldPositionDto holdPositionDto, HttpServletResponse response) {
        ApiResult<List<HoldPositionVo>> result = holdPositionFeign.exportList(holdPositionDto);
        if (result.hasFail()) {
            throw new ServiceException("导出失败");
        }
        List<HoldPositionVo> holdPositionVoList = result.getData();
        excelUtils.defaultExcelExport(holdPositionVoList, response, HoldPositionVo.class, "客户持仓查询");
    }

}
