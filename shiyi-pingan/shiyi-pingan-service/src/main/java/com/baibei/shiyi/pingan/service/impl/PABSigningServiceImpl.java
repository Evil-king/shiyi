package com.baibei.shiyi.pingan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 平安签约接口服务记录
 */
@Service
public class PABSigningServiceImpl extends AbstractPABAcceptService {

    @Autowired
    private ISigningRecordFeign signingRecordFeign;

    @Override
    public PABFunctionType getType() {
        return PABFunctionType.SIGNING;
    }

    /**
     * 根据标识返回中文描述
     *
     * @param funcFlag
     * @return
     */
    private String getFuncFlagText(String funcFlag) {
        switch (funcFlag) {
            case Constants.SigningStatus.SIGNING_CREATE:
                return "签约";
            case Constants.SigningStatus.SIGNING_UPDATE:
                return "修改签约信息";
            case Constants.SigningStatus.SIGNING_DELETE:
                return "解约";
            default:
                return null;
        }

    }

    @Override
    public PABAcceptVo returnMessage(PABAcceptDto acceptDto) {
        Map<String, String> backMessage = new HashMap<>();
        PABAcceptVo pabAcceptVo = new PABAcceptVo();
        // stop1 解析银行的报文
        backMessage.put("backBodyMessages", acceptDto.getMessage());
        BankMessageAnalysisUtils.spiltMessage_1303(backMessage);
        // stop2 调用签约接口,根据签约的接口
        PABSigningRecordDto recordDto = toEntityByHashMapRequest(backMessage); //解析Map并转换对应的对象
        recordDto.setThirdLogNo(acceptDto.getBankExternalNo());
        logger.info("签约开始");
        ApiResult<PABSigningRecordVo> pabSigningRecordDto = signingRecordFeign.signingRecord(recordDto);
        logger.info("签约结果{}", JSONObject.toJSONString(pabSigningRecordDto));
        if (pabSigningRecordDto.getCode().equals(ResultEnum.SERVIE_FAIL.getCode())) {
            throw new ServiceException("签约服务调用失败");
        }
        //stop3 根据返回结果,拼接业务报文体
        Map<String, String> result = new HashMap<>();
        result.put("ThirdLogNo", "");
        result.put("Reserve", "");
        if (pabSigningRecordDto.getCode().equals(ResultEnum.BUSINESS_ERROE.getCode())) {
            pabAcceptVo.setRspCode(PABAnswerCodeEnum.ERR074.getCode());
            if (pabSigningRecordDto.getMsg() == null) {
                pabAcceptVo.setRspMsg(getFuncFlagText(recordDto.getFuncFlag()) + "失败"); //设置默认的消息体
            }
            pabAcceptVo.setRspMsg(pabSigningRecordDto.getMsg());
            String message = BankMessageSpliceUtils.getSignMessageBody_1303(result);
            pabAcceptVo.setBackBodyMessages(message);
            return pabAcceptVo;
        }
        //  stop 如果是系统错误
        if(pabSigningRecordDto.getCode().equals(ResultEnum.ERROR.getCode())){
            pabAcceptVo.setRspCode(PABAnswerCodeEnum.ERR074.getCode());
            pabAcceptVo.setRspMsg("交易网请求失败请联系交易网");
            String message = BankMessageSpliceUtils.getSignMessageBody_1303(result);
            pabAcceptVo.setBackBodyMessages(message);
            return pabAcceptVo;
        }
        // stop4 成功签约,拼接有参数的业务报文体
        PABSigningRecordVo pabSigningRecordVo = pabSigningRecordDto.getData();
        result.put("ThirdLogNo", pabSigningRecordVo.getThirdLogNo());
        result.put("Reserve", "");
        String message = BankMessageSpliceUtils.getSignMessageBody_1303(result);
        pabAcceptVo.setRspCode(PABAnswerCodeEnum.SUCCESS.getCode());
        pabAcceptVo.setBackBodyMessages(message);
        return pabAcceptVo;
    }


    private PABSigningRecordDto toEntityByHashMapRequest(Map<String, String> params) {
        PABSigningRecordDto signingRecordDto = new PABSigningRecordDto();
        signingRecordDto.setFuncFlag(params.get("FuncFlag")); //签约标识
        signingRecordDto.setSupAcctId(params.get("SupAcctId")); // 资金汇总账号
        signingRecordDto.setCustAcctId(params.get("CustAcctId")); // 会员子账号
        signingRecordDto.setCustName(params.get("CustName")); // 会员名称
        signingRecordDto.setCustomerNo(params.get("ThirdCustId")); // 会员代码
        signingRecordDto.setIdType(params.get("IdType")); // 会员证件类型
        signingRecordDto.setIdCode(params.get("IdCode")); // 会员证件号码
        signingRecordDto.setRelatedAcctId(params.get("RelatedAcctId")); // 出入金代码
        signingRecordDto.setAcctFlag(params.get("AcctFlag")); // 账号性质
        signingRecordDto.setTranType(params.get("TranType")); //转账方式
        signingRecordDto.setAcctName(params.get("AcctName")); // 账号名称
        signingRecordDto.setBankName(params.get("BankName")); //开户行名称
        signingRecordDto.setBankCode(params.get("BankCode")); //联行号
        signingRecordDto.setOldRelatedAcctId(params.get("OldRelatedAcctId")); // 原入金账号
        signingRecordDto.setReserve(params.get("Reserve")); //保留域
        return signingRecordDto;
    }
}
