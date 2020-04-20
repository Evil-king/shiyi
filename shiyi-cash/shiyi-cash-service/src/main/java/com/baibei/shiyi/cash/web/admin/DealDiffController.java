package com.baibei.shiyi.cash.web.admin;

import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.cash.feign.base.dto.BankOrderDto;
import com.baibei.shiyi.cash.feign.base.dto.DealDiffDto;
import com.baibei.shiyi.cash.feign.base.message.DealDiffMessage;
import com.baibei.shiyi.cash.feign.base.vo.BankOrderVo;
import com.baibei.shiyi.cash.feign.client.admin.IAdminDealDiffFeign;
import com.baibei.shiyi.cash.service.IBankOrderService;
import com.baibei.shiyi.cash.service.IWithDrawDepositDiffService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/diff")
public class DealDiffController implements IAdminDealDiffFeign {
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private IWithDrawDepositDiffService withDrawDepositDiffService;
    @Autowired
    private IBankOrderService bankOrderService;

    @Value("100000")
    private int exportLimit;//导出限制条数


    @Override
    public ApiResult diffList(@RequestParam("batchNo") String batchNo) {
        ApiResult apiResult = withDrawDepositDiffService.withDrawDepositDiff(batchNo);
        return apiResult;
    }

    @Override
    public ApiResult dealDiff(@RequestBody @Validated DealDiffDto dealDiffDto) {
        ApiResult apiResult;
        try{
            ApiResult<DealDiffMessage> dealDiffMessageApiResult = withDrawDepositDiffService.dealDiff(dealDiffDto);
            DealDiffMessage dealDiffMessage = dealDiffMessageApiResult.getData();
            if(dealDiffMessage.isChangeFlag()&&dealDiffMessage.getCashChangeAmountDtoList().size()>0){//需要修改用户资金
                rocketMQUtil.sendMsg(propertiesVal.getDealdiffTopic(),JacksonUtil.beanToJson(dealDiffMessage),UUID.randomUUID().toString());
            }
            apiResult=ApiResult.success();
        }catch (Exception e){
            apiResult=ApiResult.error(e.getMessage());
        }
        return apiResult;
    }

    @Override
    public ApiResult<MyPageInfo<BankOrderVo>> bankOrderPageList(@RequestBody BankOrderDto bankOrderDto) {
        return ApiResult.success(bankOrderService.pageList(bankOrderDto));
    }

    @Override
    public ApiResult<List<BankOrderVo>> bankOrderExcelExport(@RequestBody BankOrderDto bankOrderDto) {
        bankOrderDto.setExportLimit(exportLimit);
        List<BankOrderVo> bankOrderVos = bankOrderService.BankOrderVoList(bankOrderDto);
        return ApiResult.success(bankOrderVos);
    }
}
