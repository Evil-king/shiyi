package com.baibei.shiyi.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.user.dao.CustomerDetailMapper;
import com.baibei.shiyi.user.feign.bean.dto.RealnameVerificationDto;
import com.baibei.shiyi.user.feign.bean.vo.BankCardVerificationVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.user.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* @author: hyc
* @date: 2019/05/30 15:41:30
* @description: CustomerDetail服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerDetailServiceImpl extends AbstractService<CustomerDetail> implements ICustomerDetailService {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private CustomerDetailMapper tblCustomerDetailMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${realname_verification_url}")
    private String verificationUrl;//实名认证请求地址
    @Value("${realname_verification_appcode}")
    private String verificationAppCode;//实名认证appCode
    @Value("${realname_verification_limit}")
    private String verificationLimt;//实名认证次数限制
    @Value("${bankcard_verification_url}")
    private String bankCardVerificationUrl;//储蓄卡验证地址
    private SimpleDateFormat sf = (SimpleDateFormat)DateUtil.yyyyMMddWithLine.get();
    private SimpleDateFormat sf2 = (SimpleDateFormat)DateUtil.yyyyMMddHHmmssWithLine.get();

    @Override
    public CustomerDetail findByCustomerNo(String customerNo) {
        Condition condition=new Condition(CustomerDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerDetail> customerDetails=tblCustomerDetailMapper.selectByCondition(condition);
        if(customerDetails.size()>0){
            return customerDetails.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<CustomerDetail> findByIdCard(String idCard) {
        Condition condition=new Condition(CustomerDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("idcard",idCard);
        List<CustomerDetail> customerDetails=tblCustomerDetailMapper.selectByCondition(condition);
        if(customerDetails.size()>0){
            return customerDetails;
        }else {
            return null;
        }
    }

    @Override
    public void realnameVerification(RealnameVerificationDto realnameVerificationDto) {
        log.info("用户实名认证信息：{}",realnameVerificationDto.toString());
        if (StringUtils.isEmpty(realnameVerificationDto.getCustomerNo())) {
            throw new ServiceException("用户编码不能为空");
        }
        String idCardVerification = IdCardUtil.IdentityCardVerification(realnameVerificationDto.getIdcard());
        if (!"correct".equals(idCardVerification)) {
            throw new ServiceException(idCardVerification);
        }
        Map<String, String> birAgeSex = IdCardUtil.getBirAgeSex(realnameVerificationDto.getIdcard());
        String age = birAgeSex.get("age");
        if(Integer.parseInt(age)<18||Integer.parseInt(age)>=65){
            throw new ServiceException("年龄范围必须为18~65周岁");
        }
        //限制用户一天内只允许验证n次
        String key = MessageFormat.format(RedisConstant.REALNAME_VERIFICATION_LIMIT, realnameVerificationDto.getCustomerNo());
        String value = redisUtil.get(key);
        if(!StringUtils.isEmpty(value)&&Integer.parseInt(value)>=Integer.parseInt(verificationLimt)){
            throw new ServiceException("已超过今天的验证次数");
        }
        //限制一个身份证只能绑定到一个用户上
        List<CustomerDetail> byIdCard = findByIdCard(realnameVerificationDto.getIdcard());
        if(!StringUtils.isEmpty(byIdCard)){
            for (CustomerDetail customerDetail : byIdCard) {
                if (!customerDetail.getCustomerNo().equals(realnameVerificationDto.getCustomerNo())) {
                    throw new ServiceException("该身份证已绑定其它账号");
                }
            }
        }
        ApiResult<BankCardVerificationVo> bankCardVerificationVoApiResult = bankCardVerification(realnameVerificationDto.getBankCard());
        if (bankCardVerificationVoApiResult.hasFail()) {
            throw new ServiceException("储蓄卡验证失败");
        }
        BankCardVerificationVo bankCardVerificationVo = bankCardVerificationVoApiResult.getData();
        if (!bankCardVerificationVo.isCashCardFlag()) {
            throw new ServiceException("银行卡输入有误");
        }
        Map<String, String> params = new HashMap<>();
        params.put("idcard", realnameVerificationDto.getIdcard());
        params.put("name", realnameVerificationDto.getRealname());
        params.put("bankcard", realnameVerificationDto.getBankCard());
        /*params.put("mobile",realnameVerificationDto.getBankMobile());*/
        String result="";
        try {
            result = BankMessageUtils.get(verificationAppCode, verificationUrl, params);
            log.info("用户"+realnameVerificationDto.getCustomerNo()+"认证结果："+result);
        } catch (IOException e) {
            log.info("实名认证报错：{}",e);
            throw new ServiceException("实名认证异常");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if("200".equals(code)){
            JSONObject data = jsonObject.getJSONObject("data");
            int rs = data.getIntValue("result");
            if(rs!=0){
                setRedis(key);
                log.info(realnameVerificationDto.getIdcard()+"实名认证失败："+data.getString("msg"));
                throw new ServiceException("实名认证失败");
            }else{//实名认证一致
                CustomerDetail customerDetail = findByCustomerNo(realnameVerificationDto.getCustomerNo());
                if (StringUtils.isEmpty(customerDetail)) {
                    log.info("找不到指定用户信息，用户编码为："+realnameVerificationDto.getCustomerNo());
                    redisUtil.increment(key,1l);
                   throw new ServiceException("找不到指定用户信息");
                }else{
                    //更新信息
                    CustomerDetail updateEntity = new CustomerDetail();
                    updateEntity.setIdcard(realnameVerificationDto.getIdcard());
                    updateEntity.setRealname(realnameVerificationDto.getRealname());
                    updateEntity.setBankCard(realnameVerificationDto.getBankCard());
                    updateEntity.setBankMobile(realnameVerificationDto.getBankMobile());
                    int i = modifySelectiveByCustomerNo(realnameVerificationDto.getCustomerNo(), updateEntity);
                    if(i!=1){
                        throw new ServiceException("更新实名信息失败");
                    }
                    //更新用户是否实名认证标识
                    Customer customer = new Customer();
                    customer.setRealnameVerification("1");
                    int j = customerService.modifySelectiveByCustomerNo(realnameVerificationDto.getCustomerNo(), customer);
                    if(j!=1){
                        throw new ServiceException("更新用户信息失败");
                    }
                    setRedis(key);
                }
            }
        }else{
            log.info(realnameVerificationDto.getIdcard()+"实名认证失败"+jsonObject.getString("msg"));
            setRedis(key);
            throw new ServiceException("实名认证失败");
        }
    }

    @Override
    public ApiResult<BankCardVerificationVo> bankCardVerification(String cardNo) {
        if (StringUtils.isEmpty(cardNo)) {
            return ApiResult.error("银行卡号不能为空");
        }
        StringBuilder url = new StringBuilder(bankCardVerificationUrl);
        url.append("&").append("cardNo").append("=").append(cardNo);
        String requestUrl = url.toString().substring(0, url.length());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(requestUrl).build();
        Response response = null;
        ApiResult apiResult;
        try {
            response = client.newCall(request).execute();
            String rs = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(rs);
            BankCardVerificationVo bankCardVerificationVo = new BankCardVerificationVo();
            String cardType = jsonObject.getString("cardType");
            String bank = jsonObject.getString("bank");
            Boolean validated = jsonObject.getBoolean("validated");
            String stat = jsonObject.getString("stat");
            String key = jsonObject.getString("key");
            bankCardVerificationVo.setCardType(cardType);
            bankCardVerificationVo.setBank(bank);
            bankCardVerificationVo.setValidated(validated);
            bankCardVerificationVo.setStat(stat);
            bankCardVerificationVo.setKey(key);
            apiResult=ApiResult.success(bankCardVerificationVo);
        } catch (Exception e) {
            log.error("储蓄卡验证报错：{}",e);
            apiResult=ApiResult.error();

        }
        return apiResult;
    }

    @Override
    public int modifySelectiveByCustomerNo(String customerNo, CustomerDetail customerDetail) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("用户编码不能为空");
        }
        if(StringUtils.isEmpty(customerDetail)){
            throw new ServiceException("参数异常");
        }
        customerDetail.setModifyTime(new Date());
        Condition condition = new Condition(CustomerDetail.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag","1");
        criteria.andEqualTo("customerNo",customerNo);
        int i = tblCustomerDetailMapper.updateByConditionSelective(customerDetail, condition);
        return i;
    }

    @Override
    public RealnameInfoVo getRealnameInfo(String customerNo) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("用户编码不能为空");
        }
        CustomerDetail customerDetail = findByCustomerNo(customerNo);
        RealnameInfoVo realnameInfoVo = new RealnameInfoVo();
        if (!StringUtils.isEmpty(customerDetail)) {
            realnameInfoVo.setCustomerNo(customerNo);
            realnameInfoVo.setBankCard(customerDetail.getBankCard());
            realnameInfoVo.setBankMobile(customerDetail.getBankMobile());
            realnameInfoVo.setRealname(customerDetail.getRealname());
            realnameInfoVo.setIdcard(customerDetail.getIdcard());
            realnameInfoVo.setBankName(customerDetail.getBankName());
            realnameInfoVo.setBranchName(customerDetail.getBranchName());
        }
        return realnameInfoVo;
    }

    public void setRedis(String key){
        redisUtil.increment(key,1l);
        String format = sf.format(new Date());
        try {
            //设置超时时间
            Date expiredTime = sf2.parse(format + " 23:59:59");
            redisUtil.expireAt(key,expiredTime);
        } catch (ParseException e) {
            log.error("设置台账状态超时时间报错",e);
            e.printStackTrace();
        }
    }
}
