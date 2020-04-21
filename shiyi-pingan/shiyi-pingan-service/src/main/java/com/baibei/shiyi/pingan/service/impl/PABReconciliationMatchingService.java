package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.dto.ReconciliationMatchingDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.base.vo.ReconciliationMatchingVo;
import com.baibei.shiyi.pingan.service.IPABReconciliationMatching;
import com.baibei.shiyi.pingan.service.IPABSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 出入金流水对账及会员开销户流水匹配
 */
@Service
public class PABReconciliationMatchingService implements IPABReconciliationMatching {

    @Autowired
    private IPABSendService sendService;

    @Value("${cash.supAcctId}")
    protected String supAcctId; //资金汇总账号

    @Override
    public ReconciliationMatchingVo reconciliationMatching(ReconciliationMatchingDto reconciliationMatchingDto) {

        Map<String, String> result = toParams(reconciliationMatchingDto);
        String message = BankMessageSpliceUtils.getSignMessageBody_1006(result);
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setThirdLogNo(sendService.generateThiredLogNo());
        pabSendDto.setMessage(message);
        pabSendDto.setTranFunc(PABFunctionType.DEPOSIT_WITHDRAW_MEMBERS_DETAIL.index);
        ApiResult<PABSendVo> pabSendVoApiResult = sendService.sendMessage(pabSendDto);
        if (!pabSendVoApiResult.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            Map<String, String> response = BankMessageAnalysisUtils.parsingTranMessageString(pabSendVoApiResult.getMsg());
            throw new ServiceException(String.format("请求平安银行接口失败:%s",response.get("RspMsg")));
        }
        // stop 解析报文
        PABSendVo pabSendVo = pabSendVoApiResult.getData();
        Map<String, String> bodyMessage = new HashMap<>();
        bodyMessage.put("backBodyMessages", pabSendVo.getBackBodyMessages());
        BankMessageAnalysisUtils.spiltMessage_1006(bodyMessage);
        ReconciliationMatchingVo reconciliationMatchingVo = new ReconciliationMatchingVo();
        reconciliationMatchingVo.setFileName(bodyMessage.get("FileName"));
        reconciliationMatchingVo.setReserve(bodyMessage.get("Reserve"));
        return reconciliationMatchingVo;
    }

    /**
     * 拼接参数
     *
     * @param reconciliationMatchingDto
     * @return
     */
    private Map<String, String> toParams(ReconciliationMatchingDto reconciliationMatchingDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Map<String, String> result = new HashMap<>();
        result.put("FuncFlag", reconciliationMatchingDto.getFuncFlag());
        result.put("BeginDateTime", simpleDateFormat.format(reconciliationMatchingDto.getBeginDateTime()));
        result.put("EndDateTime", simpleDateFormat.format(reconciliationMatchingDto.getEndDateTime()));
        result.put("SupAcctId", supAcctId);
        return result;
    }

}
