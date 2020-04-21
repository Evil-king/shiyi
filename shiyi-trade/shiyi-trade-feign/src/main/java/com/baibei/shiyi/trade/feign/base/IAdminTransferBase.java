package com.baibei.shiyi.trade.feign.base;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDataDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferDataVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface IAdminTransferBase {

    @RequestMapping("/shiyi/admin/trade/transfer/pageListLog")
    ApiResult<MyPageInfo<TransferLogVo>> pageListLog(@RequestBody TransferLogDto transferLogDto);


    @RequestMapping("/shiyi/admin/trade/transfer/listPage")
    ApiResult<MyPageInfo<TransferPageListVo>> listPage(@RequestBody TransferPageListDto transferPageListDto);


    @RequestMapping("/shiyi/admin/trade/transfer/export")
    ApiResult<List<TransferPageListVo>> export(@RequestBody TransferPageListDto transferPageListDto);

    @RequestMapping("/shiyi/admin/trade/transfer/transferData")
    ApiResult<TransferDataVo> transferData(@RequestBody TransferDataDto transferDataDto);

    @RequestMapping("/shiyi/admin/trade/transfer/importData")
    ApiResult importData(@RequestBody List<TransferTemplateVo> transferTemplateVo);


    @RequestMapping("/shiyi/admin/trade/transfer/deleteData")
    ApiResult deleteData(@RequestBody TransferDeleteDto transferDeleteDto);
}
