package com.baibei.shiyi.account.web.api;

import com.baibei.shiyi.account.common.dto.ConsignmentDto;
import com.baibei.shiyi.account.common.dto.ConsignmentPageListDto;
import com.baibei.shiyi.account.common.dto.InsertFundPasswordDto;
import com.baibei.shiyi.account.common.dto.SigningDataDto;
import com.baibei.shiyi.account.common.vo.AccountDetailVo;
import com.baibei.shiyi.account.common.vo.ConsignmentListVo;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.FundDetailVo;
import com.baibei.shiyi.account.feign.bean.vo.FundInformationVo;
import com.baibei.shiyi.account.model.CustomerBean;
import com.baibei.shiyi.account.service.IAccountService;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.utils.BeanUtil;
import com.baibei.shiyi.common.tool.utils.IDCardUtils;
import com.baibei.shiyi.common.tool.utils.MobileUtils;
import com.baibei.shiyi.content.feign.base.IContentBase;
import com.baibei.shiyi.content.feign.bean.dto.ConsignmentListDto;
import com.baibei.shiyi.content.feign.bean.vo.ConsignmentGoodsVo;
import com.baibei.shiyi.content.feign.bean.vo.ConsignmentVo;
import com.baibei.shiyi.user.feign.bean.dto.ForgetPasswordDto;
import com.baibei.shiyi.user.feign.bean.dto.UpdatePasswordDto;
import com.baibei.shiyi.user.feign.client.shiyi.ICustomerFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hyc
 * @date: 2019/5/28 16:14
 * @description:
 */
@Controller
@RequestMapping("/auth/api/account")
public class AuthApiAccountController {
    @Autowired
    private IAccountService accountService;

    @Autowired
    private ICustomerBeanService customerBeanService;

    @Autowired
    private IContentBase contentBase;

    @Autowired
    private ICustomerFeign customerFeign;

    @Value("${signing.personalOrEnterprise}")
    private String signingEnterprise;



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
        //密码校验
        if(!(updatePasswordDto.getNewPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        return accountService.updatePassword(updatePasswordDto);
    }
    /**
     * 签约存入数据
     * @param insertPasswordDto
     * @return
     */
    @PostMapping("/signingData")
    @ResponseBody
    public ApiResult<String> signingData(@RequestBody @Validated SigningDataDto insertPasswordDto){
        ApiResult result=new ApiResult();
        if(!MobileUtils.isMobileNO(insertPasswordDto.getMobile())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        //判断签约者年龄
        if(Constants.Identity.PERSONAL.equals(insertPasswordDto.getIdentity())){
            Integer age=IDCardUtils.IdNOToAge(insertPasswordDto.getCertificateNo());
            if(age>=65||age<18){
                result.setCode(ResultEnum.SIGNING_AGE_ERROR.getCode());
                result.setMsg(ResultEnum.SIGNING_AGE_ERROR.getMsg());
                return result;
            }
        }
        //密码校验
        if(!(insertPasswordDto.getPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        if(Constants.Identity.ENTERPRISE.equals(insertPasswordDto.getIdentity())&&("false".equals(signingEnterprise))){
            return ApiResult.badParam("当前暂不支持企业用户签约");
        }
        InsertFundPasswordDto passwordDto=BeanUtil.copyProperties(insertPasswordDto,InsertFundPasswordDto.class);
        return accountService.insertFundPassword(passwordDto);
    }

    /**
     * 资金信息
     * @param customerNoDto
     * @return
     */
    @PostMapping("/fundInformation")
    @ResponseBody
    public ApiResult<FundInformationVo> fundInformation(@RequestBody @Validated CustomerNoDto customerNoDto){
        return accountService.fundInformation(customerNoDto);
    }

    @PostMapping("/fundDetail")
    @ResponseBody
    public ApiResult<FundDetailVo> fundDetail(@RequestBody @Validated CustomerNoDto customerNoDto){
        return accountService.fundDetail(customerNoDto);
    }
    /**
     * pc端获取账户信息
     * @param customerNoDto
     * @return
     */
    @PostMapping("/getAccountDetail")
    @ResponseBody
    public ApiResult<AccountDetailVo> getAccountDetail(@RequestBody @Validated CustomerNoDto customerNoDto){
        return accountService.getAccountDetail(customerNoDto);
    }
    /**
     * 设置资金密码
     */
    @PostMapping("/insertFundPassword")
    @ResponseBody
    public ApiResult  insertFundPassword(@RequestBody @Validated InsertFundPasswordDto fundPasswordDto){
        //密码校验
        if(!(fundPasswordDto.getPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        return accountService.insertFundPassword(fundPasswordDto);
    }
    @PostMapping("/forgetPassword")
    @ResponseBody
    public ApiResult<String> forgetPassword(@RequestBody @Validated ForgetPasswordDto forgetPasswordDto){
        //密码校验
        if(!(forgetPasswordDto.getPassword().matches("\\d{6}"))){
            return ApiResult.badParam("请输入6位纯数字密码");
        }
        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }

        ApiResult<String> result=new ApiResult<String>();
        //通过手机号查询到交易商账号
        ApiResult<String> customerNoResult=accountService.findCustomerNoByMobile(forgetPasswordDto.getMobile());
        if(!customerNoResult.hasSuccess()){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        if(!customerNoResult.getData().equals(forgetPasswordDto.getCustomerNo())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        //通过公共服务校验短信验证码
        if(!accountService.checkMobileVerificationCode(forgetPasswordDto.getMobile(),forgetPasswordDto.getMobileVerificationCode(),"5")){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        //存在则进行重置密码操作
        result=accountService.forgetPassword(customerNoResult.getData(),forgetPasswordDto.getPassword());
        return result;
    }

    @PostMapping("/consignmentList")
    @ResponseBody
    public ApiResult consignmentList(@RequestBody ConsignmentPageListDto consignmentDto){
        ConsignmentListVo consignmentListVo = new ConsignmentListVo();
        // 获取用户红豆数量
        ApiResult<CustomerBean> balanceByType = customerBeanService.getBalanceByType(consignmentDto.getCustomerNo(), Constants.BeanType.EXCHANGE);
        if(balanceByType.hasSuccess()){
            consignmentListVo.setPrice(balanceByType.getData().getBalance());
        }
        //组装寄售商品
        ConsignmentListDto consignmentListDto = new ConsignmentListDto();
        consignmentListDto.setCustomerNo(consignmentDto.getCustomerNo());
        ApiResult<List<ConsignmentGoodsVo>> listApiResult = contentBase.consignmentList(consignmentListDto);
        if(listApiResult.hasSuccess()){
            consignmentListVo.setConsignmentGoodsVoList(listApiResult.getData());
        }
        //寄售时间、寄售最低手续费、手续费费率
        ApiResult<ConsignmentVo> timeResult = contentBase.consignmentConfiguration();
        if(timeResult.hasSuccess()){
            ConsignmentVo consignmentVo = timeResult.getData();
            String time = consignmentVo.getStartTime()+"-"+consignmentVo.getEndTime();
            consignmentListVo.setConsignmentTime(time);
            consignmentListVo.setFee(consignmentVo.getFee());
            consignmentListVo.setFees(consignmentVo.getFees());
        }
        return ApiResult.success(consignmentListVo);
    }



    @PostMapping("/consignmentApplication")
    @ResponseBody
    public ApiResult consignmentApplication(@RequestBody ConsignmentDto consignmentDto){

        return accountService.consignmentApplication(consignmentDto);
    }
}
