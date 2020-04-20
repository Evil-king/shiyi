package com.baibei.shiyi.admin.modules.account.web;

import com.baibei.shiyi.account.feign.bean.dto.OperationDto;
import com.baibei.shiyi.account.feign.bean.dto.PassCardExtractOrderListDto;
import com.baibei.shiyi.account.feign.bean.vo.PassCardExtractOrderListVo;
import com.baibei.shiyi.account.feign.client.admin.AdminPassCardExtractOrderFeign;
import com.baibei.shiyi.content.feign.bean.dto.UpdateAuditQuantityDto;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/11/12 15:37
 * @description:
 */
@RestController
@RequestMapping("/admin/account/passCardExtractOrder")
public class PassCardExtractOrderController {
    @Autowired
    private AdminPassCardExtractOrderFeign passCardExtractOrderFeign;

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/getPageList")
    public ApiResult<MyPageInfo<PassCardExtractOrderListVo>> getPageList(@RequestBody PassCardExtractOrderListDto passCardExtractOrderListDto) {
        return passCardExtractOrderFeign.getPageList(passCardExtractOrderListDto);
    }

    @RequestMapping("/operation")
    public ApiResult operation(@RequestBody @Validated OperationDto operationDto){
        operationDto.setOperatorName(SecurityUtils.getUsername());
        return passCardExtractOrderFeign.operation(operationDto);
    }
    /**
     * 获取仓单提取自动审核最小数量
     */
    @RequestMapping("/getAuditQuantity")
    public ApiResult<UpdateAuditQuantityDto> getAuditQuantity(){
        ApiResult<Integer> auditQuantity = contentFeign.getAuditQuantity();
        if(!auditQuantity.hasSuccess()){
            return ApiResult.build(auditQuantity);
        }
        UpdateAuditQuantityDto updateAuditQuantityDto=new UpdateAuditQuantityDto();
        updateAuditQuantityDto.setAuditQuantityCount(auditQuantity.getData());
        return ApiResult.success(updateAuditQuantityDto);
    }

    /**
     * 修改自动审核数量
     * @param updateAuditQuantityDto
     * @return
     */
    @RequestMapping("/updateAuditQuantity")
    public ApiResult updateAuditQuantity(@RequestBody @Validated UpdateAuditQuantityDto updateAuditQuantityDto){
        return contentFeign.updateAuditQuantity(updateAuditQuantityDto);
    }
    @RequestMapping("/export")
    public void export(@RequestBody PassCardExtractOrderListDto passCardExtractOrderListDto, HttpServletResponse response){
        ApiResult<List<PassCardExtractOrderListVo>> export = passCardExtractOrderFeign.export(passCardExtractOrderListDto);
        if(export.hasSuccess()){
            for (int i = 0; i <export.getData().size() ; i++) {
                PassCardExtractOrderListVo passCardExtractOrderListVo=export.getData().get(i);
                if(Constants.ExtractOrderStatus.WAIT.equals(passCardExtractOrderListVo.getStatus())){
                    passCardExtractOrderListVo.setStatus("待审核");
                }else if(Constants.ExtractOrderStatus.FAIL.equals(passCardExtractOrderListVo.getStatus())){
                    passCardExtractOrderListVo.setStatus("驳回");
                }else if(Constants.ExtractOrderStatus.SUCCESS.equals(passCardExtractOrderListVo.getStatus())){
                    passCardExtractOrderListVo.setStatus("通过");
                }
            }
        }
        excelUtils.defaultExcelExport(export.getData(),response,PassCardExtractOrderListVo.class,"仓单提取记录");
    }

}
