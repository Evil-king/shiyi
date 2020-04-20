package com.baibei.shiyi.admin.modules.security.web;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.admin.modules.security.AuthenticationInfo;
import com.baibei.shiyi.admin.modules.security.AuthorizationUser;
import com.baibei.shiyi.admin.modules.security.JwtUser;
import com.baibei.shiyi.admin.modules.security.utils.JwtTokenUtil;
import com.baibei.shiyi.admin.modules.security.utils.SecurityUtils;
import com.baibei.shiyi.admin.modules.system.model.SysUser;
import com.baibei.shiyi.admin.modules.system.service.ISysUserService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.utils.MD5Util;
import com.baibei.shiyi.publicc.feign.bean.dto.OperatorSmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.SmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import com.baibei.shiyi.publicc.feign.client.SmsFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author uqing
 * @date 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SmsFeign smsFeign;

    @Value("${sendType:1}")
    private String sendType; //短信发送类型

    @Value("${isVerification:false}")
    private Boolean isVerification;

    @Value("${contentNo:4}")
    private String contentNo; //短信发送类型编号


    /**
     * 登录授权
     * 1、验证注册码
     * 2、token 存进redis
     *
     * @param authorizationUser
     * @return
     */
    //@Log("用户登录")
    @PostMapping(value = "${jwt.auth.path}")
    public ApiResult<AuthenticationInfo> login(@Validated @RequestBody AuthorizationUser authorizationUser) {

        final JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if (!jwtUser.getPassword().equals(MD5Util.md5Hex(authorizationUser.getPassword(), jwtUser.getSalt()))) {
            return ApiResult.error("密码错误");
        }
        ValidateCodeDto validateCodeDto = new ValidateCodeDto();

        if (log.isDebugEnabled()) {
            log.info("当前用户的信息为{}", JSONObject.toJSONString(jwtUser));
        }
        if (isVerification) {
            validateCodeDto.setPhone(jwtUser.getMobile());
            validateCodeDto.setCode(authorizationUser.getCode());
            validateCodeDto.setType(contentNo);
            log.info("当前的发送短信类型为:{}", JSONObject.toJSONString(validateCodeDto));
            ApiResult smsResult = smsFeign.validateCode(validateCodeDto);
            if (smsResult.hasFail()) {
                log.info("发送验证码失败{}", smsResult.getMsg());
                return ApiResult.error("验证码错误");
            }
        }
        if (jwtUser.getUserStatus().equals(Constants.OrganizationStatus.DISABLE)) {
            return ApiResult.error("账号已停用，请联系管理员");
        }
        //获取用户的token,是否存在

        String token = jwtTokenUtil.generateToken(jwtUser);
        jwtTokenUtil.redisTokenSave(jwtUser.getUsername(), token);
        return ApiResult.success(new AuthenticationInfo(token, jwtUser));
    }

    /**
     * 如果已经过期的，就清理当前线程的上下文
     *
     * @return
     */
    @PostMapping(path = "/loggingOut")
    public ApiResult loggingOut() {
        try {
            String userName = SecurityUtils.getUsername();
            jwtTokenUtil.redisTokenClear(userName);
            SecurityContextHolder.clearContext();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return ApiResult.success();
        }
        return ApiResult.success();
    }


    /**
     * 发送手机短信验证码
     *
     * @param authorizationUser
     * @return
     */
    @PostMapping(path = "/sendPhoneCode")
    public ApiResult<String> sendPhoneCode(@RequestBody AuthorizationUser authorizationUser) {
        if (authorizationUser.getUsername() == null) {
            return ApiResult.error("用户名不能为空");
        }
        SysUser user = userService.findByName(authorizationUser.getUsername());
        if (user == null) {
            return ApiResult.error("用户不存在");
        }
        OperatorSmsDto operatorSmsDto = new OperatorSmsDto();
        operatorSmsDto.setPhone(user.getMobile());
        operatorSmsDto.setContentNo(contentNo);
        operatorSmsDto.setSmsType(sendType);
        ApiResult<String> result = smsFeign.operatorSms(operatorSmsDto);
        if (!result.getCode().equals(ResultEnum.SUCCESS.getCode())) {
            log.info("获取消息模板失败内容为{},消息为{},", result.getData(), result.getMsg());
            result.setMsg("系统开小差了,请联系客服");
            return result;
        }
        SmsDto smsDto = new SmsDto();
        smsDto.setPhone(user.getMobile());
        smsDto.setMsg(result.getData());

        ApiResult<String> sendResult = smsFeign.getSms(smsDto);
        if (sendResult.hasFail()) {
            log.info("发送短信失败内容为{},消息为{},", result.getData(), result.getMsg());
            sendResult.setMsg("系统开小差了,请联系客服");
            return sendResult;
        }
        return sendResult;
    }


    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "${jwt.auth.account}")
    public ResponseEntity getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

}
