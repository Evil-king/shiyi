package com.baibei.shiyi.admin.modules.content.controller;

import com.baibei.shiyi.admin.modules.utils.ExcelUtils;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.content.feign.bean.dto.DeleteFeedbackDto;
import com.baibei.shiyi.content.feign.bean.dto.FeedBackDto;
import com.baibei.shiyi.content.feign.bean.vo.FeedBackExportVo;
import com.baibei.shiyi.content.feign.client.ContentFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/content/feedback")
public class FeedBackController {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private ExcelUtils excelUtils;

    @RequestMapping("/pageList")
    @PreAuthorize("hasAnyRole(@authorityExpression.find('FEEDBACK_MANAGE_LIST'))")
    public ApiResult pageList(@RequestBody FeedBackDto feedBackDto) {
        return contentFeign.feedBackPageList(feedBackDto);
    }

    @RequestMapping("/deleteFeedback")
    @PreAuthorize("hasAnyRole(@authorityExpression.delete('FEEDBACK_MANAGE_LIST'))")
    public ApiResult deleteFeedback(@RequestBody DeleteFeedbackDto deleteFeedbackDto) {
        return contentFeign.deleteFeedback(deleteFeedbackDto);
    }

    @RequestMapping(path = "/exportData", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole(@authorityExpression.exportFile('FEEDBACK_MANAGE_LIST'))")
    public void exportData(@RequestBody FeedBackDto feedBackDto, HttpServletResponse response) {
        ApiResult<List<FeedBackExportVo>> listApiResult = contentFeign.exportData(feedBackDto);
        if (listApiResult.getCode().equals(ApiResult.serviceFail().getCode())) {
            throw new ServiceException("导出失败:" + listApiResult.getMsg());
        }
        excelUtils.defaultExcelExport(listApiResult.getData(), response, FeedBackExportVo.class, "意见反馈列表");
    }
}
