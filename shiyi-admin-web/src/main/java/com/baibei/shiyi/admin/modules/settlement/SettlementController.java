package com.baibei.shiyi.admin.modules.settlement;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.settlement.feign.bean.dto.SettlementDataDto;
import com.baibei.shiyi.settlement.feign.bean.vo.SettlementDataVo;
import com.baibei.shiyi.settlement.feign.client.IAdminSettlementFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/5 18:27
 * @description:
 */
@RestController
@RequestMapping("/admin/settlement")
@Slf4j
public class SettlementController {

    @Autowired
    private IAdminSettlementFeign adminSettlementFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @PostMapping(path = "/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('SETTLEMENT_MASTER_SETTLEMENT'))")
    public ApiResult<MyPageInfo<SettlementDataVo>> pageList(@Validated @RequestBody SettlementDataDto dto) {
        return adminSettlementFeign.pageList(dto);
    }

    @PostMapping(path = "/fundTransferSingle")
    @PreAuthorize("hasAnyRole(@authorityExpression.fundTransferSingle('SETTLEMENT_MASTER_SETTLEMENT'))")
    public ApiResult fundTransferSingle(@RequestBody SettlementDataDto dto) {
        return adminSettlementFeign.fundTransferSingle(dto);
    }

    @PostMapping(path = "/fundTransfer")
    @PreAuthorize("hasAnyRole(@authorityExpression.fundTransfer('SETTLEMENT_MASTER_SETTLEMENT'))")
    public ApiResult fundTransfer() {
        return adminSettlementFeign.fundTransfer();
    }

    @PostMapping(path = "/exportExcel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('SETTLEMENT_MASTER_SETTLEMENT'))")
    public void excelExport(@RequestBody SettlementDataDto dto, HttpServletResponse response) {
        ApiResult<List<SettlementDataVo>> apiResult = adminSettlementFeign.exportExcel(dto);
        if (apiResult.hasFail()) {
            log.info("导出失败，apiResult={}", apiResult.toString());
            throw new ServiceException("导出失败");
        }
        excelUtils.defaultExcelExport(apiResult.getData(), response, SettlementDataVo.class, "清算数据列表");
    }
}