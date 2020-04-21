package com.baibei.shiyi.pingan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.common.tool.utils.RandomUtils;
import com.baibei.shiyi.pingan.enumeration.PABAnswerCodeEnum;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.service.IPABSendService;
import com.baibei.shiyi.pingan.utils.GetSentServerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PABSendServiceImpl implements IPABSendService {

    private Logger logger = LoggerFactory.getLogger(PABSendServiceImpl.class);

    @Autowired
    private GetSentServerUtils getSentServerUtils;

    @Value("${cash.Qydm}")
    protected String Qydm; //平台代码

    @Override
    public ApiResult<PABSendVo> sendMessage(PABSendDto request) {
        if (logger.isInfoEnabled()) {
            logger.info("execute {} Function by started,request={}", request.getTranFunc(), JSONObject.toJSONString(request));
        }
        long start = System.currentTimeMillis();
        byte[] messageBody;
        Map<String, String> response = new HashMap<>();
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("TranFunc", request.getTranFunc().toString()); // 交易码
        requestMap.put("ServiceType", "01");// 01请求,02应答
        requestMap.put("Qydm", Qydm);
        requestMap.put("ReqCode", StringUtils.isEmpty(request.getRequestCode()) == true ? null : request.getRequestCode());
        requestMap.put("ReqMsg", StringUtils.isEmpty(request.getMessage()) == true ? null : request.getRequestMsg());
        requestMap.put("ThirdLogNo", request.getThirdLogNo()); //交易流水号
        try {
            messageBody = request.getMessage().getBytes("gbk");
            // stop1 报文头
            String headMessage = BankMessageSpliceUtils.getYinShangJieSuanTongHeadMessage(messageBody.length, requestMap);
            // stop2 报文头+报文体——>完整的报文体
            String tranMessage = headMessage + request.getMessage();
            logger.info("当前发送的完整报文为{}", tranMessage);
            String tranBackMessage = getSentServerUtils.sendAndGetMessage(tranMessage);
            logger.info("当前银行返回的报文为{}", tranBackMessage);
            response = BankMessageAnalysisUtils.parsingTranMessageString(tranBackMessage);
            if (!response.get("RspCode").equals(PABAnswerCodeEnum.SUCCESS.getCode())) {
                logger.info("当前失败的报文发送结果为{}", JSONObject.toJSONString(response));
                PABSendVo sendVo = new PABSendVo();
                sendVo.setExternalNo(response.get("externalNo"));
                sendVo.setRspCode(response.get("RspCode"));
                sendVo.setTranBackMessage(tranBackMessage);
                sendVo.setBackBodyMessages(tranBackMessage);
                return ApiResult.build(ResultEnum.BUSINESS_ERROE,sendVo); //错误返回银行报文
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        long eliminateTime = end - start;
        if (logger.isInfoEnabled()) {
            logger.info("execute {} Function by end, used time={},request={},response={}", request.getTranFunc(),
                    eliminateTime, JSONObject.toJSONString(request), JSONObject.toJSONString(response));
        }
        PABSendVo pabSendVo = this.toEntity(response);
        return ApiResult.success(pabSendVo);
    }

    @Override
    public String generateThiredLogNo() {
        StringBuilder thiredLogNoStr = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        thiredLogNoStr.append(dtf.format(LocalDateTime.now()));
        thiredLogNoStr.append(String.format("%04d", RandomUtils.getRandom(1, 100000)));
        return thiredLogNoStr.toString();
    }


    private PABSendVo toEntity(Map<String, String> entity) {
        PABSendVo response = new PABSendVo();
        response.setRspCode(entity.get("RspCode")); //返回码
        response.setRspMsg(entity.get("RspMsg")); // 返回消息
        response.setExternalNo(entity.get("externalNo")); //外部请求流水号
        response.setBackBodyMessages(entity.get("backBodyMessages")); // 返回消息体
        return response;
    }

}
