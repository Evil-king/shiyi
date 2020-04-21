package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.cash.feign.base.dto.PABDepositDto;
import com.baibei.shiyi.cash.feign.base.vo.PABDepositVo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiDepositFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.pingan.enumeration.PABAnswerCodeEnum;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABAcceptVo;
import com.baibei.shiyi.pingan.service.base.AbstractPABAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 入金消息接收处理类
 */
@Service
public class PABDepositServiceImpl extends AbstractPABAcceptService {

    @Autowired
    private IShiyiDepositFeign shiyiDepositFeign;


    @Override
    public PABAcceptVo returnMessage(PABAcceptDto acceptDto) {
        // stop 解析银行处理的消息
        Map<String, String> retKeyDict = new HashMap<>();
        PABAcceptVo pabAcceptVo = new PABAcceptVo();
        retKeyDict.put("backBodyMessages", acceptDto.getMessage());
        BankMessageAnalysisUtils.spiltMessage_1310(retKeyDict);
        // stop 验证资金汇总账号是否正确
        PABDepositDto pabDepositDto = toEntity(retKeyDict);
        pabDepositDto.setExternalNo(acceptDto.getBankExternalNo());
        if (!supAcctId.equals(pabDepositDto.getSupAcctId())) {
            throw new ServiceException(String.format("资金汇总账号:%s不正确",pabDepositDto.getSupAcctId()));
        }
        retKeyDict.put("ThirdLogNo", "");
        retKeyDict.put("Reserve", "");
        ApiResult<PABDepositVo> pabDepositVoApiResult = shiyiDepositFeign.deposit(pabDepositDto);

        if (pabDepositVoApiResult.getCode().equals(ResultEnum.SERVIE_FAIL.getCode())) {
            throw new ServiceException("入金服务调用失败");
        }
        // stop 如果入金服务内部报错
        if (pabDepositVoApiResult.getData().equals(ResultEnum.BUSINESS_ERROE.getCode())) {
            pabAcceptVo.setRspCode(PABAnswerCodeEnum.ERR074.getCode());
            if (pabDepositVoApiResult.getMsg() == null) {
                pabAcceptVo.setRspMsg("入金失败"); //设置默认的消息体
            }
            pabAcceptVo.setRspMsg(pabDepositVoApiResult.getMsg());
            String message = BankMessageSpliceUtils.getSignMessageBody_1310(retKeyDict);
            pabAcceptVo.setBackBodyMessages(message);
            return pabAcceptVo;
        }

        if (pabDepositVoApiResult.getData().equals(ResultEnum.ERROR.getCode())) {
            pabAcceptVo.setRspCode(PABAnswerCodeEnum.ERR074.getCode());
            pabAcceptVo.setRspMsg("交易网入金失败请联系交易网");
            String message = BankMessageSpliceUtils.getSignMessageBody_1310(retKeyDict);
            pabAcceptVo.setBackBodyMessages(message);
            return pabAcceptVo;
        }
        PABDepositVo pabDepositVo = pabDepositVoApiResult.getData();
        retKeyDict.put("ThirdLogNo", pabDepositVo.getThirdLogNo());
        // stop 拼接入金报文
        String message = BankMessageSpliceUtils.getSignMessageBody_1310(retKeyDict);
        pabAcceptVo.setBackBodyMessages(message);
        return pabAcceptVo;
    }

    private PABDepositDto toEntity(Map<String, String> params) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        PABDepositDto orderDepositDto = new PABDepositDto();
        orderDepositDto.setSupAcctId(params.get("SupAcctId")); // 资金汇总账号
        orderDepositDto.setCustAcctId(params.get("CustAcctId")); // 会员子账号
        orderDepositDto.setTranAmount(new BigDecimal(params.get("TranAmount"))); // 入金金额
        orderDepositDto.setInAcctId(params.get("InAcctId")); // 入金账号
        orderDepositDto.setInAcctIdName(params.get("InAcctIdName"));
        orderDepositDto.setCcyCode(params.get("CcyCode"));
        try {
            orderDepositDto.setAcctDate(sdf.parse(params.get("AcctDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderDepositDto.setReserve(params.get("Reserve"));
        return orderDepositDto;
    }

    @Override
    public PABFunctionType getType() {
        return PABFunctionType.DEPOSIT;
    }
}
