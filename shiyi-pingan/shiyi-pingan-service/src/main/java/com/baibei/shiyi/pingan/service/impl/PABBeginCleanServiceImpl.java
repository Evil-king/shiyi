package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.pingan.feign.base.dto.BeginCleanDto;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.service.IBeginCleanService;
import com.baibei.shiyi.pingan.service.IPABSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * 交易网查询银行清算与对账文件的进度1004
 */
@Service
public class PABBeginCleanServiceImpl implements IBeginCleanService {

    @Autowired
    private IPABSendService sendService;

    @Value("${cash.supAcctId}")
    protected String supAcctId;

    @Override
    public void beginClean(BeginCleanDto dto) {
        Map<String, String> params = new HashMap<>();
        params.put("FuncFlag", dto.getFuncFlag());
        params.put("FileName", dto.getFileName());
        params.put("FileSize", dto.getFileSize());
        params.put("SupAcctId", supAcctId);
        params.put("QsZcAmount", dto.getQsZcAmount().toPlainString());
        params.put("FreezeAmount", dto.getFreezeAmount().toPlainString());
        params.put("UnfreezeAmount", dto.getUnfreezeAmount().toPlainString());
        params.put("SyZcAmount", dto.getSyZcAmount().toPlainString());
        params.put("Reserve", dto.getReserve());
        String message = BankMessageSpliceUtils.getSignMessageBody_1003(params);
        // stop 交易网发送消息
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(PABFunctionType.BEGIN_CLEAN.index);
        pabSendDto.setThirdLogNo(sendService.generateThiredLogNo());
        pabSendDto.setMessage(message);
        // 获取发送结果，并解析
        ApiResult<PABSendVo> result = sendService.sendMessage(pabSendDto);
        if (!result.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            Map<String, String> errorMessage = BankMessageAnalysisUtils.parsingTranMessageString(result.getMsg());
            throw new ServiceException(String.format("发起清算失败:%s", errorMessage.get("RspMsg")));
        }
    }
}
