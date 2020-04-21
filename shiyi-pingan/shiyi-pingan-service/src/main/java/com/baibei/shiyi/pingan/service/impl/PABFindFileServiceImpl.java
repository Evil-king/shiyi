package com.baibei.shiyi.pingan.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.common.tool.utils.SFTPUtils;
import com.baibei.shiyi.pingan.feign.base.dto.ViewFileDto;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABAcceptVo;
import com.baibei.shiyi.pingan.service.base.AbstractPABAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 获取银行通知交易网的文件,使用sftp去下载
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PABFindFileServiceImpl extends AbstractPABAcceptService {


    /**
     * 本地服务器存储文件路径
     */
    @Value("${shiyi.ftp.path}")
    private String localFile;

    @Autowired
    private SFTPUtils sftpUtils;

    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Value("${rocketmq.findFile.topic}")
    private String findFileTopic;

    /**
     * 出入金流水文件
     */
    @Value("${rocketmq.findFile.topic.cashTag}")
    private String mqCashTag;

    /**
     * 清算失败的文件
     */
    @Value("${rocketmq.findFile.topic.batFailResultTag}")
    private String mqFailResultTag;

    /**
     * 清算不平的文件
     */
    @Value("${rocketmq.findFile.topic.batCustDzFailTag}")
    private String mqCustDzFailTag;

    private String FILE_NOT_EXIST = "none.txt";


    @Override
    public PABAcceptVo returnMessage(PABAcceptDto acceptDto) {
        logger.info("银行通知交易网的文件——文件标识 1：清算失败文件2：会员余额文件 3：出入金流水文件4：会员开销户文件 5：对账不平记录文件");
        Map<String, String> retKeyDict = new HashMap<>();
        retKeyDict.put("backBodyMessages", acceptDto.getMessage());
        // stop 解析消息
        BankMessageAnalysisUtils.spiltMessage_1005(retKeyDict);
        ViewFileDto request = toEntity(retKeyDict);
        Map<String, String> result = new HashMap<>();
        result.put("Reserve", "");
        PABAcceptVo acceptVo = new PABAcceptVo();
        String message = BankMessageSpliceUtils.getSignMessageBody_1005(result);
        acceptVo.setBackBodyMessages(message);

        logger.info("获取银行通知的交易网的文件是数据为{}", JSONObject.toJSONString(request));
        if (FILE_NOT_EXIST.equals(request.getFileName())) {
            logger.info("当日标识文件没有相关流水,文件标识为:{}", request.getFuncFlag());
            if (!request.getFuncFlag().equals("3")) { // 是出入金流水文件，不拦截
                return acceptVo;
            }
        }
        if (!isMathSubject(request)) {
            String decryptionFile = localFile + "/" + request.getFileName();
            sftpUtils.PABDownload(decryptionFile, request.getReserve()); //会把文件下载到本地
        }
        return acceptVo;
    }

    @Override
    public PABFunctionType getType() {
        return PABFunctionType.VIEW_FILE;
    }

    /**
     * 获取匹配的消息体
     *
     * @return
     */
    private Boolean isMathSubject(ViewFileDto viewFileDto) {
        switch (viewFileDto.getFuncFlag()) {
            case "1": //清算失败的文件
                rocketMQUtil.sendMsg(findFileTopic, JSON.toJSONString(viewFileDto), mqFailResultTag, UUID.randomUUID().toString());
                return true;
            case "3": //出入金流水文件
                rocketMQUtil.sendMsg(findFileTopic, JSON.toJSONString(viewFileDto), mqCashTag, UUID.randomUUID().toString());
                return true;
            case "5": //对账不平记录文件
                rocketMQUtil.sendMsg(findFileTopic, JSON.toJSONString(viewFileDto), mqCustDzFailTag, UUID.randomUUID().toString());
                return true;
            default:
                return false;
        }
    }


    private ViewFileDto toEntity(Map<String, String> params) {
        ViewFileDto viewFileDto = new ViewFileDto();
        viewFileDto.setFileName(params.get("FileName"));
        viewFileDto.setFuncFlag(params.get("FuncFlag"));
        viewFileDto.setSupAcctId(supAcctId);
        viewFileDto.setReserve(params.get("Reserve"));
        return viewFileDto;
    }


}
