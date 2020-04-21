package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.pingan.feign.base.dto.FilePlannedSpeedDto;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.FilePlannedSpeedVo;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.service.IFilePlannedSpeedService;
import com.baibei.shiyi.pingan.service.IPABSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * 交易网查询银行清算与对账文件的进度1004
 */
@Service
@Slf4j
public class PABFilePlannedSpeedServiceImpl implements IFilePlannedSpeedService {

    @Autowired
    private IPABSendService sendService;

    @Value("${cash.supAcctId}")
    protected String supAcctId;

    @Override
    public FilePlannedSpeedVo filePlannedSpeed(FilePlannedSpeedDto filePlannedSpeedDto) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        Map<String, String> params = new HashMap<>();
        params.put("FuncFlag", filePlannedSpeedDto.getFuncFlag());
        params.put("BeginDate", simpleDateFormat.format(filePlannedSpeedDto.getBeginDate()));
        params.put("EndDate", simpleDateFormat.format(filePlannedSpeedDto.getEndDate()));
        params.put("SupAcctId", supAcctId);
        params.put("Reserve", "");
        String message = BankMessageSpliceUtils.getSignMessageBody_1004(params);
        // stop 交易网发送消息
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(PABFunctionType.FIND_FILE_PLANNED_SPEED.index);
        pabSendDto.setThirdLogNo(sendService.generateThiredLogNo());
        pabSendDto.setMessage(message);
        // 获取发送结果，并解析
        ApiResult<PABSendVo> result = sendService.sendMessage(pabSendDto);
        if (!result.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            Map<String, String> errorMessage = BankMessageAnalysisUtils.parsingTranMessageString(result.getMsg());
            throw new ServiceException(String.format("交易网查询银行清算与对账文件的进度失败:%s", errorMessage.get("RspMsg")));
        }
        PABSendVo pabSendVo = result.getData();
        Map<String, String> retKeyDict = new HashMap<>();
        retKeyDict.put("backBodyMessages", pabSendVo.getBackBodyMessages());
        BankMessageAnalysisUtils.spiltMessage_1004(retKeyDict);
        FilePlannedSpeedVo filePlannedSpeedVo = new FilePlannedSpeedVo();
        filePlannedSpeedVo.setRecordCount(Integer.valueOf(retKeyDict.get("RecordCount")));
        filePlannedSpeedVo.setResultFlag(getDesc(retKeyDict.get("ResultFlag")));
        return filePlannedSpeedVo;
    }

    /**
     * （1:正取批量文件; 2:取批量文件失败  3:正在读取文件; 4:读取文件失败 5：正在处理中; 6：处理完成,但部分成功 7：处理全部失败, 8:处理完全成功 9:处理完成,但生成处理结果文件失败）
     *
     * @param resultFlag
     * @return
     */
    private String getDesc(String resultFlag) {
        switch (resultFlag) {
            case "1":
                return "正取批量文件";
            case "2":
                return "取批量文件失败";
            case "3":
                return "正在读取文件";
            case "4":
                return "读取文件失败";
            case "5":
                return "正在处理中";
            case "6":
                return "处理完成,但部分成功";
            case "7":
                return "处理全部失败";
            case "8":
                return "处理完全成功";
            case "9":
                return "处理完成,但生成处理结果文件失败";
            default:
                return "";
        }
    }


}
