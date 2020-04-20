package com.baibei.shiyi.cash.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.cash.dao.SignInBackMapper;
import com.baibei.shiyi.cash.feign.base.dto.SignInBackDto;
import com.baibei.shiyi.cash.feign.base.vo.SignInBackVo;
import com.baibei.shiyi.cash.model.SignInBack;
import com.baibei.shiyi.cash.service.ISignInBackService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.PABFunctionType;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.client.IPABSendMessageFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: uqing
 * @date: 2019/11/01 10:28:13
 * @description: SignInBack服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class SignInBackServiceImpl extends AbstractService<SignInBack> implements ISignInBackService {

    @Autowired
    private SignInBackMapper tblCashSignInBackMapper;

    @Autowired
    private IPABSendMessageFeign pabSendMessageFeign;


    @Override
    public SignInBackVo signInOrBack(SignInBackDto signInBackDto) {
        SignInBackVo signInBackVo = new SignInBackVo();
        // stop1 发送结果签约报文
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(PABFunctionType.SIGN_IN_BACK.index);//设置交易码
        pabSendDto.setMessage(pabMessage(signInBackDto));
        pabSendDto.setThirdLogNo(signInBackDto.getSignNo()); //请求方流水号

        //消息体
        ApiResult<PABSendVo> result = pabSendMessageFeign.sendMessage(pabSendDto);
        // stop2  获取发送结果
        PABSendVo pabSendVo = result.getData();
        if (ResultEnum.SUCCESS.getCode() != result.getCode()) {
            log.info("当前签到或签退失败,失败结果为{}", JSONObject.toJSONString(pabSendVo));
            Map<String, String> errorMsg = BankMessageAnalysisUtils.parsingTranMessageString(pabSendVo.getBackBodyMessages());
            throw new ServiceException(String.format("签到或签退失败:状态码为%s,消息为%s", errorMsg.get("RspCode"), errorMsg.get("RspMsg")));
        }

        Map<String, String> bodyMessage = new HashMap<>();
        bodyMessage.put("backBodyMessages", pabSendVo.getBackBodyMessages());
        // stop3 解析成功报文
        BankMessageAnalysisUtils.spiltMessage_1030(bodyMessage);
        signInBackDto.setExternalNo(bodyMessage.get("FrontLogNo")); //设置银行的前置流水号
        SignInBack signInBack = toEntity(signInBackDto);
        log.info("保存签到或签退信息[{}]", JSONObject.toJSONString(signInBack));
        this.save(signInBack);
        // stop 拼接返回对象
        signInBackVo.setFrontLogNo(bodyMessage.get("FrontLogNo"));
        signInBackVo.setReserve(bodyMessage.get("Reserve"));
        return signInBackVo;
    }

    @Override
    public Boolean isToDaySignInBack(String status) {
        log.info("status={}", status);
        List<SignInBack> result = tblCashSignInBackMapper.findSignInBackByToday(status, LocalDate.now());
        log.info("result={}", result.size());
        if (result.size() > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public SignInBackDto lastSignStatus() {
        Condition condition = new Condition(SignInBack.class);
        condition.orderBy("createTime").desc();
        condition.createCriteria().andEqualTo("status", Constants.SuccessOrFail.SUCCESS);

        List<SignInBack> signInBackList = this.findByCondition(condition);

        if (!signInBackList.isEmpty()) {
            SignInBack signInBack = signInBackList.get(0);
            SignInBackDto signInBackDto =new SignInBackDto();
            signInBackDto.setSignNo(signInBack.getSignNo());
            signInBackDto.setExternalNo(signInBack.getExternalNo());
            signInBackDto.setFuncFlag(signInBack.getSignStatus());
            signInBackDto.setSignDate(signInBack.getCreateTime());
            signInBackDto.setReserve(signInBack.getReserve());
            return signInBackDto;
        }
        return null;
    }


    private String pabMessage(SignInBackDto signInBackDto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Map<String, String> requestResult = new HashMap<>();
        requestResult.put("FuncFlag", signInBackDto.getFuncFlag()); // 提交标识
        requestResult.put("TxDate", formatter.format(signInBackDto.getSignDate()));
        requestResult.put("Reserve", signInBackDto.getReserve()); //返回业务处理
        String bodyMessage = BankMessageSpliceUtils.getSignMessageBody_1330(requestResult);
        log.info("当前签到的拼接的报文为{}", bodyMessage);
        return bodyMessage;
    }

    /**
     * 保存签约信息
     *
     * @param signInBackDto
     */
    private SignInBack toEntity(SignInBackDto signInBackDto) {
        // stp1 保存签约信息
        SignInBack signInBack = new SignInBack();
        signInBack.setId(IdWorker.getId());
        signInBack.setCreateTime(new Date());
        signInBack.setFlag(new Byte(Constants.Flag.VALID));
        signInBack.setSignStatus(signInBackDto.getFuncFlag());
        signInBack.setTxDate(signInBackDto.getSignDate());
        signInBack.setSignNo(signInBackDto.getSignNo());
        signInBack.setReserve(signInBackDto.getReserve()); //保留域
        signInBack.setExternalNo(signInBackDto.getExternalNo()); //外部流水号
        signInBack.setStatus(Constants.SuccessOrFail.SUCCESS);
        return signInBack;
    }


}
