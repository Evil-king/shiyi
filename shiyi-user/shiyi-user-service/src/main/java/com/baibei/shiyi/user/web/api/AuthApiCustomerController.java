package com.baibei.shiyi.user.web.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.AccountVo;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.account.feign.client.CustomerBeanFeign;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.common.tool.enumeration.BankNameCodeEnum;
import com.baibei.shiyi.common.tool.enumeration.BankNameEnum;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.user.common.dto.BankNodeDto;
import com.baibei.shiyi.user.common.dto.OpenAccountDto;
import com.baibei.shiyi.user.common.vo.EmailAndAddressVo;
import com.baibei.shiyi.user.common.vo.QrcodeVo;
import com.baibei.shiyi.user.common.vo.ToOpenAccountVo;
import com.baibei.shiyi.user.feign.bean.dto.BankCardVerificationDto;
import com.baibei.shiyi.user.feign.bean.dto.RealnameVerificationDto;
import com.baibei.shiyi.user.feign.bean.dto.UpdatePasswordDto;
import com.baibei.shiyi.user.feign.bean.vo.BankCardVerificationVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import com.baibei.shiyi.user.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hyc
 * @date: 2019/5/28 16:05
 * @description:
 */
@Slf4j
@Controller
@RequestMapping("/auth/api/customer")
public class AuthApiCustomerController {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICustomerDetailService customerDetailService;
    @Autowired
    private CustomerBeanFeign customerBeanFeign;

    @Autowired
    private AccountFeign accountFeign;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${cash.supAcctId}")
    private String supAcctId;
    @Value("${bankcard_verification_url}")
    private String bankCardVerificationUrl;//储蓄卡验证地址

    @Value("${host}")
    private String host;
    @Value("${path}")
    private String path;
    @Value("${method}")
    private String method;
    @Value("${appcode}")
    private String appcode;

    /**
     * 修改密码
     * @param updatePasswordDto
     * @return
     */
    @PostMapping("/updatePassword")
    @ResponseBody
    public ApiResult<String> updatePassword(@RequestBody @Validated UpdatePasswordDto updatePasswordDto){
        if(!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        return customerService.updatePassword(updatePasswordDto);
    }
    /**
     * 返回2个端的二维码给前端
     * @param
     * @return
     */
    @PostMapping("/createQrCode")
    @ResponseBody
    public ApiResult<QrcodeVo> createQrCode(@RequestBody CustomerNoDto customerNoDto){
        return customerService.createQrCode(customerNoDto.getCustomerNo());
    }
    /**
     * 根据用户编号查询用户信息
     * @param customerNoDto
     * @return
     */
    @PostMapping("/getCustomer")
    @ResponseBody
    public ApiResult<CustomerVo> getCustomer(@RequestBody @Validated CustomerNoDto customerNoDto){
        Customer customer=customerService.findByCustomerNo(customerNoDto.getCustomerNo());
        if(customer==null){
            return ApiResult.badParam("用户不存在");
        }
        CustomerVo customerVo=BeanUtil.copyProperties(customer,CustomerVo.class);

        ApiResult<AccountVo> accountVo=accountFeign.findAccount(customerNoDto);
        if (accountVo.hasSuccess()){
            customerVo.setMoneyBalance(accountVo.getData().getBalance());
            customerVo.setWithdraw(accountVo.getData().getWithdrawableCash());
            customerVo.setFundPassword(accountVo.getData().getFundPassword());
            customerVo.setTotalBalance(accountVo.getData().getTotalBalance());
            customerVo.setFreezingAmount(accountVo.getData().getFreezingAmount());
        }
        ApiResult<CustomerBeanVo> customerBeanVo=customerBeanFeign.getBalance(customerNoDto);
        if(customerBeanVo.hasSuccess()){
            customerVo.setConsumptionBalance(customerBeanVo.getData().getConsumptionBalance());
            customerVo.setExchangeBalance(customerBeanVo.getData().getExchangeBalance());
            customerVo.setShiyiBalance(customerBeanVo.getData().getShiyiBalance());
            customerVo.setExchangeEmpowermentBalance(customerBeanVo.getData().getExchangeEmpowermentBalance());
            customerVo.setPassCardBalance(customerBeanVo.getData().getPassCardBalance());
            customerVo.setMallAccountBalance(customerBeanVo.getData().getMallAccountBalance());
        }

        CustomerDetail customerDetail=customerDetailService.findByCustomerNo(customerNoDto.getCustomerNo());
        if(customerDetail!=null){
            customerVo.setUserPicture(customerDetail.getUserPicture());
        }
        customerVo.setSupAcctId(supAcctId);
        return ApiResult.success(customerVo);
    }

    /**
     * 退出登录
     * @param customerNoDto
     * @return
     */
    @PostMapping("/exitLogin")
    @ResponseBody
    public ApiResult<String> exitLogin(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerService.exitLogin(customerNoDto.getCustomerNo());
    }

    /**
     * 用户购销协议判断
     * @param customerNoDto
     * @return
     */
    @PostMapping("/confirmSign")
    @ResponseBody
    public ApiResult<String> confirmSign(@RequestBody @Validated CustomerNoDto customerNoDto){
        return customerService.confirmSign(customerNoDto);
    }

    @PostMapping("/bankCardVerification")
    @ResponseBody
    public ApiResult<BankCardVerificationVo> bankCardVerification(@RequestBody @Validated BankCardVerificationDto bankCardVerificationDto){
        /*StringBuilder url = new StringBuilder(bankCardVerificationUrl);
        url.append("&").append("cardNo").append("=").append(bankCardVerificationDto.getCardNo());
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

        }*/
        return customerDetailService.bankCardVerification(bankCardVerificationDto.getCardNo());
    }

    @PostMapping("/realnameVerification")
    @ResponseBody
    public ApiResult realnameVerification(@RequestBody @Validated RealnameVerificationDto realnameVerificationDto){
        customerDetailService.realnameVerification(realnameVerificationDto);
        return ApiResult.success();
    }

    @PostMapping("/realnameInfo")
    @ResponseBody
    public ApiResult<RealnameInfoVo> realnameInfo(@RequestBody @Validated CustomerBaseDto customerBaseDto){
        RealnameInfoVo realnameInfoVo = customerDetailService.getRealnameInfo(customerBaseDto.getCustomerNo());
        return ApiResult.success(realnameInfoVo);
    }
    @PostMapping("/openAccount")
    @ResponseBody
    public ApiResult openAccount(@RequestBody @Validated OpenAccountDto openAccountDto){
        if(!EmailUtils.emailFormat(openAccountDto.getEmail())){
            return ApiResult.badParam("请输入正确的邮箱地址");
        }
        return customerService.openAccount(openAccountDto);
    }
    @PostMapping("/getEmailAndAddress")
    @ResponseBody
    public ApiResult<EmailAndAddressVo> getEmailAndAddress(@RequestBody @Validated CustomerNoDto customerNoDto){
        CustomerDetail customerDetail = customerDetailService.findByCustomerNo(customerNoDto.getCustomerNo());
        if(customerDetail==null){
            return ApiResult.badParam("用户不存在");
        }
        EmailAndAddressVo emailAndAddressVo = BeanUtil.copyProperties(customerDetail, EmailAndAddressVo.class);
        return ApiResult.success(emailAndAddressVo);
    }
    @PostMapping("/toOpenAccount")
    @ResponseBody
    public ApiResult<ToOpenAccountVo> toOpenAccount(@RequestBody @Validated CustomerNoDto customerNoDto){
        CustomerDetail customerDetail = customerDetailService.findByCustomerNo(customerNoDto.getCustomerNo());
        if(customerDetail==null){
            return ApiResult.badParam("用户不存在");
        }
        ToOpenAccountVo toOpenAccountVo=new ToOpenAccountVo();
        toOpenAccountVo.setBankName(BankNameEnum.getMsg(BankCardNoUtils.checkBankCardNo(customerDetail.getBankCard())));
        toOpenAccountVo.setBankTotalNode(BankNameCodeEnum.getMsg(BankCardNoUtils.checkBankCardNo(customerDetail.getBankCard())));

        toOpenAccountVo.setIsFlag(1);
        if(StringUtils.isEmpty(toOpenAccountVo.getBankName())||StringUtils.isEmpty(toOpenAccountVo.getBankTotalNode())){
            return ApiResult.badParam("暂不支持该银行卡开户");
        }
        return ApiResult.success(toOpenAccountVo);
    }

    @PostMapping("/getBankNode")
    @ResponseBody
    public ApiResult getBankNode(@RequestBody @Validated BankNodeDto bankNodeDto){
        //获取支行代码
        Map<String, String> querys = new HashMap<String, String>();
        if(!StringUtils.isEmpty(bankNodeDto.getProvince())){
            querys.put("province", bankNodeDto.getProvince());
        }
        if(!StringUtils.isEmpty(bankNodeDto.getCity())){
            querys.put("city", bankNodeDto.getCity());
        }
        if(!StringUtils.isEmpty(bankNodeDto.getKey())){
            querys.put("key", bankNodeDto.getKey());
        }
        querys.put("card",bankNodeDto.getBankCard());
        querys.put("page",bankNodeDto.getCurrentPage());
        String json = BankMessageUtils.getBankCode(host, path, method, appcode, querys);
        JSONObject jsonObject=JSON.parseObject(json);
        if(StringUtils.isEmpty(jsonObject)){
            return ApiResult.error("系统异常,请联系客服");
        }
        Map<String, String> keyMap = new HashMap<String, String>();
        keyMap.put("lName", "lname");
        keyMap.put("bankCode", "bankcode");
        JSONObject jsonObj = JsonUtils.changeJsonObj(jsonObject.getJSONObject("data"),keyMap);
        return ApiResult.success(jsonObj);
    }
    @PostMapping("/getFuQingSingleMem")
    @ResponseBody
    public ApiResult getFuQingSingleMem(@RequestBody CustomerNoDto customerNoDto){
        return customerService.getFuQingSingleMem(customerNoDto);
    }

}
