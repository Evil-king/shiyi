package com.baibei.shiyi.cash.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.base.shiyi.IAccountBase;
import com.baibei.shiyi.account.feign.bean.dto.ChangeAmountDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.SigningRecordVo;
import com.baibei.shiyi.account.feign.client.ISigningRecordFeign;
import com.baibei.shiyi.cash.dao.OrderWithdrawMapper;
import com.baibei.shiyi.cash.feign.base.dto.*;
import com.baibei.shiyi.cash.feign.base.message.Apply1318ConsumerMessage;
import com.baibei.shiyi.cash.feign.base.message.WithdrawDetuchAccountMessage;
import com.baibei.shiyi.cash.feign.base.vo.AddMoneyFlagVo;
import com.baibei.shiyi.cash.feign.base.vo.ExchCashExchangeDetailQuery;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawForBank1312Vo;
import com.baibei.shiyi.cash.feign.base.vo.WithdrawListVo;
import com.baibei.shiyi.cash.model.OrderWithdraw;
import com.baibei.shiyi.cash.service.IOrderWithdrawService;
import com.baibei.shiyi.cash.service.IValidateService;
import com.baibei.shiyi.cash.util.PropertiesVal;
import com.baibei.shiyi.cash.util.Utils;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.BaseReq;
import com.baibei.shiyi.common.tool.api.BaseResp;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;
import com.baibei.shiyi.pingan.feign.client.IPABSendMessageFeign;
import com.baibei.shiyi.user.feign.base.shiyi.ICustomerBase;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import com.github.pagehelper.PageHelper;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
* @author: Longer
* @date: 2019/10/31 14:08:48
* @description: OrderWithdraw服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderWithdrawServiceImpl extends AbstractService<OrderWithdraw> implements IOrderWithdrawService {

    @Autowired
    private OrderWithdrawMapper orderWithdrawMapper;
    @Autowired
    private IValidateService validateService;
    @Autowired
    private PropertiesVal propertiesVal;
    @Autowired
    private ISigningRecordFeign signingRecordFeign;
    @Autowired
    private IPABSendMessageFeign pabSendMessageFeign;
    @Autowired
    private RocketMQUtil rocketMQUtil;
    @Autowired
    private IAccountBase accountBase;
    @Autowired
    private ICustomerFeign customerFeign;
    @Autowired
    private ICustomerBase customerBase;
    private SimpleDateFormat yyyyMMddNoLine= (SimpleDateFormat)DateUtil.yyyyMMddNoLine.get();
    private SimpleDateFormat yyyyMMddHHmmssNoLine= (SimpleDateFormat)DateUtil.yyyyMMddHHmmssNoLine.get();

    @Value("${fuqing.request.url}")
    private String fuqingReqUrl;//福清域名
    @Override
    public OrderWithdrawDto withdrawApplication(OrderWithdrawDto orderWithdrawDto) {
        log.info("用户提现申请，用户编码为:"+orderWithdrawDto.getCustomerNo()+",提现金额为："+orderWithdrawDto.getOrderAmt());
        //相关校验
        validateService.validateWithdraw(orderWithdrawDto);
        //计算手续费
        BigDecimal Fee = Utils.getFee(orderWithdrawDto.getOrderAmt(), propertiesVal.getRate(), propertiesVal.getFee());
        //创建出金订单
        String orderNo = NoUtil.generateOrderNo();
        orderWithdrawDto.setOrderNo(orderNo);
        orderWithdrawDto.setAmount(orderWithdrawDto.getOrderAmt().subtract(Fee));
        orderWithdrawDto.setFee(Fee);
        //创建订单
        createOrder(orderWithdrawDto);
        return orderWithdrawDto;
    }



    @Override
    public List<OrderWithdraw> select1318OrderList() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status",Constants.OrderWithdrawStatus.WITHDRAW_PASS);//提现审核成功
        /*criteria.orEqualTo("status", Constants.OrderWithdrawStatus.WITHDRAW_PASS);//提现审核成功
        criteria.orEqualTo("status", Constants.OrderWithdrawStatus.WITHDRAW_UNPASS);//提现审核失败*/
        criteria.andEqualTo("effective", 1);//查询未处理
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public List<OrderWithdraw> select1317OrderList() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status",Constants.OrderWithdrawStatus.WITHDRAW_UNPASS);//提现审核不通过
        criteria.andEqualTo("orderType","2");
        criteria.andEqualTo("effective", 1);//查询未处理
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public void apply1318(OrderWithdraw orderWithdraw) {
        log.info("正在处理单个用户出金，用户编码为："+orderWithdraw.getCustomerNo()+
                "，订单号为："+orderWithdraw.getOrderNo()+",订单状态为："+orderWithdraw.getStatus()+
                ",订单类型："+orderWithdraw.getOrderType());
        if (Constants.OrderWithdrawStatus.WITHDRAW_PASS.equals(orderWithdraw.getStatus())
                &&Constants.WithdrawOrderType.COMPANY.equals(orderWithdraw.getOrderType())) {//交易网发起
            //调用1318接口
            execute1318(orderWithdraw);
        }

        /*if(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS.equals(orderWithdraw.getStatus())
                && Constants.WithdrawOrderType.BANK.equals(orderWithdraw.getOrderType())){//银行发起
            //调用1317接口
            apply1317(orderWithdraw);
        }*/
    }

    @Override
    public void apply1318Ack(Apply1318ConsumerMessage apiResultApply1318ConsumerMessage) {
        //无论结果怎样，这里都将订单状态改成“渠道处理中”状态（这里需要确保生产者那边，传过来的pabSendVo不为空）
        log.info("apply1318Ack:更新订单状态为“渠道处理中”");
        OrderWithdraw updateEntity = new OrderWithdraw();
        updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_DOING);
        int i = this.safetyUpdateOrderBySelective(updateEntity, apiResultApply1318ConsumerMessage.getOrderNo());
        if (i==0) {
            throw new ServiceException("apply1318Ack:更新出金订单失败");
        }
    }

    @Override
    public List<OrderWithdraw> selectDoingList() {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("status", Constants.OrderWithdrawStatus.WITHDRAW_DOING);//渠道处理中的
        criteria.andEqualTo("orderType", "1");//交易网
        criteria.andEqualTo("effective", 1);//查询未处理
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public List<OrderWithdraw> selectListByStatus(String status) {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.orEqualTo("status", status);//渠道处理中的
        criteria.andEqualTo("orderType", "1");//交易网
        /*criteria.andEqualTo("effective", 1);//查询未处理*/
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public ApiResult<Boolean> apply1325(OrderWithdraw orderWithdraw) {
        log.info("银行1325接口---------------------orderWithdraw={}", JSONObject.toJSONString(orderWithdraw));
        boolean addMoneyFlag=false;//是否需要异步加钱标识(true=需要；false=不需要)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String startTime = simpleDateFormat.format(orderWithdraw.getCreateTime());
        String endTime = simpleDateFormat.format(orderWithdraw.getModifyTime());
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        Map<String, String> retKeyDict;// 返回报文解析结果
        parmaKeyDict.put("TranFunc", "1325");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());//订单号

        parmaKeyDict.put("SupAcctId", orderWithdraw.getSupAcctId());
        parmaKeyDict.put("BeginDate", startTime);//开始日期
        parmaKeyDict.put("EndDate", endTime);//结束日期
        parmaKeyDict.put("PageNum", "1");//第几页
        log.info("请求报文：",JSONObject.toJSONString(parmaKeyDict));
        //直接发送请求1325接口消息
        String message = BankMessageSpliceUtils.getSignMessageBody_1325(parmaKeyDict);
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(1325);
        pabSendDto.setThirdLogNo(orderWithdraw.getOrderNo());
        pabSendDto.setMessage(message);
        ApiResult<PABSendVo> pabSendVoApiResult = pabSendMessageFeign.sendMessage(pabSendDto);
        if (pabSendVoApiResult.getCode()!=ResultEnum.BUSINESS_ERROE.getCode()
                &&pabSendVoApiResult.getCode()!=ResultEnum.SUCCESS.getCode()) {
            log.info("调用1325接口失败：",pabSendVoApiResult.getMsg());
            throw new ServiceException("调用1325接口失败");
        }
        PABSendVo data = pabSendVoApiResult.getData();
        if("000000".equals(data.getRspCode())){
            Map<String, String> respMap = new HashMap();
            respMap.put("backBodyMessages",data.getBackBodyMessages());
            BankMessageAnalysisUtils.spiltMessage_1325(respMap);
            log.info("解析返回完整报文："+data.getBackBodyMessages());
            String ThirdLogNo = "";//交易网流水号(业务系统的订单号)
            String frontLogNo = "";//银行前置流水号
            String TranFlag = "";//记账标志
            String TranStatus = "";//交易状态
            String TranAmount = "";//交易金额
            String CustAcctId = "";//子账号
            String ThirdCustId = "";//会员代码
            String TranDate = "";//交易日期
            String AcctDate = "";//会计日期
            String remark = respMap.get("RspMsg");

            String array = respMap.get("Array");
            String[] arr = array.split("&");
            log.info("arr={}", JSONObject.toJSONString(arr));
            for (int j = 0; j < arr.length; j += 9) {
                ThirdLogNo = arr[j];
                frontLogNo = arr[j + 1];
                TranFlag = arr[j + 2];
                TranStatus = arr[j + 3];
                TranAmount = arr[j + 4];
                CustAcctId = arr[j + 5];
                ThirdCustId = arr[j + 6];
                TranDate = arr[j + 7];
                AcctDate = arr[j + 8];
                if ("2".equals(TranFlag)) { // 1、入金 2、出金
                    log.info("ThirdLogNo={},orderNo={}", ThirdLogNo, orderWithdraw.getOrderNo());
                    if (ThirdLogNo.equals(orderWithdraw.getOrderNo())) {  //判断订单号是否相等
                        OrderWithdraw updateEntity = new OrderWithdraw();
                        updateEntity.setCustAcctId(CustAcctId);
                        updateEntity.setExternalNo(frontLogNo);
                        updateEntity.setEffective((byte) 0);//置为已处理
                        if ("0".equals(TranStatus)) { //判断状态是否是成功的
                            //更新提现订单
                            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
                            int i = this.safetyUpdateOrderBySelective(updateEntity, orderWithdraw.getOrderNo());
                            if (i != 1) {
                                throw new ServiceException("更新订单状态失败");
                            }
                        }
                        if ("1".equals(TranStatus)) {//处理失败的
                            //更新提现订单
                            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
                            int i = this.safetyUpdateOrderBySelective(updateEntity, orderWithdraw.getOrderNo());
                            if (i != 1) {
                                throw new ServiceException("更新订单状态失败");
                            }
                            addMoneyFlag=true;
                        }
                    }
                }
            }
        } else {
            log.error("RspCode={}", data.getRspCode());
            log.error("RspMsg={}", data.getRspCode());
            //更新提现订单
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
            updateEntity.setExternalNo("");
            updateEntity.setEffective((byte) 0);//置为已处理
            int i = this.safetyUpdateOrderBySelective(updateEntity, orderWithdraw.getOrderNo());
            if (i != 1) {
                throw new ServiceException("更新订单状态失败");
            }
            addMoneyFlag=true;
        }
        return ApiResult.success(addMoneyFlag);
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, String status) {
        if (StringUtils.isEmpty(orderNo)) {
            throw new ServiceException("出金单号不能为空");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo",orderNo);
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        OrderWithdraw orderWithdraw = new OrderWithdraw();
        orderWithdraw.setStatus(status);
        orderWithdraw.setModifyTime(new Date());
        orderWithdrawMapper.updateByConditionSelective(orderWithdraw,condition);
    }

    @Override
    public ApiResult<AddMoneyFlagVo> auditOrder(AuditOrderDto auditOrderDto) {
        AddMoneyFlagVo addMoneyFlagVo = new AddMoneyFlagVo();
        boolean addMoneyFlag=false;//是否需要异步加钱。（true=需要；false=不需要）
        if (!Constants.OrderWithdrawStatus.WITHDRAW_PASS.equals(auditOrderDto.getStatus())
                &&!Constants.OrderWithdrawStatus.WITHDRAW_UNPASS.equals(auditOrderDto.getStatus())) {
            throw new ServiceException("参数异常");
        }
        OrderWithdraw orderWithdraw = getByOrderNo(auditOrderDto.getOrderNo());
        if (StringUtils.isEmpty(orderWithdraw)) {
            throw new ServiceException("查不到指定的订单信息");
        }
        //更新状态
        OrderWithdraw updateEntity = new OrderWithdraw();
        updateEntity.setStatus(auditOrderDto.getStatus());
        updateEntity.setReviewer(auditOrderDto.getReviewer());
        /*if(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS.equals(auditOrderDto.getStatus())){
            updateEntity.setEffective(new Byte("0"));//置为已处理
        }*/
        int i = this.safetyUpdateOrderBySelective2(updateEntity,orderWithdraw,auditOrderDto.getOrderNo());
        if(i==0){
            throw new ServiceException("更新出金订单失败");
        }
        if (Constants.OrderWithdrawStatus.WITHDRAW_UNPASS.equals(auditOrderDto.getStatus())) {
            addMoneyFlag=true;
        }
        addMoneyFlagVo.setAddMoneyFlag(addMoneyFlag);
        addMoneyFlagVo.setO(orderWithdraw);
        return ApiResult.success(addMoneyFlagVo);
    }

    @Override
    public OrderWithdraw getByOrderNo(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            throw new ServiceException("未指定出金单号");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("orderNo",orderNo);
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        if (orderWithdrawList.size()>1) {
            throw new ServiceException("出金单号异常");
        }
        return orderWithdrawList.size()==0?null:orderWithdrawList.get(0);
    }

    @Override
    public MyPageInfo<WithdrawListVo> pageList(WithdrawListDto withdrawListDto) {
        if (StringUtils.isEmpty(withdrawListDto.getCurrentPage())) {
            throw new ServiceException("当前页不能为空");
        }
        if (StringUtils.isEmpty(withdrawListDto.getPageSize())) {
            throw new ServiceException("页码不能为空");
        }
        PageHelper.startPage(withdrawListDto.getCurrentPage(), withdrawListDto.getPageSize());
        /*if ("5".equals(withdrawListDto.getStatus()) || "6".equals(withdrawListDto.getStatus())) {
            withdrawListDto.setEffective("0");
        }*/
        List<WithdrawListVo> pageList = orderWithdrawMapper.pageList(withdrawListDto);
//        for (WithdrawListVo withdrawListVo : pageList) {
//            String strName = MobileUtils.changeName(withdrawListVo.getUserName());
//            withdrawListVo.setUserName(strName);
//            /*withdrawListVo.setMobile(MobileUtils.changeMobile(withdrawListVo.getMobile()));*/
//        }
        MyPageInfo<WithdrawListVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }

    @Override
    public BigDecimal sumAmountOfCustomer(String customerNo) {
        return orderWithdrawMapper.sumAmountOfCustomer(customerNo, new Date());
    }

    @Override
    public void accountAckUpdateStatus(WithdrawDetuchAccountMessage withdrawDetuchAccountMessage) {
        log.info("accountAckUpdateStatus：",withdrawDetuchAccountMessage.toString());
        OrderWithdraw orderWithdraw = getByOrderNo(withdrawDetuchAccountMessage.getOrderNo());
        OrderWithdraw updateEntity = new OrderWithdraw();
        if ("success".equals(withdrawDetuchAccountMessage.getResult())) {//扣款成功
            if (!Constants.OrderWithdrawStatus.WITHDRAW_INIT.equals(orderWithdraw.getStatus())) {
                log.info("出金订单状态不是初始化状态，无需更新状态。订单号为：" +
                        withdrawDetuchAccountMessage.getOrderNo()+"，状态为："+orderWithdraw.getStatus());
                throw new ServiceException("出金订单状态不是初始化状态，无需更新状态");
            }
            if(compairAmount(withdrawDetuchAccountMessage.getCustomerNo(),
                    withdrawDetuchAccountMessage.getAmount().add(withdrawDetuchAccountMessage.getFee()))){
                log.info("将出金订单修改成“待审核”",withdrawDetuchAccountMessage.getOrderNo());
                updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_APLLYING);//提现申请中状态（待审核状态）
            } else {//当日提现总额度未超过系统设定上限额度，则无需后台审核，直接将状态置为审核通过
                log.info("将出金订单修改成“审核通过”",withdrawDetuchAccountMessage.getOrderNo());
                updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_PASS);//不需要后台审核(审核通过状态)
                updateEntity.setReviewer("系统");
                updateEntity.setReviewerTime(new Date());
            }
        }else{//扣款失败
            //将订单修改成失败状态
            log.info("将出金订单修改成“失败”",withdrawDetuchAccountMessage.getOrderNo());
            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
            updateEntity.setEffective(new Byte("0"));
            updateEntity.setRemarks("扣除用户余额失败");
        }
        int i = safetyUpdateOrderBySelective2(updateEntity,orderWithdraw,orderWithdraw.getOrderNo());
        if(i==0){
            throw new ServiceException("accountAckUpdateStatus:更新出金订单失败，乐观锁问题");
        }
    }

    @Override
    public WithdrawForBank1312Vo withdrawForBank1312(String message) {
        log.info("1312接口入参,message={}", message);
        //解析报文
        Map<String, String> result = BankMessageAnalysisUtils.parsingTranMessageString(message);
        String tranFunc = result.get("TranFunc");
        if(!"1312".equals(tranFunc)){
            return null;
        }
        String orderNo = NoUtil.generateOrderNo();
        //创建出金订单
        OrderWithdraw orderWithdraw = new OrderWithdraw();
        orderWithdraw.setCustomerNo(result.get("ThirdCustId"));//会员编号
        orderWithdraw.setAccount(result.get("OutAcctId"));//出金账号
        orderWithdraw.setAccountName(result.get("CustName"));//出金账户名
        orderWithdraw.setCustAcctId(result.get("CustAcctId"));//会员子账号
        orderWithdraw.setCreateTime(new Date());
        orderWithdraw.setModifyTime(new Date());
        orderWithdraw.setFlag((byte) 1);//未删除
        orderWithdraw.setEffective((byte) 1);//未处理
        //计算手续费
        BigDecimal Fee = Utils.getFee(new BigDecimal(result.get("TranAmount")), propertiesVal.getRate(), propertiesVal.getFee());
        orderWithdraw.setHandelFee(Fee);//手续费
        orderWithdraw.setExternalNo(result.get("externalNo"));//外部订单号
        orderWithdraw.setOrderType("2");//出金类型
        orderWithdraw.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS);//直接审核不通过
        orderWithdraw.setReviewer("系统");
        orderWithdraw.setRemarks("");
        //出金金额需要扣减手续费
        BigDecimal amount = new BigDecimal(result.get("TranAmount")).subtract(Fee);
        orderWithdraw.setOrderamt(amount);//出金金额
        orderWithdraw.setOrderNo(orderNo);//出金订单号
        orderWithdraw.setId(IdWorker.getId());
        orderWithdrawMapper.insert(orderWithdraw);

        //组装报文发送到渠道
        BankMessageSpliceUtils bf = new BankMessageSpliceUtils();// 拼接报文类
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        parmaKeyDict.put("ThirdLogNo", orderNo);//交易网流水号
        parmaKeyDict.put("TranFunc", "1312");//接口交易码

        parmaKeyDict.put("RspCode", "111111");//这样告诉银行这笔单是需要人工审核的
        parmaKeyDict.put("Reserve", "");

        String Message = bf.getSignMessage(parmaKeyDict);//拼接报文
        WithdrawForBank1312Vo withdrawForBank1312Vo = new WithdrawForBank1312Vo();
        withdrawForBank1312Vo.setTranFunc(tranFunc);
        withdrawForBank1312Vo.setBankExternalNo(result.get("externalNo"));
        withdrawForBank1312Vo.setMessage(Message);
        return withdrawForBank1312Vo;
    }

    @Override
    public void apply1317(OrderWithdraw orderWithdraw) {
        log.info("银行1317接口---------------------");
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(orderWithdraw.getCustomerNo());
        SigningRecordVo signingRecord = signingRecordVoApiResult.getData();
        if (StringUtils.isEmpty(signingRecord)) {
            log.info("1317接口用户签约信息异常，请联系客服，订单号为：",orderWithdraw.getOrderNo());
            throw new ServiceException("1317接口用户签约信息异常，请联系客服");
        }
        //组装数据
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        Map<String, String> retKeyDict;// 返回报文解析结果
        parmaKeyDict.put("TranFunc", "1317");// 接口交易码
        parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());
        parmaKeyDict.put("FrontLogNo", orderWithdraw.getExternalNo());//银行前置流水号
        parmaKeyDict.put("TranWebName", propertiesVal.getTranWebName());//交易网名称
        parmaKeyDict.put("ThirdCustId", signingRecord.getThirdCustId());
        parmaKeyDict.put("IdType", signingRecord.getIdType());
        parmaKeyDict.put("IdCode", signingRecord.getIdCode());
        parmaKeyDict.put("CustAcctId", signingRecord.getCustAcctId());
        parmaKeyDict.put("CustName", signingRecord.getCustName());
        parmaKeyDict.put("SupAcctId", signingRecord.getSupAcctId());
        parmaKeyDict.put("TranStatus", "2");//审核状态(1、通过 2、未通过)
        parmaKeyDict.put("TranType", signingRecord.getTranType());
        parmaKeyDict.put("OutAcctId", signingRecord.getRelatedAcctId());//出金账号
        parmaKeyDict.put("OutAcctIdName", signingRecord.getCustName());//子账户名称
        parmaKeyDict.put("CcyCode", "RMB");
        log.info("1317接口发给银行的金额,amount={}", orderWithdraw.getOrderamt());
        parmaKeyDict.put("TranAmount", orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()).toString());
        parmaKeyDict.put("Reserve", propertiesVal.getWithdrawStr());

        //直接调用1317接口请求渠道
        String message = BankMessageSpliceUtils.getSignMessageBody_1317(parmaKeyDict);
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(1317);
        pabSendDto.setThirdLogNo(orderWithdraw.getOrderNo());
        pabSendDto.setMessage(message);
        ApiResult<PABSendVo> pabSendVoApiResult = pabSendMessageFeign.sendMessage(pabSendDto);
        if (pabSendVoApiResult.getCode()!=ResultEnum.BUSINESS_ERROE.getCode()
                &&pabSendVoApiResult.getCode()!=ResultEnum.SUCCESS.getCode()) {
            throw new ServiceException("请求1317接口失败");
        }
        String backBodyMessages = pabSendVoApiResult.getData().getBackBodyMessages();
        log.info("请求1317接口返回的业务报文为：",backBodyMessages);
        //更新订单
        OrderWithdraw updateOrderEntity = new OrderWithdraw();
        updateOrderEntity.setEffective(new Byte("0"));
        int i = this.safetyUpdateOrderBySelective(updateOrderEntity,orderWithdraw.getOrderNo());
        if(i==0){
            throw new ServiceException("1317更新出金订单失败");
        }
    }

    @Override
    public int safetyUpdateOrderBySelective(OrderWithdraw updateEntity,String orderNo) {
        OrderWithdraw orderWithdraw = this.getByOrderNo(orderNo);
        if (StringUtils.isEmpty(orderWithdraw)) {
            throw new ServiceException("查询不到指定出金单信息");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo",orderNo);
        criteria.andEqualTo("status",orderWithdraw.getStatus());
        criteria.andEqualTo("orderamt",orderWithdraw.getOrderamt());
        criteria.andEqualTo("effective",new Byte(orderWithdraw.getEffective()));
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        updateEntity.setModifyTime(new Date());
        return orderWithdrawMapper.updateByConditionSelective(updateEntity, condition);
    }

    @Override
    public int safetyUpdateOrderBySelective2(OrderWithdraw updateEntity, OrderWithdraw resouceEntity, String orderNo) {
        if (StringUtils.isEmpty(resouceEntity)) {
            throw new ServiceException("查询不到指定出金单信息");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo",orderNo);
        criteria.andEqualTo("status",resouceEntity.getStatus());
        criteria.andEqualTo("orderamt",resouceEntity.getOrderamt());
        criteria.andEqualTo("effective",new Byte(resouceEntity.getEffective()));
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        updateEntity.setModifyTime(new Date());
        return orderWithdrawMapper.updateByConditionSelective(updateEntity, condition);
    }

    @Override
    public void createOrder(OrderWithdrawDto orderWithdrawDto) {
        //TODO 获取用户开户信息
        /*ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(orderWithdrawDto.getCustomerNo());
        SigningRecordVo signingRecordVo = signingRecordVoApiResult.getData();
        if (StringUtils.isEmpty(signingRecordVo)) {
            throw new ServiceException("签约信息异常，请联系客服");
        }*/
        OrderWithdraw orderWithdraw = new OrderWithdraw();
        orderWithdraw.setId(IdWorker.getId());
        orderWithdraw.setCustomerNo(orderWithdrawDto.getCustomerNo());//会员编号
        //设置出金账号
        orderWithdraw.setAccount("");//出金账号(收款账号)
        orderWithdraw.setAccountName(orderWithdrawDto.getAccountName());//出金账户名称
        orderWithdraw.setBankName(orderWithdrawDto.getBankName());//银行用户名称
        orderWithdraw.setCreateTime(new Date());
        orderWithdraw.setModifyTime(new Date());
        orderWithdraw.setFlag((byte) 1);//未删除
        orderWithdraw.setEffective((byte) 1);//未处理

        orderWithdraw.setHandelFee(orderWithdrawDto.getFee());//手续费
        orderWithdraw.setExternalNo("");//外部订单号

        //将订单状态置为：初始化状态（这个状态是不允许后台审核的）,待确认扣减了用户资金时才将状态改成“提现申请中”状态
        orderWithdraw.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_INIT);//状态
        orderWithdraw.setOrderType("1");//出金类型(交易网发起)
        orderWithdraw.setRemarks("");
        orderWithdraw.setHandelFee(Utils.getFee(orderWithdrawDto.getOrderAmt(), propertiesVal.getRate(), propertiesVal.getFee()));//出金手续费
        //出金金额需要扣减手续费
        BigDecimal amount = orderWithdrawDto.getOrderAmt().subtract(orderWithdrawDto.getFee());
        orderWithdraw.setOrderamt(amount);
        orderWithdraw.setOrderNo(orderWithdrawDto.getOrderNo());//出金订单号
        // 设置资金汇总账号
        /*orderWithdraw.setSupAcctId(signingRecordVo.getSupAcctId());*///资金汇总账号
        orderWithdrawMapper.insert(orderWithdraw);
    }

    @Override
    public List<OrderWithdraw> getPeriodOrderList(String batchNo) {
        return orderWithdrawMapper.selectPeriodOrderList(batchNo);
    }

    @Override
    public OrderWithdraw getOrderByExternalNo(String externalNo) {
        Condition condition = new Condition(OrderWithdraw.class);
        condition.setOrderByClause("create_time desc,id");
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(externalNo)) {
            criteria.andEqualTo("externalNo", externalNo);
        }
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList.size() == 0 ? null : orderWithdrawList.get(0);
    }

    @Override
    public int deleteByOrderNo(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            throw new ServiceException("订单号不能为空");
        }
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("orderNo",orderNo);
        int i = orderWithdrawMapper.deleteByCondition(condition);
        return i;
    }

    @Override
    public List<OrderWithdraw> getListByStatus(String status) {
        Condition condition = new Condition(OrderWithdraw.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag",Constants.Flag.VALID);
        criteria.andEqualTo("status",status);
        List<OrderWithdraw> orderWithdrawList = orderWithdrawMapper.selectByCondition(condition);
        return orderWithdrawList;
    }

    @Override
    public List<WithdrawListVo> getWithdrawList(WithdrawListDto withdrawListDto) {
        List<WithdrawListVo> withdrawListVoList = orderWithdrawMapper.pageList(withdrawListDto);
        return withdrawListVoList;
    }

    @Override
    public void operatorReview() {
        //获取状态处于提现申请中的出金订单
        List<OrderWithdraw> listByStatus = getListByStatus(Constants.OrderWithdrawStatus.WITHDRAW_APLLYING);
        List<ChangeAmountDto> changeAmountDtoList = new ArrayList<>();
        for (OrderWithdraw orderWithdraw : listByStatus) {
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setEffective(new Byte("0"));
            updateEntity.setReviewer("系统");
            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_UNPASS);
            int i = safetyUpdateOrderBySelective(updateEntity, orderWithdraw.getOrderNo());
            if(i==0){
                throw new ServiceException("更新出金订单失败");
            }
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
            changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
            changeAmountDto.setReType(Constants.Retype.IN);
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.OPERATEREVIEW_WITHDRAW_BACK.getCode());
            changeAmountDtoList.add(changeAmountDto);
        }
        //加钱
        if(changeAmountDtoList.size()>0){
            ApiResult apiResult = accountBase.changeMoneyList(changeAmountDtoList);
            if (apiResult.hasFail()) {
                log.info("operatorReview 修改用户资金失败：",apiResult.getMsg());
                throw new ServiceException("修改用户资金失败");
            }
        }

    }

    //请求福清逻辑
    @Override
    public void applyFuqing315002(OrderWithdraw orderWithdraw) {
        //获取用户信息
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(orderWithdraw.getCustomerNo());
        ApiResult<CustomerVo> userByCustomerNo = customerBase.findUserByCustomerNo(customerNoDto);
        if (userByCustomerNo.hasFail()||StringUtils.isEmpty(userByCustomerNo.getData())) {
            log.info("applyFuqing315002:获取用户信息失败");
            throw new ServiceException("获取用户信息失败");
        }
        CustomerVo customerVo = userByCustomerNo.getData();
        String signing = customerVo.getSigning();
        if("0".equals(signing)){
            log.info("applyFuqing315002:"+"用户"+orderWithdraw.getCustomerNo()+"未开户");
            throw new ServiceException("用户"+orderWithdraw.getCustomerNo()+"未开户");
        }
        //获取用户实名信息
        CustomerBaseDto customerBaseDto = new CustomerBaseDto();
        customerBaseDto.setCustomerNo(orderWithdraw.getCustomerNo());
        ApiResult<RealnameInfoVo> realnameInfoVoApiResult = customerFeign.realnameInfo(customerBaseDto);
        if (realnameInfoVoApiResult.hasFail()) {
            log.info("applyFuqing315002:获取用户实名信息失败");
            throw new ServiceException("获取用户实名信息失败");
        }
        RealnameInfoVo realnameInfoVo = realnameInfoVoApiResult.getData();//实名信息
        OutGoldenReq outGoldenReq = new OutGoldenReq();
        outGoldenReq.setRequestId(orderWithdraw.getOrderNo());//交易流水号
        String initDate = yyyyMMddNoLine.format(new Date());
        outGoldenReq.setInitDate(Long.parseLong(initDate));//业务发生日期
        outGoldenReq.setExchangeFundAccount(orderWithdraw.getCustomerNo());//交易网资金账号（用户编号）
        outGoldenReq.setMoneyType("CNY");//币种编码
        outGoldenReq.setBisinType("1");//银行业务类型（1：普通；2：冲正；3：重发；4：调账）
        outGoldenReq.setAccountName(realnameInfoVo.getRealname());//银行账户名
        outGoldenReq.setBankAccount(realnameInfoVo.getBankCard());//银行账号
        long withdrawAmt = orderWithdraw.getOrderamt().multiply(new BigDecimal("100")).longValue();
        outGoldenReq.setOccurAmount(withdrawAmt);//发生金额（分为单位）
        //判断是否跨行
        boolean b = BankCardNoUtils.checkBankCardNoIsCITIC(realnameInfoVo.getBankCard());
        log.info("是否跨行判断结果："+b+"，银行卡号为："+orderWithdraw.getOrderNo());
        outGoldenReq.setCrossFlag(b?"0":"1");//是否跨行(0：不跨行；1：跨行)
        String busiDateTime = yyyyMMddHHmmssNoLine.format(new Date());
        outGoldenReq.setBusiDatetime(Long.parseLong(busiDateTime));//业务时间(yyyyMMddHHmmss)
        outGoldenReq.setIdKind("111");//证件类型（身份证）
        outGoldenReq.setIdNo(realnameInfoVo.getIdcard());//证件号
        outGoldenReq.setOutPoundage(0L);//出金手续费（分为单位）
        outGoldenReq.setMemberMainType("2");//会员主体类型(1=机构 2=个人)
        outGoldenReq.setFullName(realnameInfoVo.getRealname());
        //请求福清
        OrderWithdraw updateEntity = new OrderWithdraw();
        try{
            String result = HttpClientUtils.doPostJson(fuqingReqUrl+"/cusOutCash/cusOutCash.json",
                    JSON.toJSONString(outGoldenReq));
            updateEntity.setFuqingWithdrawResp(result);
            ApiResult apiResult = JSONObject.parseObject(result, ApiResult.class);
            log.info("请求福清出金结果："+apiResult.toString());
            String data = JSONObject.toJSONString(apiResult.getData());
            BaseResp baseResp = JSONObject.parseObject(data, BaseResp.class);
            String serialNo = StringUtils.isEmpty(baseResp)?"":baseResp.getSerialNo();
            updateEntity.setExternalNo(serialNo);
        }catch (Exception e){
            log.info("applyFuqing315002,请求福清失败：{}",e);
        }
        //更新订单状态为“渠道处理中”状态
        updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_DOING);
        updateEntity.setFuqingWithdrawReq(JSON.toJSONString(outGoldenReq));
        int i = safetyUpdateOrderBySelective2(updateEntity,orderWithdraw,orderWithdraw.getOrderNo());
        if(i!=1){
            log.info("applyFuqing315002:"+"更新订单失败，订单号为："+orderWithdraw.getOrderNo());
            throw new ServiceException("更新订单失败，订单号为："+orderWithdraw.getOrderNo());
        }
    }

    @Override
    public void applyFuqing315003(OrderWithdraw orderWithdraw) {
        ExchCashExchangeDetail exchCashExchangeDetail = new ExchCashExchangeDetail();
        String initDate = yyyyMMddNoLine.format(orderWithdraw.getModifyTime());
        exchCashExchangeDetail.setInitDate(Long.parseLong(initDate));
        exchCashExchangeDetail.setExchangeFundAccount(orderWithdraw.getCustomerNo());
        exchCashExchangeDetail.setRequestId(orderWithdraw.getOrderNo());
        //请求福清
        String result = HttpClientUtils.doPostJson(fuqingReqUrl+"/cashSerialQuery/cashSerialQuery.json",
                JSON.toJSONString(exchCashExchangeDetail));
        log.info("查询订单号为"+orderWithdraw.getOrderNo()+"的出入金流水结果：",result);
        ApiResult apiResult = JacksonUtil.jsonToBean(result, ApiResult.class);
        if (apiResult.hasFail()) {
            throw new ServiceException("出金订单查询失败，订单号为："+orderWithdraw.getOrderNo());
        }
        Object data = apiResult.getData();
        String s = JSON.toJSONString(data);
        ExchCashExchangeDetailQuery exchCashExchangeDetailQuery =
                JSONObject.parseObject(s, ExchCashExchangeDetailQuery.class);
        List<ExchCashExchangeDetail> items = exchCashExchangeDetailQuery.getItems();
        if (!StringUtils.isEmpty(items)) {
            for (ExchCashExchangeDetail item : items) {
                if ("1".equals(item.getInoutFlag())) {//出金
                    if ("2".equals(item.getCashFlowStatus())) {//成功
                        //更新订单状态为出金成功
                        log.info("applyFuqing315003，订单号："+orderWithdraw.getOrderNo()+"查询结果返回成功，不做处理");
                       /* OrderWithdraw updateEntity = new OrderWithdraw();
                        updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_SUCCESS);
                        updateEntity.setExternalNo(item.getSerialNo());
                        int i = safetyUpdateOrderBySelective2(updateEntity, orderWithdraw, orderWithdraw.getOrderNo());
                        if(i!=1){
                            log.info("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
                            throw new ServiceException("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
                        }*/
                    }
                    if("3".equals(item.getCashFlowStatus())){//失败
                        //更新订单状态为出金失败
                        OrderWithdraw updateEntity = new OrderWithdraw();
                        updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
                        updateEntity.setExternalNo(item.getSerialNo());
                        updateEntity.setFuqingWithdrawResp(s);
                        int i = safetyUpdateOrderBySelective2(updateEntity, orderWithdraw, orderWithdraw.getOrderNo());
                        if(i!=1){
                            log.info("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
                            throw new ServiceException("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
                        }
                        //异步加钱
                        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
                        changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
                        changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
                        changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
                        changeAmountDto.setReType(Constants.Retype.IN);
                        changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_BACK.getCode());
                        //发送加钱消息
                        rocketMQUtil.sendMsg(propertiesVal.getWithdrawAddMoneyTopic(),
                                JacksonUtil.beanToJson(changeAmountDto),changeAmountDto.getOrderNo());
                    }
                }
            }
        }else{
            //更新订单状态为出金失败
            OrderWithdraw updateEntity = new OrderWithdraw();
            updateEntity.setStatus(Constants.OrderWithdrawStatus.WITHDRAW_FAIL);
            updateEntity.setFuqingWithdrawResp("查询不到该订单");
            int i = safetyUpdateOrderBySelective2(updateEntity, orderWithdraw, orderWithdraw.getOrderNo());
            if(i!=1){
                log.info("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
                throw new ServiceException("applyFuqing315003更新订单失败，乐观锁问题，订单号为："+orderWithdraw.getOrderNo());
            }
            //异步加钱
            ChangeAmountDto changeAmountDto = new ChangeAmountDto();
            changeAmountDto.setCustomerNo(orderWithdraw.getCustomerNo());
            changeAmountDto.setOrderNo(orderWithdraw.getOrderNo());
            changeAmountDto.setChangeAmount(orderWithdraw.getOrderamt().add(orderWithdraw.getHandelFee()));
            changeAmountDto.setReType(Constants.Retype.IN);
            changeAmountDto.setTradeType(TradeMoneyTradeTypeEnum.WITHDRAW_BACK.getCode());
            //发送加钱消息
            rocketMQUtil.sendMsg(propertiesVal.getWithdrawAddMoneyTopic(),
                    JacksonUtil.beanToJson(changeAmountDto),changeAmountDto.getOrderNo());
        }
    }


    public void execute1318(OrderWithdraw orderWithdraw){
        log.info("银行1318接口---------------------");
        //获取签约信息
        ApiResult<SigningRecordVo> signingRecordVoApiResult = signingRecordFeign.findByThirdCustId(orderWithdraw.getCustomerNo());
        SigningRecordVo signingRecordVo = signingRecordVoApiResult.getData();
        if (StringUtils.isEmpty(signingRecordVo)) {
            throw new ServiceException("签约信息异常，请联系客服");
        }
        HashMap<String, String> parmaKeyDict = new HashMap<String, String>();// 请求报文所需字段参数
        parmaKeyDict.put("ThirdLogNo", orderWithdraw.getOrderNo());//订单号
        parmaKeyDict.put("TranWebName", propertiesVal.getTranWebName());//交易网名称
        parmaKeyDict.put("ThirdCustId", signingRecordVo.getThirdCustId());//交易网会员代码
        parmaKeyDict.put("IdType", signingRecordVo.getIdType());//会员证件类型
        parmaKeyDict.put("IdCode", signingRecordVo.getIdCode());//会员证件号码
        parmaKeyDict.put("TranOutType", "1");//出金类型 1：会员出金
        parmaKeyDict.put("CustAcctId", signingRecordVo.getCustAcctId());//子账户账号
        parmaKeyDict.put("CustName", signingRecordVo.getCustName());//子账户名称
        parmaKeyDict.put("SupAcctId", signingRecordVo.getSupAcctId());//资金汇总账号
        parmaKeyDict.put("TranType", signingRecordVo.getTranType());//转账方式
        parmaKeyDict.put("OutAcctId", signingRecordVo.getRelatedAcctId());//出金账号
        parmaKeyDict.put("OutAcctIdName", orderWithdraw.getAccountName());//出金账户名称
        parmaKeyDict.put("OutAcctIdBankName", orderWithdraw.getBankName());//出金账号开户行名
        log.info("1318接口出金金额---amount{}", orderWithdraw.getOrderamt());
        parmaKeyDict.put("TranAmount", String.valueOf(orderWithdraw.getOrderamt()));//申请出金金额

        //异步发送请求1318接口消息给平安代理服务。（事物消息）
        String message = BankMessageSpliceUtils.getSignMessageBody_1318(parmaKeyDict);
        PABSendDto pabSendDto = new PABSendDto();
        pabSendDto.setTranFunc(1318);
        pabSendDto.setThirdLogNo(orderWithdraw.getOrderNo());
        pabSendDto.setMessage(message);
        log.info("异步发送事物消息：请求1318接口消息给平安代理服务");
        rocketMQUtil.sendMessageInTransaction2("Groups-Apply1318",
                propertiesVal.getApply1318TxTopic(), JacksonUtil.beanToJson(pabSendDto),
                orderWithdraw.getOrderNo(),JacksonUtil.beanToJson(orderWithdraw));
    }

    /**
     * 判断用户当天提现金额总计是否达到系统限定值，若达到了，则需要后台审核，否则，直接审核通过
     * @param customerNo
     * @param money
     * @return
     */
    public boolean compairAmount(String customerNo,BigDecimal money){
        BigDecimal sumAmount = this.sumAmountOfCustomer(customerNo);
        if(sumAmount == null){
            sumAmount = BigDecimal.ZERO;
        }
        log.info("用户当日成功出金金额,sumAmount={}", sumAmount);
        BigDecimal amount = sumAmount.add(money);
        log.info("对比金额,amount={}", amount);
        if(amount.compareTo(propertiesVal.getWithdrawalAmount()) > -1){
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        OutGoldenReq outGoldenReq = new OutGoldenReq();
        outGoldenReq.setRequestId("8887263283");//交易流水号
        outGoldenReq.setInitDate(20191204L);//业务发生日期
        outGoldenReq.setExchangeId("1001022");//交易网代码
        outGoldenReq.setExchangeFundAccount("5052480052");//交易网资金账号（用户编号）
        outGoldenReq.setMoneyType("CNY");//币种编码
        outGoldenReq.setBisinType("1");//银行业务类型（1：普通；2：冲正；3：重发；4：调账）
        outGoldenReq.setBankProCode("citicyq");//银行产品代码
        outGoldenReq.setAccountName("冯刚");//银行账户名
        outGoldenReq.setBankAccount("6225882012585151");//银行账号
        outGoldenReq.setOccurAmount(100L);//发生金额（分为单位）
        outGoldenReq.setCrossFlag("1");//是否跨行(0：不跨行；1：跨行)
        outGoldenReq.setBusiDatetime(20191204095900L);//业务时间(yyyyMMddHHmmss)
        outGoldenReq.setIdKind("111");//证件类型（身份证）
        outGoldenReq.setIdNo("640221197911012111");//证件号
        outGoldenReq.setOutPoundage(0L);//出金手续费（分为单位）
        outGoldenReq.setMemberMainType("2");//会员主体类型
        outGoldenReq.setFullName("冯刚");
        String result = HttpClientUtils.doPostJson("http://localhost:8888/cusOutCash/cusOutCash.json",
                JSON.toJSONString(outGoldenReq));
        ApiResult apiResult = JacksonUtil.jsonToBean(result, ApiResult.class);
        String s = JSON.toJSONString(apiResult.getData());
        OutGoldenReq outGoldenReq1 = JacksonUtil.jsonToBean(s, OutGoldenReq.class);

        System.out.println(apiResult);
    }
}
