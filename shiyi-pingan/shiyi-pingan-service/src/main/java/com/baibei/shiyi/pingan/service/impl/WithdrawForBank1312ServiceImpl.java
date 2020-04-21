package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.cash.feign.base.dto.WithdrawForBank1312Dto;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.client.shiyi.IShiyiWithdrawFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.pingan.enumeration.PABAnswerCodeEnum;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABAcceptVo;
import com.baibei.shiyi.pingan.service.base.AbstractPABAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.ServiceRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 平安1312接口服务记录。
 * 说明：银行发起的出金，我们这边直接置为审核失败（即.银行发起出金，不予理睬，因为生产上很少银行端发起出金的情况）
 */
@Service
public class WithdrawForBank1312ServiceImpl extends AbstractPABAcceptService {
    @Autowired
    private IShiyiWithdrawFeign shiyiWithdrawFeign;

    @Override
    public PABFunctionType getType() {
        return PABFunctionType.WITHDRAW_FOR_BANK;
    }


    @Override
    public PABAcceptVo returnMessage(PABAcceptDto acceptDto) {
        logger.info("监听到1312接口，完整报文为："+acceptDto.getTranMessage());
        WithdrawForBank1312Dto withdrawForBank1312Dto = new WithdrawForBank1312Dto();
        withdrawForBank1312Dto.setMessage(acceptDto.getTranMessage());
        // 调用出入金服务(因为生产上很少银行端发起出金的情况,所以这里不发送事物消息，从而降低复杂程度)
        ApiResult<WithdrawForBank1312Vo> withdrawForBank1312VoApiResult = shiyiWithdrawFeign.withdrawForBank1312(withdrawForBank1312Dto);
        WithdrawForBank1312Vo withdrawForBank1312Vo = withdrawForBank1312VoApiResult.getData();
        PABAcceptVo pabAcceptVo = new PABAcceptVo();
        if (withdrawForBank1312VoApiResult.getCode()!=200) {
            throw new ServiceException("1312调用出入金服务失败");
        }else{
            pabAcceptVo.setRspCode("111111");////这样告诉银行这笔单是需要人工审核的
            pabAcceptVo.setBackBodyMessages(withdrawForBank1312Vo.getMessage());
        }
        return pabAcceptVo;
    }
}
