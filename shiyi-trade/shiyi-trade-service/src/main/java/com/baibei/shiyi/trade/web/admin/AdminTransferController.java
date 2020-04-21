package com.baibei.shiyi.trade.web.admin;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.trade.feign.base.IAdminTransferBase;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDataDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferDataVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferDetails;
import com.baibei.shiyi.trade.service.*;
import com.baibei.shiyi.trade.thread.CalculationCostThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class AdminTransferController implements IAdminTransferBase {

    @Autowired
    private ITransferLogService transferLogService;
    @Autowired
    private ITransferDetailsService transferDetailsService;
    @Autowired
    private ITransferLogRecordService transferLogRecordService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    /**
     * 异步计算持仓成本线程池
     */
    private ExecutorService costExecutorService = Executors.newFixedThreadPool(5);

    @Override
    public ApiResult<MyPageInfo<TransferLogVo>> pageListLog(@RequestBody TransferLogDto transferLogDto) {
        return ApiResult.success(transferLogService.pageListLog(transferLogDto));
    }

    @Override
    public ApiResult<MyPageInfo<TransferPageListVo>> listPage(@RequestBody TransferPageListDto transferPageListDto) {
        return ApiResult.success(transferDetailsService.listPage(transferPageListDto));
    }

    @Override
    public ApiResult<List<TransferPageListVo>> export(@RequestBody TransferPageListDto transferPageListDto) {
        List<TransferPageListVo> pageListVos = transferDetailsService.export(transferPageListDto);
        return ApiResult.success(pageListVos);
    }

    @Override
    public ApiResult<TransferDataVo> transferData(@RequestBody TransferDataDto transferDataDto) {
        //判断是否闭式
        if (!tradeDayService.isClose()) {
            log.info("当前已经开市");
            return ApiResult.error("请在闭市之后执行");
        } else {
        TransferDataVo transferDataVo = new TransferDataVo();
        int failCount = 0;
        int successCount = 0;
        String orderNo = "";
        BigDecimal buyFee = null;
        BigDecimal sellFee = null;
        List<TransferTemplateVo> transferTemplateVos = transferLogRecordService.selectByTransferLogId(transferDataDto.getTransferLogId());
        transferDataVo.setOriginCount(transferTemplateVos.size());
        for (int i = 0; i < transferTemplateVos.size(); i++) {
            orderNo = NoUtil.generateOrderNo();
            TransferTemplateVo transferTemplateVo = transferTemplateVos.get(i);
            ApiResult apiResult = null;
            try {
                buyFee = transferDetailsService.buyFee(transferTemplateVo.getInCustomerNo(),
                        transferTemplateVo.getPrice(), transferTemplateVo.getNum(), transferTemplateVo.getIsFee());

                sellFee = transferDetailsService.sellFee(transferTemplateVo.getOutCustomerNo(),
                        transferTemplateVo.getPrice(), transferTemplateVo.getNum(), transferTemplateVo.getIsFee());

                apiResult = transferDetailsService.transferData(transferTemplateVo,
                        transferDataDto.getTransferLogId(), orderNo, buyFee, sellFee);
                if (apiResult.hasFail()) {
                    //写入transfer_details表中
                    TransferDetails transferDetails = transferDetailsService.operatorObj(orderNo, transferTemplateVo, buyFee, sellFee);
                    transferDetails.setTransferLogId(transferDataDto.getTransferLogId());
                    transferDetailsService.operatorTransferDetailsDB(transferDetails, "fail");
                    transferDataVo.setFailCount(++failCount);
                    continue;
                }
                if (apiResult.hasSuccess()) {
                    transferDataVo.setSuccessCount(++successCount);
                    // 异步计算成本价
                    costExecutorService.submit(new CalculationCostThread(transferTemplateVo.getInCustomerNo(),
                            transferTemplateVo.getProductTradeNo(), holdDetailsService));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //写入transfer_details表中
                TransferDetails transferDetails = transferDetailsService.operatorObj(orderNo, transferTemplateVo, buyFee, sellFee);
                transferDetails.setTransferLogId(transferDataDto.getTransferLogId());
                transferDetailsService.operatorTransferDetailsDB(transferDetails, "fail");
                transferDataVo.setFailCount(++failCount);
                continue;
            }
        }
        //更新log表记录
        transferLogService.updateStatus(transferDataDto.getTransferLogId());
        return ApiResult.success(transferDataVo);
        }
    }

    @Override
    public ApiResult importData(@RequestBody List<TransferTemplateVo> transferTemplateListVo) {
        return transferLogService.importData(transferTemplateListVo);
    }

    @Override
    public ApiResult deleteData(@RequestBody TransferDeleteDto transferDeleteDto) {
        return transferLogService.deleteData(transferDeleteDto);
    }
}
