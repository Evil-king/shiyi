package com.baibei.shiyi.pingan.listener;


import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.service.IPABAcceptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * 监听消息处理
 */
public class MessageHandler implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    Socket mSocket = null;
    boolean mStopped = false;
    BufferedWriter mWriter = null;
    BufferedReader mReader = null;

    private List<IPABAcceptService> pabAcceptService;

    public MessageHandler(Socket pSocket, List<IPABAcceptService> pabAcceptService) {
        logger.info("创建新的消息处理器");
        this.mSocket = pSocket;
        this.pabAcceptService = pabAcceptService;
    }

    @Override
    public void run() {
        char tagChar[];
        tagChar = new char[1024];
        int len;
        String temp;
        String rev = "";
        String message;
        try {
            while (!mStopped && !mSocket.isClosed()) {
                logger.info("处理消息...");
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "GBk"));
                mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "GBK"));

                if ((len = mReader.read(tagChar)) != -1) {
                    temp = new String(tagChar, 0, len);
                    rev += temp;
                    temp = null;
                }
                message = handleMessage1(rev, mWriter);
                if (StringUtils.isEmpty(message)) {
                    message = handleMessage(rev, mWriter);
                }
                mWriter.write(message + "\r\n");
                mWriter.flush();
                closeSocket();
            }
        } catch (Exception e) {
            logger.error("error in MessageHandler -- closing down.");
            e.printStackTrace();
        } finally {
            if (mWriter != null) {
                try {
                    mWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取报文体的内容
     * 进行逻辑处理
     *
     * @param pMessage
     * @param bwriter
     */
    public String handleMessage(String pMessage, BufferedWriter bwriter) throws IOException {
        //todo:此处填写消息处理代码
        logger.info("--------------- 开始处理监管系统的传入的消息为:{} ---------------", pMessage);

        // stop 1  解析报文体
        Map<String, String> retKeyDict = BankMessageAnalysisUtils.parsingTranMessageString(pMessage);
        PABAcceptDto acceptDto = new PABAcceptDto();
        acceptDto.setTranFunc(retKeyDict.get("TranFunc")); // 交易码
        acceptDto.setBankExternalNo(retKeyDict.get("externalNo"));
        acceptDto.setMessage(retKeyDict.get("backBodyMessages"));
        acceptDto.setTranMessage(pMessage);
        logger.info("开始执行当前交易接口");
        IPABAcceptService acceptService = pabAcceptService.stream().filter(function -> function.getType().getIndex() == Integer.valueOf(acceptDto.getTranFunc())).findFirst().orElse(null);
        if (acceptService == null) {
            throw new ServiceException(String.format("当前的交易码%s不支持", acceptDto.getTranFunc()));
        }
        ApiResult<String> acceptVoApiResult = acceptService.execute(acceptDto);
        if (acceptVoApiResult.getCode() != ResultEnum.SUCCESS.getCode()) {
            throw new ServiceException("监听银行的消息处理失败");
        }
        return acceptVoApiResult.getData();
    }

    /**
     * 获取报文体的内容
     * 进行逻辑处理（重写）
     *
     * @param pMessage
     * @param bwriter
     */
    public String handleMessage1(String pMessage, BufferedWriter bwriter) throws IOException {
//        String tranBackMessage = orderWithdrawService.withdrawForBank(pMessage);
//        if (StringUtil.isEmpty(tranBackMessage)) {
//            tranBackMessage = signingRecordService.getAcountBalance(pMessage);
//            return tranBackMessage;
//        }
//        return tranBackMessage;
        return "";
    }

    /**
     * 关闭通讯
     *
     * @throws IOException
     */
    public void closeSocket() throws IOException {
        mSocket.close();
    }
}



