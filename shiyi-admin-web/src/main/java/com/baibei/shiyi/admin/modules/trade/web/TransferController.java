package com.baibei.shiyi.admin.modules.trade.web;


import com.alibaba.fastjson.JSON;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDataDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferDataVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.feign.client.admin.IAdminTransferFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/trade/transfer")
@Slf4j
public class TransferController {

    @Autowired
    private IAdminTransferFeign adminTransferFeign;

    @Autowired
    private ExcelUtils excelUtils;


    @RequestMapping("/pageListLog")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('NON_TRANSACTION_TRANSFER'))")
    public ApiResult<MyPageInfo<TransferLogVo>> pageListLog(@RequestBody TransferLogDto transferLogDto){
        return adminTransferFeign.pageListLog(transferLogDto);
    }

    /**
     * 过户数据列表页
     * @param transferPageListDto
     * @return
     */
    @RequestMapping("/listPage")
    @PreAuthorize("hasAnyRole(@authorityExpression.edit('NON_TRANSACTION_TRANSFER'))")
    public ApiResult<MyPageInfo<TransferPageListVo>> listPage(@RequestBody TransferPageListDto transferPageListDto){
        return adminTransferFeign.listPage(transferPageListDto);
    }

    /**
     * 过户数据列表导出
     * @param transferPageListDto
     * @param response
     */
    @RequestMapping("/export")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('NON_TRANSACTION_TRANSFER'))")
    public void export(@RequestBody TransferPageListDto transferPageListDto, HttpServletResponse response){
        ApiResult<List<TransferPageListVo>> listApiResult = adminTransferFeign.export(transferPageListDto);
        if (listApiResult.getCode().equals(ApiResult.serviceFail().getCode())) {
            throw new ServiceException("导出失败:" + listApiResult.getMsg());
        }
        excelUtils.defaultExcelExport(listApiResult.getData(), response, TransferPageListVo.class, "过户数据列表");
    }

    /**
     * 模板下载
     * @return
     */
    @PostMapping(path = "/templateDownload")
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('NON_TRANSACTION_TRANSFER'))")
    public void templateDownload(HttpServletResponse response) {
        List<TransferTemplateVo> transferTemplateVoArrayList = new ArrayList<>();
        excelUtils.defaultExcelExport(transferTemplateVoArrayList, response, TransferTemplateVo.class, "非交易过户");
    }

    /**
     * 导入文件
     * @return
     */
    @PostMapping(path = "/importFile")
    @PreAuthorize("hasAnyRole(@authorityExpression.importFile('NON_TRANSACTION_TRANSFER'))")
    public ApiResult<TransferDataVo> importFile(@RequestParam(value = "file") MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        List<TransferTemplateVo> transferTemplateVos = null;
        try {
            transferTemplateVos = excelUtils.defaultReadExcelFile(multipartFile, TransferTemplateVo.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("e={}", e.getMessage());
            return ApiResult.error("导入失败，请检查数据后重新导入！");
        }
        transferTemplateVos.stream().forEach(transferTemplateVo -> {
            transferTemplateVo.setCreator(SecurityUtils.getUsername());
            transferTemplateVo.setModifier(SecurityUtils.getUsername());
            transferTemplateVo.setFileName(fileName);
        });
        log.info("参数打印,transferTemplateVos={}", JSON.toJSONString(transferTemplateVos));
        return adminTransferFeign.importData(transferTemplateVos);
    }

    /**
     * 删除
     * @param transferDeleteDto
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('NON_TRANSACTION_TRANSFER'))")
    public ApiResult delete(@RequestBody TransferDeleteDto transferDeleteDto){
        return adminTransferFeign.deleteData(transferDeleteDto);
    }

    /**
     * 执行操作
     * @param transferDataDto
     * @return
     */
    @RequestMapping("/execute")
    @PreAuthorize("hasAnyRole(@authorityExpression.execute('NON_TRANSACTION_TRANSFER'))")
    public ApiResult<TransferDataVo> execute(@RequestBody TransferDataDto transferDataDto){
        return adminTransferFeign.transferData(transferDataDto);
    }

}
