package com.baibei.shiyi.pingan.service.base;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABAcceptVo;
import com.baibei.shiyi.pingan.service.IPABAcceptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * 平安银行,消息接收类
 */
public abstract class AbstractPABAcceptService implements IPABAcceptService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${cash.supAcctId}")
    protected String supAcctId; //资金汇总账号

    @Value("${cash.Qydm}")
    protected String Qydm; //平台代码


    @Override
    public ApiResult<String> execute(PABAcceptDto response) {
        if (logger.isInfoEnabled()) {
            logger.info("execute {} Function by started,request={}", response.getTranFunc(), JSONObject.toJSONString(response));
        }
        String tranBackMessage = null;
        byte[] messages;

        Map<String, String> parmaKeyDict = new HashMap<>();
        parmaKeyDict.put("TranFunc", Integer.toString(getType().index)); //交易码
        parmaKeyDict.put("ServiceType", "02");//01请求,02应答
        parmaKeyDict.put("Qydm", Qydm);
        parmaKeyDict.put("ThirdLogNo", response.getBankExternalNo()); //设置银行外部流水号
        // 把消息发送给其他的服务
        PABAcceptVo pabAcceptVo = returnMessage(response); //处理结果
        parmaKeyDict.put("RspCode", pabAcceptVo.getRspCode()); //返回消息Code码
        parmaKeyDict.put("RspMsg", pabAcceptVo.getRspMsg()); //返回消息
        try {
            messages = pabAcceptVo.getBackBodyMessages().getBytes("gbk");
            String headMessage = BankMessageSpliceUtils.getYinShangJieSuanTongHeadMessage(messages.length, parmaKeyDict);
            tranBackMessage = headMessage + pabAcceptVo.getBackBodyMessages();
            logger.info("返回的银行的完整报文为{}",tranBackMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ApiResult.success(tranBackMessage);
    }

    /**
     * stop 返回业务报文体
     *
     * @param acceptDto
     * @return
     */
    public abstract PABAcceptVo returnMessage(PABAcceptDto acceptDto);

}
