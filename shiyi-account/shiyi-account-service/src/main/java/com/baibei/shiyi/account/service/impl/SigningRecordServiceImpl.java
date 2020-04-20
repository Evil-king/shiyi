package com.baibei.shiyi.account.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.dao.SigningRecordMapper;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.model.Account;
import com.baibei.shiyi.account.model.SigningRecord;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.account.service.ISigningRecordService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.BankMessageAnalysisUtils;
import com.baibei.shiyi.common.tool.utils.BankMessageSpliceUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementCustomerMsg;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author: uqing
 * @date: 2019/11/01 17:58:10
 * @description: SigningRecord服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class SigningRecordServiceImpl extends AbstractService<SigningRecord> implements ISigningRecordService {

    @Autowired
    private SigningRecordMapper tblTraSigningRecordMapper;

    @Autowired
    private ICustomerFeign customerBase;

    @Value("${rocketmq.customer.topics}")
    private String customerTopic;

    @Value("${rocketmq.settlement.customer.topics}")
    private String settlementCustomerTopic;

    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Autowired
    private IAccountService accountService;

    /**
     * 根据customerNO
     *
     * @param customerNO
     * @return
     */
    @Override
    public SigningRecord findByThirdCustId(String customerNO) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("thirdCustId", customerNO);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        return this.findOneByCondition(condition);
    }

    @Override
    public SigningRecord findByOneCustAcctId(String custAcctId) {
        Condition condition = new Condition(SigningRecord.class);
        condition.createCriteria().andEqualTo("custAcctId", custAcctId);
        return this.findOneByCondition(condition);
    }

    @Override
    public List<SigningRecord> allList() {
        List<String> list = new ArrayList<>();
        list.add(Constants.SigningStatus.SIGNING_CREATE);
        list.add(Constants.SigningStatus.SIGNING_UPDATE);
        list.add(Constants.SigningStatus.SIGNING_DELETE);
        Condition condition = new Condition(SigningRecord.class);
        condition.createCriteria().andIn("funcFlag", list)
                .andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }

    @Override
    public Boolean isOnlyIdCode(String idCode) {
        return isOnlyIdCode(idCode, null);
    }

    @Override
    public Boolean isOnlyIdCode(String idCode, String customerNo) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("idCode", idCode);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));

        // 排除自己以外的身份证是否存在
        if (customerNo != null) {
            criteria.andNotEqualTo("thirdCustId", customerNo);
        }
        List<SigningRecord> signingRecordList = this.findByCondition(condition);
        if (signingRecordList.size() > 0) {
            return Boolean.FALSE;
        }
        if (signingRecordList.size() > 0) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public SigningRecord findByReleaseThirdCustId(String thirdCustId) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andEqualTo("thirdCustId", thirdCustId);
        criteria.andEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        criteria.andEqualTo("flag", new Byte(Constants.Flag.VALID));
        return this.findOneByCondition(condition);
    }

    @Override
    public PABSigningRecordVo signingRecord(PABSigningRecordDto request) {
        log.info("开始签约.....................................");
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(request.getCustomerNo());
        ApiResult<PABCustomerVo> customerResult = customerBase.findCustomerNo(customerNoDto);
        if (customerResult.getCode() != ResultEnum.SUCCESS.getCode()) {
            log.info("根据会员代码获取用户信息结果Code为{},Msg为{}", customerResult.getCode(), customerResult.getMsg());
            throw new ServiceException(customerResult.getMsg());
        }
        PABCustomerVo customer = customerResult.getData();
        SigningRecord signingRecord;
        if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_CREATE)) {
            SigningRecord signingUser = this.findByThirdCustId(request.getCustomerNo());
            if (!this.isOnlyIdCode(request.getIdCode())) {
                throw new ServiceException("不支持身份证重复绑定银行卡");
            }
            if (signingUser != null) {
                throw new ServiceException("当前会员已经签约不需要重复签约");
            }
            SigningRecord releaseThirdCustId = this.findByReleaseThirdCustId(request.getCustomerNo());
            // stop1 当用户重新签约时，把用户上一条签约信息作废
            if (releaseThirdCustId != null) {
                releaseThirdCustId.setFlag(new Byte(Constants.Flag.UNVALID));
                this.update(releaseThirdCustId);
            }
            signingRecord = toEntity(request);
            signingRecord.setId(IdWorker.getId());
            signingRecord.setThirdLogNo(request.getThirdLogNo());  //银行流水号
            signingRecord.setFlag(new Byte(Constants.Flag.VALID));
            log.info("创建签约信息开始:[{}]", JSONObject.toJSONString(signingRecord));
            this.save(signingRecord);
            customer.setSigning(Constants.Flag.VALID); // 设置客户签约
            customer.setModifyTime(new Date()); //修改时间
            customer.setRealName(signingRecord.getAcctName());
            customer.setIdCard(signingRecord.getIdCode());
            // stop 发送消息主题给用户增加信息
            log.info("发送消息给客户服务,消息体为{}", JSONObject.toJSONString(customer));
            rocketMQUtil.sendMsg(customerTopic, JSON.toJSONString(customer), UUID.randomUUID().toString());
            toSettlementCustomerMessage(request);
        } else if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_UPDATE)) {
            signingRecord = this.findByThirdCustId(request.getCustomerNo());
            if (signingRecord == null) {
                throw new ServiceException("会员不存在");
            }
            if (!this.isOnlyIdCode(request.getIdCode(), request.getCustomerNo())) {
                throw new ServiceException("不支持身份证重复绑定银行卡");
            }
            if (!signingRecord.getRelatedAcctId().equals(request.getOldRelatedAcctId())) { // stop1 原出入金账号不等于之前签约的出入金账号，不允许修改
                throw new ServiceException("原出入金账号不匹配");
            }
            request.setFuncFlag(Constants.SigningStatus.SIGNING_UPDATE);
            SigningRecord result = toEntity(request);
            result.setId(signingRecord.getId());
            result.setThirdLogNo(request.getThirdLogNo()); //银行外部流水号
            result.setUpdateTime(new Date());
            log.info("修改会员签约信息开始:[{}]", JSONObject.toJSONString(result));
            this.update(result);
        } else if (request.getFuncFlag().equals(Constants.SigningStatus.SIGNING_DELETE)) {
            signingRecord = this.findByThirdCustId(request.getCustomerNo());
            if (signingRecord == null) {
                throw new ServiceException("删除签约会员失败会员不存在");
            }
            signingRecord.setFuncFlag(new Byte(Constants.SigningStatus.SIGNING_DELETE));
            signingRecord.setUpdateTime(new Date());
            log.info("删除签约信息开始:[{}]", JSONObject.toJSONString(signingRecord));
            signingRecord.setUpdateTime(new Date());
            this.update(signingRecord);
            customer.setSigning(Constants.Flag.UNVALID); // 设置客户解约
            customer.setModifyTime(new Date()); //设置修改时间
            // 发送消息
            log.info("发送消息给客户服务,消息体为{}", JSONObject.toJSONString(customer));
            rocketMQUtil.sendMsg(customerTopic, JSON.toJSONString(customer), UUID.randomUUID().toString());
            toSettlementCustomerMessage(request);
        }
        PABSigningRecordVo pabSigningRecordVo = new PABSigningRecordVo();
        pabSigningRecordVo.setThirdLogNo(request.getThirdLogNo()); //交易网流水号
        log.info("签约成功,并结束");
        return pabSigningRecordVo;
    }

    @Override
    public Boolean isTodaySigning(String customerNo) {
        List<SigningRecord> signing = tblTraSigningRecordMapper.findTodaySigning(customerNo);
        if (signing.size() > 0) {
            // 2、stop 获取签约创建时间的下一个交易日
//            Date date = tradeDayService.getAddNTradeDay(signing.get(0).getCreateTime(), 1);
//            int result = new Date().compareTo(date);
//            if (result >= 0) {
//                return Boolean.TRUE;
//            }
        }
        return Boolean.FALSE;
    }

    @Override
    public List<SigningRecord> findByThirdCustIdList(List<String> customerNos) {
        Condition condition = new Condition(SigningRecord.class);
        Example.Criteria criteria = condition.createCriteria().andIn("thirdCustId", customerNos);
        criteria.andNotEqualTo("funcFlag", new Byte(Constants.SigningStatus.SIGNING_DELETE));
        return findByCondition(condition);
    }


    private SigningRecord toEntity(PABSigningRecordDto signingRecord) {
        SigningRecord entity = new SigningRecord();
        entity.setFuncFlag(new Byte(signingRecord.getFuncFlag())); // 功能标识
        entity.setSupAcctId(signingRecord.getSupAcctId()); // 资金汇总账号
        entity.setCustAcctId(signingRecord.getCustAcctId()); // 会员子账号
        entity.setCustName(signingRecord.getCustName()); // 会员名称
        entity.setThirdCustId(signingRecord.getCustomerNo());//会员代码
        entity.setIdType(signingRecord.getIdType()); // 会员证件类型
        entity.setIdCode(signingRecord.getIdCode()); //会员证件号码
        entity.setRelatedAcctId(signingRecord.getRelatedAcctId()); // 出入金账号
        entity.setAcctFlag(signingRecord.getAcctFlag()); // 账号性质
        if (signingRecord.getFuncFlag().equals(Constants.SigningStatus.SIGNING_CREATE)) { //签约时间
            entity.setCreateTime(new Date());
        }
        entity.setTranType(signingRecord.getTranType()); // 转账方式
        entity.setAcctName(signingRecord.getAcctName()); // 账号名称
        entity.setBankCode(signingRecord.getBankCode()); // 联行户
        entity.setBankName(signingRecord.getBankName()); // 开户行名称
        entity.setOldRelatedAcctId(signingRecord.getOldRelatedAcctId());
        entity.setReserve(signingRecord.getReserve()); // 保留域
        return entity;
    }

    @Override
    public String getBalance(String message) {
        {
            log.info("1019接口入参,message={}", message);
            Map<String, String> result = BankMessageAnalysisUtils.parsingTranMessageString(message);
            String bodyMessages = result.get("backBodyMessages");
            Map<String, String> bodyMessage = new HashMap<>();
            bodyMessage.put("backBodyMessages",bodyMessages);
            BankMessageAnalysisUtils.spiltMessage_1019(bodyMessage);
            String tranFunc = result.get("TranFunc");
            if (tranFunc != null && "1019".equals(tranFunc)) {
                Condition condition = new Condition(SigningRecord.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("custAcctId", bodyMessage.get("CustAcctId"));
                criteria.andNotEqualTo("funcFlag", 3);
                List<SigningRecord> signingRecords = tblTraSigningRecordMapper.selectByCondition(condition);
                if (signingRecords.size() < 1) {
                    return "";
                }
                SigningRecord signingRecord = signingRecords.get(0);
                Account account = accountService.checkAccount(signingRecord.getThirdCustId());
                if (account == null) {
                    return "";
                }
                //组装报文发送到渠道
                HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
                parmaKeyDict.put("TranFunc", tranFunc);
                parmaKeyDict.put("CustAcctId", signingRecord.getCustAcctId());//子账户
                parmaKeyDict.put("ThirdCustId", signingRecord.getThirdCustId());//交易网会员代码
                parmaKeyDict.put("CustName", signingRecord.getCustName());//子账户名称
                parmaKeyDict.put("TotalAmount", account.getTotalBalance().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//总资产
                parmaKeyDict.put("TotalBalance", account.getBalance().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//可用资金
                parmaKeyDict.put("TotalFreezeAmount", account.getFreezingAmount().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN) + "");//冻结资金
                parmaKeyDict.put("TranDate", signingRecord.getCreateTime() + "");//开户日期
                String Message = BankMessageSpliceUtils.getSignMessage(parmaKeyDict);//拼接报文
                return Message;
            }
            return "";
        }
    }

    private void toSettlementCustomerMessage(PABSigningRecordDto signingRecordDto) {
        SettlementCustomerMsg settlementCustomerMsg = new SettlementCustomerMsg();
        /*settlementCustomerMsg.setCustAcctId(signingRecordDto.getCustAcctId());*/
        settlementCustomerMsg.setSignFlag(new Byte(signingRecordDto.getFuncFlag()));
        /*settlementCustomerMsg.setCustName(signingRecordDto.getCustName());*/
        settlementCustomerMsg.setCustomerNo(signingRecordDto.getCustomerNo());
        rocketMQUtil.sendMsg(settlementCustomerTopic, JSON.toJSONString(settlementCustomerMsg), UUID.randomUUID().toString());
    }


}
