package com.baibei.shiyi.user.web.api;

import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.core.aop.NoRepeatSubmit;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.utils.MobileUtils;
import com.baibei.shiyi.common.tool.utils.VerificationCodeUtils;
import com.baibei.shiyi.user.common.dto.ExternalRegisterDto;
import com.baibei.shiyi.user.common.dto.MobileLoginDto;
import com.baibei.shiyi.user.common.dto.ToRegisterVo;
import com.baibei.shiyi.user.common.vo.CustomerTokenVo;
import com.baibei.shiyi.user.common.vo.QrcodeVo;
import com.baibei.shiyi.user.feign.bean.dto.ForgetPasswordDto;
import com.baibei.shiyi.user.feign.bean.dto.LoginDto;
import com.baibei.shiyi.user.feign.bean.dto.MobileDto;
import com.baibei.shiyi.user.feign.bean.dto.RegisterDto;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author: hyc
 * @date: 2019/5/24 9:49 AM
 * @description:
 */
@RestController
@RequestMapping("/api/customer")
public class ApiCustomerController {
    @Value("${invitaionCode}")
    private String invitaionCode;
    @Autowired
    private ICustomerService CustomerService;
    //IOS二维码
    @Value("${IOSQrcode}")
    private String IOSQrcode;

    //是否必填邀请码
    @Value("${isMust}")
    private String isMust;

    //安卓二维码
    @Value("${AndroidQrcode}")
    private String AndroidQrcode;

    @Autowired
    private RedisUtil redisUtil;
    /**
     * 返回2个端的二维码给前端
     * @param
     * @return
     */
    @PostMapping("/createQrCode")
    @ResponseBody
    public ApiResult<QrcodeVo> createQrCode(){
        QrcodeVo qrcodeVo=new QrcodeVo();
        qrcodeVo.setIosQrcode(IOSQrcode);
        qrcodeVo.setAndroidQrcode(AndroidQrcode);
        return ApiResult.success(qrcodeVo);
    }
    @PostMapping("/toRegister")
    @ResponseBody
    public ApiResult<ToRegisterVo> toRegister(){
        ToRegisterVo toRegisterVo=new ToRegisterVo();
        toRegisterVo.setIsMust(isMust);
        return ApiResult.success(toRegisterVo);
    }

    /**
     * 外部注册接口
     * @param externalRegisterDto
     * @return
     */
    @PostMapping("/externalRegister")
    @ResponseBody
    @NoRepeatSubmit
    public ApiResult externalRegister(@RequestBody  @Validated ExternalRegisterDto externalRegisterDto){
        if(!(MobileUtils.isMobileNO(externalRegisterDto.getMobile())&&MobileUtils.isMobileNO(externalRegisterDto.getRecommenderMobile()))){
            return ApiResult.badParam("手机号格式不正确");
        }
        Customer registerCustomer = CustomerService.findByMobile(externalRegisterDto.getMobile());
        if(!StringUtils.isEmpty(registerCustomer)){
            return ApiResult.build(ResultEnum.USER_EXIST.getCode(),ResultEnum.USER_EXIST.getMsg(),null);
        }
        Customer RecommenderCustomer = CustomerService.findByMobile(externalRegisterDto.getRecommenderMobile());
        if(StringUtils.isEmpty(RecommenderCustomer)){
            return ApiResult.badParam("该推荐人不存在");
        }
        RegisterDto registerDto =new RegisterDto();
        registerDto.setMobile(externalRegisterDto.getMobile());
        //密码取手机号6位数
        registerDto.setPassword(externalRegisterDto.getMobile().substring(externalRegisterDto.getMobile().length()-6,externalRegisterDto.getMobile().length()));
        registerDto.setRegisterSource(externalRegisterDto.getRegisterSource());
        ApiResult<CustomerTokenVo> registerApiresult = CustomerService.register(registerDto, RecommenderCustomer);
        if(!registerApiresult.hasSuccess()){
            return registerApiresult;
        }
        return ApiResult.success();
    }
    /**
     * 注册
     * @param registerDto
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    @NoRepeatSubmit
    public ApiResult<CustomerTokenVo> register(@RequestBody  @Validated RegisterDto registerDto){
        ApiResult result=new ApiResult();
        //判断手机号是否被注册过了
        if(CustomerService.checkMobileRegistered(registerDto.getMobile())){
            result.setCode(ResultEnum.USER_EXIST.getCode());
            result.setMsg(ResultEnum.USER_EXIST.getMsg());
            return result;
        }
        if(!(registerDto.getPassword().length()<=20 && registerDto.getPassword().length()>=6)){
            return ApiResult.badParam("请设置正确的密码");
        }
        if(!registerDto.getPassword().equals(registerDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        //通过公共服务校验短信验证码
        if(!CustomerService.checkMobileVerificationCode(registerDto.getMobile(),registerDto.getMobileVerificationCode(),"1")){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        if(StringUtils.isEmpty(registerDto.getInvitationCode())){
            //如果未输入邀请码则设置默认邀请码
            registerDto.setInvitationCode(invitaionCode);
        }
        //通过判断邀请码确定直属推荐人的ID
        Customer customer=CustomerService.checkUsername(registerDto.getInvitationCode());
        if(null==customer){
            return ApiResult.badParam("请输入正确的邀请码");
        }
        ApiResult<CustomerTokenVo> apiResult=CustomerService.register(registerDto,customer);
        return apiResult;
    }
    @PostMapping("/login")
    @ResponseBody
    public ApiResult<CustomerTokenVo> login(@RequestBody @Validated LoginDto loginDto, HttpServletRequest request){
        ApiResult result=new ApiResult();
        //通过交易账号判断是交易商编码或者是手机号，再通过不同的字段使用不同的方法查询用户的信息
        Customer customer=CustomerService.checkUsername(loginDto.getUsername());
        if(null==customer){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        if(customer.getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_MALL_LOGIN.getCode())!=-1){
            //用户状态如果存在限制登录状态
            result.setCode(ResultEnum.LIMIT_LOGIN.getCode());
            result.setMsg(ResultEnum.LIMIT_LOGIN.getMsg());
            return result;
        }
        //从redis查看当前账户是否输入密码错误达到上限
        Boolean flag=CustomerService.checkLoginFailCount(customer.getCustomerNo());
        if(flag){
            result.setCode(ResultEnum.USER_LOCK.getCode());
            result.setMsg(ResultEnum.USER_LOCK.getMsg());
            return result;
        }
        //
        ApiResult<CustomerTokenVo> apiResult=CustomerService.login(customer,loginDto,request);
        return  apiResult;
    }
    @PostMapping("/mobileLogin")
    @ResponseBody
    public ApiResult<CustomerTokenVo> mobileLogin(@RequestBody @Validated MobileLoginDto mobileLoginDto){
        if(!MobileUtils.isMobileNO(mobileLoginDto.getMobile())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        Customer customer=CustomerService.checkUsername(mobileLoginDto.getMobile());
        ApiResult result=new ApiResult();
        if(customer.getCustomerStatus().indexOf(CustomerStatusEnum.LIMIT_MALL_LOGIN.getCode())!=-1){
            //用户状态如果存在限制登录状态
            result.setCode(ResultEnum.LIMIT_LOGIN.getCode());
            result.setMsg(ResultEnum.LIMIT_LOGIN.getMsg());
            return result;
        }
        ApiResult<CustomerTokenVo> apiResult=CustomerService.mobileLogin(mobileLoginDto);
        return apiResult;
    }
    /**
     *
     * @param forgetPasswordDto
     * @return
     */
    @PostMapping("/forgetPassword")
    @ResponseBody
    public ApiResult<String> forgetPassword(@RequestBody @Validated ForgetPasswordDto forgetPasswordDto){
        ApiResult<String> result=new ApiResult<String>();
        if(!forgetPasswordDto.getPassword().equals(forgetPasswordDto.getRepeatPassword())){
            return ApiResult.badParam("两次输入密码不一致");
        }
        //先判断该手机号是否存在用户
        //通过交易账号判断是交易商编码或者是手机号，再通过不同的字段使用不同的方法查询用户的信息
        Customer customer=CustomerService.checkUsername(forgetPasswordDto.getMobile());
        if(null==customer){
            result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
            result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
            return result;
        }
        //通过公共服务校验短信验证码
        if(!CustomerService.checkMobileVerificationCode(forgetPasswordDto.getMobile(),forgetPasswordDto.getMobileVerificationCode(),"2")){
            result.setCode(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getCode());
            result.setMsg(ResultEnum.MOBILE_VERIFICATION_CODE_ERROR.getMsg());
            return result;
        }
        //存在则进行重置密码操作
        result=CustomerService.resetPassword(customer,forgetPasswordDto.getPassword());
        return result;
    }

    /**
     *  生成一个验证码
     * @param request
     */
    @PostMapping("/verificationCodeImage")
    @ResponseBody
    public ApiResult<String> getVerificationCodeImage(HttpServletRequest request){
        ApiResult result=new ApiResult();
        //生成一个随机四位数验证码
        String randomData =VerificationCodeUtils.getRandomCode(4);
        if(randomData==null){
            return ApiResult.error("验证码生成失败！");
        }
        // 将系统生成的文本内容保存到session中
        HttpSession session = request.getSession(true);
        //存储验证码数据到Session中
        session.setAttribute("VerificationCode",randomData);
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(randomData);
        return result;
    }

    /**
     * 请求手机短信验证码
     * @param mobileDto    手机号以及短信类型
     * @return
     */
    @PostMapping("/requestMobileVerificationCode")
    @ResponseBody
    public ApiResult<String>  requestMobileVerificationCode(@RequestBody @Validated MobileDto mobileDto){
        ApiResult result=new ApiResult();
        if(!MobileUtils.isMobileNO(mobileDto.getMobile())){
            return ApiResult.badParam("请输入正确的手机号码");
        }
        //先判断该手机号是否存在用户
        Customer customer=CustomerService.findByMobile(mobileDto.getMobile());
        if("1".equals(mobileDto.getType())){
            //注册判断不存在
            if(null!=customer){
                return ApiResult.badParam("该手机号已被注册");
            }
        }else  if("2".equals(mobileDto.getType())){
            //忘记密码判断存在
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
            if(customer.getCustomerStatus().indexOf("101")!=-1){
                //用户状态如果存在限制登录状态
                result.setCode(ResultEnum.LIMIT_LOGIN.getCode());
                result.setMsg(ResultEnum.LIMIT_LOGIN.getMsg());
                return result;
            }
        }else  if("3".equals(mobileDto.getType())){
            //设置资金密码
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
        }else  if("4".equals(mobileDto.getType())){
            //快捷登录
            if(null==customer){
                return ApiResult.badParam("手机号未注册");
            }
            if(customer.getCustomerStatus().indexOf("101")!=-1){
                //用户状态如果存在限制登录状态
                result.setCode(ResultEnum.LIMIT_LOGIN.getCode());
                result.setMsg(ResultEnum.LIMIT_LOGIN.getMsg());
                return result;
            }
        }else  if("5".equals(mobileDto.getType())){
            //判断存在
            if(null==customer){
                result.setCode(ResultEnum.USER_NOT_EXIST.getCode());
                result.setMsg(ResultEnum.USER_NOT_EXIST.getMsg());
                return result;
            }
        }else {
            return ApiResult.badParam("短信类型错误");
        }
        return CustomerService.requestMobileVerificationCode(mobileDto);
    }
}
