package com.baibei.shiyi.pingan.service.impl;

import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.pingan.feign.base.dto.PABAcceptDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABAcceptVo;
import com.baibei.shiyi.pingan.service.base.AbstractPABAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: hyc
 * @date: 2019/11/7 10:13
 * @description:
 */
@Service
public class PABGetBalanceServiceImpl extends AbstractPABAcceptService {
    @Autowired
    private ISigningRecordFeign signingRecordFeign;
    @Override
    public PABAcceptVo returnMessage(PABAcceptDto acceptDto) {
        ApiResult<String> message = signingRecordFeign.getBalance(acceptDto.getTranMessage());
        PABAcceptVo pabAcceptVo = new PABAcceptVo();
        pabAcceptVo.setBackBodyMessages(message.getData());
        return pabAcceptVo;
    }

    @Override
    public PABFunctionType getType() {
        return PABFunctionType.FIND_BALANCE;
    }
}
