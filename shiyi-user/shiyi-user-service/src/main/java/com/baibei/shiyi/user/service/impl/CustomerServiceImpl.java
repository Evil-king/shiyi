package com.baibei.shiyi.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.component.rocketmq.core.util.RocketMQUtil;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.client.AccountFeign;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.api.ResultEnum;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.common.tool.enumeration.CustomerStatusEnum;
import com.baibei.shiyi.common.tool.exception.ServiceException;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.*;
import com.baibei.shiyi.content.feign.client.IUploadImageFeign;
import com.baibei.shiyi.publicc.feign.bean.dto.OperatorSmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.SmsDto;
import com.baibei.shiyi.publicc.feign.bean.dto.ValidateCodeDto;
import com.baibei.shiyi.publicc.feign.client.SmsFeign;
import com.baibei.shiyi.settlement.feign.bean.message.SettlementCustomerMsg;
import com.baibei.shiyi.user.common.GenerateCertificateTokenUtil;
import com.baibei.shiyi.user.common.dto.CustomerPageListDto;
import com.baibei.shiyi.user.common.dto.MobileLoginDto;
import com.baibei.shiyi.user.common.dto.OpenAccountDto;
import com.baibei.shiyi.user.common.dto.QuertSingleMemDto;
import com.baibei.shiyi.user.common.vo.CustomerPageListVo;
import com.baibei.shiyi.user.common.vo.CustomerTokenVo;
import com.baibei.shiyi.user.common.vo.QrcodeVo;
import com.baibei.shiyi.user.dao.CustomerDetailMapper;
import com.baibei.shiyi.user.dao.CustomerMapper;
import com.baibei.shiyi.user.dao.CustomerRefMapper;
import com.baibei.shiyi.user.feign.bean.dto.*;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.model.Customer;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.user.model.CustomerRef;
import com.baibei.shiyi.user.service.ICustomerDetailService;
import com.baibei.shiyi.user.service.ICustomerService;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: hyc
 * @date: 2019/05/23 19:39:38
 * @description: Customer服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CustomerServiceImpl extends AbstractService<Customer> implements ICustomerService {
    @Value("${errorTimes}")
    private Integer errorTimes;
    //邀请注册信息
    @Value("${inviteMessage}")
    private String inviteMessage;
    //二维码
    @Value("${Qrcode}")
    private String Qrcode;

    @Value("${rocketmq.settlement.customer.topics}")
    private String settlementCustomerTopics;

    @Value("${registerUrl}")
    private String registerUrl;

    @Value("${querySingleMemUrl}")
    private String querySingleMemUrl;


    @Value("${userPicture}")
    private String userPicture;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRefMapper customerRefMapper;
    @Autowired
    private RocketMQUtil rocketMQUtil;

    @Autowired
    private AccountFeign AccountFeign;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CustomerDetailMapper customerDetailMapper;

    @Autowired
    private ICustomerDetailService customerDetailService;

    @Autowired
    private SmsFeign smsFeign;

    @Autowired
    private IUploadImageFeign uploadImageFeign;

    /**
     * @param registerDto 注册信息
     * @param customer    上级推荐人
     * @return
     */
    @Override
    public ApiResult<CustomerTokenVo> register(RegisterDto registerDto, Customer customer) {
        //生成注册用户对象
        Customer registerCustomer = new Customer();
        registerCustomer.setMobile(registerDto.getMobile());
        String salt = MD5Util.getRandomSalt(10);//获取随机的盐值
        registerCustomer.setId(IdWorker.getId());
        registerCustomer.setCustomerNo(registerDto.getMobile());
        registerCustomer.setSalt(salt);
        registerCustomer.setPassword(MD5Util.md5Hex(registerDto.getPassword(), salt));
        registerCustomer.setRecommenderId(customer.getCustomerNo());//上级推荐人
        registerCustomer.setOrgId(2==customer.getCustomerType()?customer.getOrgCode():customer.getOrgId());
        registerCustomer.setCustomerType(new Byte("1"));
        registerCustomer.setCustomerStatus(CustomerStatusEnum.NORMAL.getCode());//100正常登陆
        //registerCustomer.setQrcode("123");//二维码先暂时不处理
        registerCustomer.setCreateTime(new Date());
        registerCustomer.setModifyTime(new Date());
        registerCustomer.setFlag(new Byte("1"));
        registerCustomer.setCityAgentCode(customer.getCityAgentCode());
        registerCustomer.setRegisterSource(registerDto.getRegisterSource());
        int length = registerCustomer.getCityAgentCode().toString().length();

        String customerNO = null;//生成一个交易商编码
        while (true) {
            //生成交易编码，并循环判断该编码是否重复
            customerNO = registerCustomer.getCityAgentCode() + NoUtil.randomNumbers(12 - length);
            Customer findCustomer = new Customer();
            findCustomer.setCustomerNo(customerNO);
            findCustomer.setFlag(new Byte("1"));
            findCustomer = customerMapper.selectOne(findCustomer);
            if (null == findCustomer) {
                break;
            }
        }
        registerCustomer.setCustomerNo(customerNO);
        registerCustomer.setSigning("0");
        customerMapper.insertSelective(registerCustomer);
        //插入一条关系记录表
        CustomerRef customerRef = new CustomerRef();
        customerRef.setId(IdWorker.getId());
        customerRef.setOrgId(registerCustomer.getOrgId());
        customerRef.setStartTime(new Date());
        customerRef.setRecommenderId(registerCustomer.getRecommenderId());
        customerRef.setCreateTime(new Date());
        customerRef.setCustomerNo(registerCustomer.getCustomerNo());
        customerRef.setFlag(new Byte("1"));
        customerRef.setModifyTime(new Date());
        customerRef.setOperationType(new Byte("1"));
        customerRef.setOperator(0L);
        customerRefMapper.insertSelective(customerRef);
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setId(IdWorker.getId());
        customerDetail.setCustomerNo(customerNO);
        customerDetail.setUserPicture(userPicture);
        customerDetail.setCreateTime(new Date());
        customerDetail.setModifyTime(new Date());
        customerDetail.setFlag(new Byte("1"));
        customerDetailMapper.insertSelective(customerDetail);
        //通过账户服务插入对应的记录
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(customerNO);
        AccountFeign.register(customerNoDto);
        CustomerTokenVo customerTokenVo = new CustomerTokenVo();
        try {
            //将用户信息存入redis
            updateRedisUser(registerCustomer);
            customerTokenVo = BeanUtil.copyProperties(registerCustomer, CustomerTokenVo.class);
            //注册成功，生成token相关信息
            CustomerTokenVo token = buildTokenToRedis(registerCustomer.getCustomerNo());
            customerTokenVo.setAccessToken(token.getAccessToken());
            customerTokenVo.setRefreshToken(token.getRefreshToken());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ApiResult.success(customerTokenVo);
    }

    @Override
    public ApiResult<CustomerTokenVo> login(Customer customer, LoginDto loginDto, HttpServletRequest request) {
        ApiResult apiResult = new ApiResult();
//        //判断验证码是否正确
//        HttpSession session = request.getSession();
//        if(!loginDto.getVerificationCode().equalsIgnoreCase(((String)session.getAttribute("VerificationCode")))){
//            apiResult.setCode(ResultEnum.LOGIN_VERIFICATION_CODE_ERROR.getCode());
//            apiResult.setMsg(ResultEnum.LOGIN_VERIFICATION_CODE_ERROR.getMsg());
//            return apiResult;
//        }
        //查到信息后通过MD5加盐加密处理进行密码的匹配
        String password = MD5Util.md5Hex(loginDto.getPassword(), customer.getSalt());
        if (!password.equals(customer.getPassword())) {
            //密码匹配失败，调用一次登录失败方法一次
            Integer failCount = increaseFailedLoginCounter(customer.getCustomerNo());
            if (failCount - errorTimes > 0) {
                return ApiResult.error("密码错误次数超过" + errorTimes + "次，请明天再试");
            }
            apiResult.setCode(ResultEnum.USER_PASSWORD_ERROR.getCode());
            apiResult.setMsg(ResultEnum.USER_PASSWORD_ERROR.getMsg());
            return apiResult;
        }
        //将用户信息存入redis
        updateRedisUser(customer);
        //匹配成功，则生成用户登录token以及刷新用户登录token的token，并设置失效时间，
        CustomerTokenVo customerTokenVo = BeanUtil.copyProperties(customer, CustomerTokenVo.class);
        //注册成功，生成token相关信息
        CustomerTokenVo token = buildTokenToRedis(customer.getCustomerNo());
        customerTokenVo.setAccessToken(token.getAccessToken());
        customerTokenVo.setRefreshToken(token.getRefreshToken());
        //登录成功删除用户错误登陆次数信息
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customer.getCustomerNo());
        redisUtil.set(key, "0");
        return ApiResult.success(customerTokenVo);
    }

    /**
     * 修改登录失败次数
     *
     * @param customerNo 交易商编码
     * @return 是否超过限定次数
     */
    public Boolean checkLoginFailCount(String customerNo) {
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customerNo);
        String v = redisUtil.get(key);
        if (org.springframework.util.StringUtils.isEmpty(v)) {
            return false;
        } else if (Integer.valueOf(v) - errorTimes < 0) {
            return false;
        } else {
            redisUtil.set(key, v, 1800);
            return true;
        }

    }

    /**
     * 通过公共服务验证验证码是否正确
     *
     * @param mobile
     * @param mobileVerificationCode 手机短信验证码
     * @return 正确true
     */
    public boolean checkMobileVerificationCode(String mobile, String mobileVerificationCode, String type) {
        ValidateCodeDto validateCodeDto = new ValidateCodeDto();
        validateCodeDto.setPhone(mobile);
        validateCodeDto.setCode(mobileVerificationCode);
        validateCodeDto.setType(type);
        ApiResult result = smsFeign.validateCode(validateCodeDto);
        if (result.hasSuccess()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 可以通过此方法查找到customer
     * 判断其是交易商编码或者是手机号
     *
     * @param username 交易商账号或邀请码
     * @return 用户
     */
    public Customer checkUsername(String username) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        if (MobileUtils.isMobileNO(username)) {
            //是手机号，通过手机号查找交易商编码（直属人ID）
            criteria.andEqualTo("mobile", username);
        } else {
            //是交易商编码
            criteria.andEqualTo("customerNo", username);
        }
        List<Customer> customers = customerMapper.selectByCondition(condition);
        if (customers.size() > 0) {
            return customers.get(0);
        } else {
            return null;
        }
    }

    /**
     * 通过手机号判断用户是否存在（已删除的不算）
     *
     * @param mobile 手机号
     * @return 是：存在 否：不存在
     */
    public boolean checkMobileRegistered(String mobile) {
        Customer customer = new Customer();
        customer.setFlag(new Byte("1"));
        customer.setMobile(mobile);
        customer = customerMapper.selectOne(customer);
        if (null == customer) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 向公共服务请求验证码
     *
     * @param mobileDto
     * @return
     */
    public ApiResult<String> requestMobileVerificationCode(MobileDto mobileDto) {
        SmsDto smsDto = new SmsDto();
        //手机号
        smsDto.setPhone(mobileDto.getMobile());
        //短信类型 1:注册 2:重置密码\
        OperatorSmsDto operatorSmsDto = new OperatorSmsDto();
        operatorSmsDto.setSmsType(mobileDto.getSmsType());
        operatorSmsDto.setContentNo(mobileDto.getType());
        operatorSmsDto.setPhone(mobileDto.getMobile());
        //发送短信并且入库
        ApiResult<String> apiResult = smsFeign.operatorSms(operatorSmsDto);
        if (!apiResult.hasSuccess()) {
            return ApiResult.error("系统开小差了，请联系客服");
        }
        smsDto.setMsg(apiResult.getData());
        ApiResult<String> apiResult1=smsFeign.getSms(smsDto);
        if (!apiResult1.hasSuccess()) {
            return ApiResult.error("系统开小差了，请联系客服");
        }
        return ApiResult.success();
    }

    /**
     * 通过customerNo查询用户信息
     *
     * @param customerNo customerNo
     * @return
     */
    @Override
    public Customer findByCustomerNo(String customerNo) {
        //先从redis里取用户的信息
//        String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customerNo);
//        Map<String,Object> map=redisUtil.hgetAll(key);
//        if(map.size()!=0){
//            //将redis里的值转换成map
//            Customer customer= (Customer) MapUtil.mapToObject(map,Customer.class);
//            return customer;
//        }
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        //如果为空还是根据删除状态为1查询该用户
        List<Customer> customers = customerMapper.selectByCondition(condition);
        if (customers.size() > 0) {
            return customers.get(0);
        } else {
            return null;
        }
    }

    /**
     * 重置用户密码
     *
     * @param customer 需要重置密码的用户
     * @param password 新密码
     * @return
     */
    @Override
    public ApiResult<String> resetPassword(Customer customer, String password) {
        //重新生成盐值
        String salt = MD5Util.getRandomSalt(10);
        customer.setSalt(salt);
        customer.setPassword(MD5Util.md5Hex(password, salt));
        customer.setModifyTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        //修改redis中用户的信息
        updateRedisUser(customer);
        return ApiResult.success();
    }

    /**
     * 修改密码
     *
     * @param updatePasswordDto
     * @return
     */
    public ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto) {
        ApiResult result = new ApiResult();
        Customer customer = checkUsername(updatePasswordDto.getCustomerNo());
        String password = MD5Util.md5Hex(updatePasswordDto.getOldPassword(), customer.getSalt());
        if (!password.equals(customer.getPassword())) {
            result.setCode(ResultEnum.USER_PASSWORD_ERRO.getCode());
            result.setMsg(ResultEnum.USER_PASSWORD_ERRO.getMsg());
            return result;
        }
        customer.setPassword(MD5Util.md5Hex(updatePasswordDto.getNewPassword(), customer.getSalt()));
        customer.setModifyTime(new Date());
        customerMapper.updateByPrimaryKeySelective(customer);
        //修改redis中用户的信息
        updateRedisUser(customer);
        return ApiResult.success();
    }

    /**
     * 通过手机号查询交易商编码
     *
     * @param mobile
     * @return
     */
    @Override
    public ApiResult<String> findCustomerNoByMobile(String mobile) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("mobile", mobile);
        List<Customer> list = customerMapper.selectByCondition(condition);
        return list.size() < 1 ? ApiResult.error() : ApiResult.success(list.get(0).getCustomerNo());
    }

    /**
     * 通过手机号查找用户
     *
     * @param mobile
     * @return
     */
    @Override
    public Customer findByMobile(String mobile) {
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("mobile", mobile);
        List<Customer> customerList = customerMapper.selectByCondition(condition);
        return customerList.size() < 1 ? null : customerList.get(0);
    }

    @Override
    public ApiResult<CustomerTokenVo> mobileLogin(MobileLoginDto mobileLoginDto) {
        Customer customer = findByMobile(mobileLoginDto.getMobile());
        if (!checkMobileVerificationCode(mobileLoginDto.getMobile(), mobileLoginDto.getVerificationCode(), "4")) {
            return ApiResult.badParam("请输入正确的验证码");
        }
        CustomerTokenVo customerTokenVo = BeanUtil.copyProperties(customer, CustomerTokenVo.class);
        CustomerTokenVo token = buildTokenToRedis(customer.getCustomerNo());
        customerTokenVo.setAccessToken(token.getAccessToken());
        customerTokenVo.setRefreshToken(token.getRefreshToken());
        return ApiResult.success(customerTokenVo);
    }

    @Override
    public MyPageInfo<CustomerPageListVo> adminPageList(CustomerPageListDto customerPageListDto) {
        PageHelper.startPage(customerPageListDto.getCurrentPage(), customerPageListDto.getPageSize());
        List<CustomerPageListVo> customerPageListVos = customerMapper.pageList(customerPageListDto);
        for (int i = 0; i < customerPageListVos.size(); i++) {
            CustomerPageListVo customerPageListVo = customerPageListVos.get(i);
            //状态操作
            String status = "";
            String[] statusArgs = customerPageListVo.getStatus().split(",");
            for (int j = 0; j < statusArgs.length; j++) {
                if (j == statusArgs.length - 1) {
                    status = status + CustomerStatusEnum.getMsg(statusArgs[j]);
                } else {
                    status = status + CustomerStatusEnum.getMsg(statusArgs[j]) + " ";
                }
            }
            customerPageListVo.setStatus(status);
            customerPageListVo.setMobile(MobileUtils.changeMobile(customerPageListVo.getMobile()));
        }

        MyPageInfo<CustomerPageListVo> page = new MyPageInfo<>(customerPageListVos);
        return page;
    }

    /**
     * 登录次数错误+1
     *
     * @param customerNo 交易商编号
     * @return 登录错误次数
     */
    private Integer increaseFailedLoginCounter(String customerNo) {
        String key = MessageFormat.format(RedisConstant.USER_ERROR_COUNT, customerNo);
        String v = redisUtil.get(key);
        if (org.springframework.util.StringUtils.isEmpty(v)) {
            redisUtil.set(key, "1", 1800);
            return 1;
        } else {
            v = Integer.valueOf(v) + 1 + "";
            redisUtil.set(key, v, 1800);
            return Integer.valueOf(v) + 1;
        }
    }

    /**
     * 将用户的token存入redis
     *
     * @param customerNo 交易商编码
     */
    public CustomerTokenVo buildTokenToRedis(String customerNo) {
        //登录token
        String accessToken = GenerateCertificateTokenUtil.accessToken();
        //刷新token
        String refreshToken = GenerateCertificateTokenUtil.refreshToken();
        //登录token生效时间
        long accessTokenExpireTime = GenerateCertificateTokenUtil.accessTokenExpireTime();
        //刷新token生效时间
        long refreshTokenExpireTime = GenerateCertificateTokenUtil.refreshTokenExpireTime();

        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);
        map.put("accessTokenExpireTime", String.valueOf(accessTokenExpireTime));
        map.put("refreshTokenExpireTime", String.valueOf(refreshTokenExpireTime));
        String redisKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
        redisUtil.hsetAll(redisKey, map);
        // 缓存token与customerNo的对应关系
        String tokenCodeKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, accessToken);
        redisUtil.set(tokenCodeKey, customerNo);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(format.format(accessTokenExpireTime));
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("时间戳转换失败");
        }
        redisUtil.expireAt(tokenCodeKey, date);
        CustomerTokenVo tokenVo = new CustomerTokenVo();
        tokenVo.setAccessToken(accessToken);
        tokenVo.setRefreshToken(refreshToken);
        return tokenVo;
    }

    /**
     * 修改存在redis中用户的信息
     *
     * @param customer 用户实体
     */
    private void updateRedisUser(Customer customer) {
        String key = MessageFormat.format(RedisConstant.USER_CUSTOMERINFO, customer.getCustomerNo());
        Map<String, Object> map = MapUtil.objectToMap(customer);
        redisUtil.hsetAll(key, map);
    }

    /**
     * 生成二维码
     *
     * @param
     * @return
     */
    @Override
    public ApiResult<QrcodeVo> createQrCode(String customerNo) {
        QrcodeVo qrcodeVo = new QrcodeVo();
        qrcodeVo.setMessage(inviteMessage);
        Customer customer=findByCustomerNo(customerNo);
        if(customer==null){
            return ApiResult.badParam("账号不存在");
        }
        if(StringUtils.isEmpty(customer.getQrcode())){
            ApiResult<String> uploadUrl = uploadImageFeign.createQrcode(Qrcode.replace("InvitationCode", customerNo), System.currentTimeMillis()+"");
            if(uploadUrl.hasFail()){
                return ApiResult.badParam(uploadUrl.getMsg());
            }
            customer.setQrcode(uploadUrl.getData());
            customer.setModifyTime(new Date());
            customerMapper.updateByPrimaryKeySelective(customer);
        }
        qrcodeVo.setQrcode(customer.getQrcode());
        return ApiResult.success(qrcodeVo);
    }

    /**
     * 退出登录
     *
     * @param customerNo
     */
    @Override
    public ApiResult<String> exitLogin(String customerNo) {
        Customer customer = findByCustomerNo(customerNo);
        if (customer == null) {
            return ApiResult.error("用户不存在");
        }
        deleteToken(customerNo);
        return ApiResult.success();
    }

    @Override
    public MyPageInfo<AdminCustomerBalanceVo> getCustomerBalanceList(AdminCustomerAccountDto adminCustomerAccountDto) {
        PageHelper.startPage(adminCustomerAccountDto.getCurrentPage(), adminCustomerAccountDto.getPageSize());
        List<AdminCustomerBalanceVo> adminCustomerBalanceVos = customerMapper.getCustomerBalanceList(adminCustomerAccountDto);
        MyPageInfo<AdminCustomerBalanceVo> myPageInfo = new MyPageInfo<>(adminCustomerBalanceVos);
        return myPageInfo;
    }

    @Override
    public List<AdminCustomerBalanceVo> getCustomerList(AdminCustomerAccountDto adminCustomerAccountDto) {
        return customerMapper.getCustomerBalanceList(adminCustomerAccountDto);
    }

    @Override
    public MyPageInfo<CustomerListVo> getAllCustomerList(CustomerListDto customerListDto) {
        PageHelper.startPage(customerListDto.getCurrentPage(), customerListDto.getPageSize());
        List<CustomerListVo> customerListVos = export(customerListDto);
        MyPageInfo<CustomerListVo> MyPageInfo = new MyPageInfo<>(customerListVos);
        return MyPageInfo;
    }

    @Override
    public ApiResult changeStatus(ChangeStatusDto changeStatusDto) {
        for (int i = 0; i < changeStatusDto.getCustomerNos().size(); i++) {
            List<String> customerNos = changeStatusDto.getCustomerNos();
            Customer customer = findByCustomerNo(customerNos.get(i));
            if (customer != null) {
                String status = "";
                for (int j = 0; j < changeStatusDto.getCustomerStatus().size(); j++) {
                    List<String> customerStatus = changeStatusDto.getCustomerStatus();
                    if (j == customerStatus.size() - 1) {
                        status = status + customerStatus.get(j);
                    } else {
                        status = status + customerStatus.get(j) + ",";
                    }
                }
                customer.setCustomerStatus(status);
                customer.setModifyTime(new Date());
                customerMapper.updateByPrimaryKeySelective(customer);
            } else {
                log.info("{}用户不存在", customerNos.get(i));
            }
        }
        return ApiResult.success();
    }

    @Override
    public ApiResult changeRecommend(ChangeRecommendDto changeRecommendDto) {
        Customer customer = findByCustomerNo(changeRecommendDto.getCustomerNo());
        if (customer == null) {
            return ApiResult.badParam("用户不存在");
        }
        Customer reconmenderCustomer=findByCustomerNo(changeRecommendDto.getNewRecommendId());
        if(reconmenderCustomer == null){
            return ApiResult.badParam("直接推荐人不存在");
        }
        customer.setRecommenderId(changeRecommendDto.getNewRecommendId());
        customer.setModifyTime(new Date());
        //修改用户归属信息
        int i = customerMapper.updateByPrimaryKeySelective(customer);
        if (i != 1) {
            return ApiResult.error("网络异常，请重试");
        }
        Condition condition = new Condition(CustomerRef.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", changeRecommendDto.getCustomerNo());
        List<CustomerRef> customerRefs = customerRefMapper.selectByCondition(condition);
        if (customerRefs.size() > 0) {
            //有数据将旧数据改成
            CustomerRef customerRef = customerRefs.get(0);
            customerRef.setEndTime(new Date());
            customerRef.setModifyTime(new Date());
            customerRef.setFlag(new Byte(Constants.Flag.UNVALID));
            customerRefMapper.updateByCondition(customerRef, condition);
        }
        //此处是关系表中无数据
        CustomerRef customerRef = new CustomerRef();
        customerRef.setId(IdWorker.getId());
        customerRef.setOrgId(customer.getOrgId());
        customerRef.setStartTime(new Date());
        customerRef.setRecommenderId(customer.getRecommenderId());
        customerRef.setCreateTime(new Date());
        customerRef.setCustomerNo(customer.getCustomerNo());
        customerRef.setFlag(new Byte("1"));
        customerRef.setModifyTime(new Date());
        customerRef.setOperationType(new Byte("2"));
        customerRef.setOperator(changeRecommendDto.getOperatorId());
        customerRefMapper.insertSelective(customerRef);
        return ApiResult.success();
    }

    @Override
    public List<CustomerListVo> export(CustomerListDto customerListDto) {
        List<CustomerListVo> customerListVos = customerMapper.getAllCustomerList(customerListDto);
        for (int i = 0; i < customerListVos.size(); i++) {
            CustomerListVo customerListVo = customerListVos.get(i);
            //状态操作
            String status = "";
            String[] statusArgs = customerListVo.getCustomerStatus().split(",");
            for (int j = 0; j < statusArgs.length; j++) {
                if (j == statusArgs.length - 1) {
                    status = status + CustomerStatusEnum.getMsg(statusArgs[j]);
                } else {
                    status = status + CustomerStatusEnum.getMsg(statusArgs[j]) + " ";
                }
            }
            customerListVo.setCustomerStatus(status);
        }
        return customerListVos;
    }

    @Override
    public void updateCustomerDetail(PABCustomerVo pabCustomerVo) {
        Customer customer = BeanUtil.copyProperties(pabCustomerVo, Customer.class);
        CustomerDetail customerDetail = customerDetailService.findByCustomerNo(customer.getCustomerNo());
        this.update(customer);
        log.info("当前用户的详细信息为{}", JSONObject.toJSONString(customerDetail));
        if (customerDetail != null) {
            customerDetail.setRealname(pabCustomerVo.getRealName());
            customerDetail.setIdcard(pabCustomerVo.getIdCard());
            customerDetailService.update(customerDetail);
        }
    }

    @Override
    public ApiResult<String> adminRegister(String mobile, String recommenderNo,Long superOrgcode,Long myOrgCode,Long cityAgentOrgCode) {
        System.out.println("入参推荐人编号为："+recommenderNo);
        //找到推荐人用户
//        Customer byCustomerNo = findByCustomerNo(recommenderNo);
//        if(byCustomerNo==null&&(!"0".equals(recommenderNo))){
//            return ApiResult.badParam("推荐人不存在");
//        }
        Customer byMobile = findByMobile(mobile);
        if(!StringUtils.isEmpty(byMobile)){
            return ApiResult.badParam("手机号已被注册");
        }
        String password=mobile.substring(mobile.length()-6,mobile.length());
        Customer registerCustomer = new Customer();
        registerCustomer.setMobile(mobile);
        String salt = MD5Util.getRandomSalt(10);//获取随机的盐值
        registerCustomer.setId(IdWorker.getId());
        registerCustomer.setSalt(salt);
        registerCustomer.setPassword(MD5Util.md5Hex(password, salt));
        //上级推荐人修改成0，代理层级无推荐人可言
        registerCustomer.setRecommenderId("0");//上级推荐人
        //代理用户直属代理写自己的机构编号
        registerCustomer.setOrgId(myOrgCode);
        registerCustomer.setCustomerType(new Byte("2"));
        registerCustomer.setCustomerStatus(CustomerStatusEnum.NORMAL.getCode());//100正常登陆
        //registerCustomer.setQrcode("123");//二维码先暂时不处理
        registerCustomer.setCreateTime(new Date());
        registerCustomer.setModifyTime(new Date());
        registerCustomer.setFlag(new Byte("1"));
        registerCustomer.setOrgCode(myOrgCode);
        registerCustomer.setCityAgentCode(cityAgentOrgCode);
        int length = cityAgentOrgCode.toString().length();

        String customerNO = null;//生成一个交易商编码
        while (true) {
            //生成交易编码，并循环判断该编码是否重复
            customerNO = cityAgentOrgCode + NoUtil.randomNumbers(12 - length);
            Customer findCustomer = new Customer();
            findCustomer.setCustomerNo(customerNO);
            findCustomer.setFlag(new Byte("1"));
            findCustomer = customerMapper.selectOne(findCustomer);
            if (null == findCustomer) {
                break;
            }
        }
        registerCustomer.setCustomerNo(customerNO);
        customerMapper.insertSelective(registerCustomer);
        //插入一条关系记录表
        CustomerRef customerRef = new CustomerRef();
        customerRef.setId(IdWorker.getId());
        customerRef.setOrgId(registerCustomer.getOrgId());
        customerRef.setStartTime(new Date());
        customerRef.setRecommenderId(registerCustomer.getRecommenderId());
        customerRef.setCreateTime(new Date());
        customerRef.setCustomerNo(registerCustomer.getCustomerNo());
        customerRef.setFlag(new Byte("1"));
        customerRef.setModifyTime(new Date());
        customerRef.setOperationType(new Byte("1"));
        customerRef.setOperator(0L);
        customerRefMapper.insertSelective(customerRef);
        CustomerDetail customerDetail = new CustomerDetail();
        customerDetail.setId(IdWorker.getId());
        customerDetail.setCustomerNo(customerNO);
        customerDetail.setUserPicture(userPicture);
        customerDetail.setCreateTime(new Date());
        customerDetail.setModifyTime(new Date());
        customerDetail.setFlag(new Byte("1"));
        customerDetailMapper.insertSelective(customerDetail);
        //通过账户服务插入对应的记录
        CustomerNoDto customerNoDto = new CustomerNoDto();
        customerNoDto.setCustomerNo(customerNO);
        ApiResult<String> register = AccountFeign.register(customerNoDto);
        if(register.hasFail()){
            throw new ServiceException("注册失败，请重新再试");
        }
        //将用户信息存入redis
        updateRedisUser(registerCustomer);
        return ApiResult.success(registerCustomer.getCustomerNo());
    }

    @Override
    public ApiResult confirmSign(CustomerNoDto customerNoDto) {
        Customer customer = new Customer();
        customer.setIsSign("0");
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNoDto.getCustomerNo());
        criteria.andEqualTo("isSign","1" );
        if(customerMapper.updateByConditionSelective(customer, condition) > 0){
            return ApiResult.success();
        }
        return  ApiResult.error();
    }

    @Override
    public int modifySelectiveByCustomerNo(String customerNo, Customer customer) {
        if (StringUtils.isEmpty(customerNo)) {
            throw new ServiceException("用户编码不能为空");
        }
        Customer byCustomerNo = findByCustomerNo(customerNo);
        if (StringUtils.isEmpty(byCustomerNo)) {
            log.info("查询不到指定用户"+customerNo);
            throw new ServiceException("查询不到指定用户");
        }
        Condition condition = new Condition(Customer.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("flag","1");
        criteria.andEqualTo("customerNo",customerNo);
        int i = customerMapper.updateByConditionSelective(customer, condition);
        return i;
    }

    @Override
    public ApiResult changeUser(List<ChangeUserDto> changeUserDto) {

        return null;
    }

    @Override
    public ApiResult openAccount(OpenAccountDto openAccountDto) {
        Customer customer=findByCustomerNo(openAccountDto.getCustomerNo());
        if(StringUtils.isEmpty(customer)){
            return ApiResult.badParam("用户不存在");
        }
        RegisterFuQingDto registerFuQingDto=new RegisterFuQingDto();
        registerFuQingDto.setMemCode(openAccountDto.getCustomerNo());
        registerFuQingDto.setTradeAccount(openAccountDto.getCustomerNo());
        registerFuQingDto.setExchangeFundAccount(openAccountDto.getCustomerNo());
        registerFuQingDto.setFullName(openAccountDto.getRealname());
        registerFuQingDto.setTel(openAccountDto.getMobile());
        registerFuQingDto.setIdNo(openAccountDto.getIdcardNo());
        if("男".equals(IDCardUtils.getSex(openAccountDto.getIdcardNo()))){
            registerFuQingDto.setGender("1");
        }else {
            registerFuQingDto.setGender("0");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        registerFuQingDto.setInitDate(sdf.format(new Date()));
        registerFuQingDto.setRequestId(UUID.randomUUID().toString().replace("-", ""));
        registerFuQingDto.setBankProCode("citicyq");
        registerFuQingDto.setBankAccount(openAccountDto.getBankCardNo());
        registerFuQingDto.setBankAccountName(openAccountDto.getRealname());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf1.format(new Date());
        registerFuQingDto.setBusiDatetime(now);
        registerFuQingDto.setLegalPerson(openAccountDto.getRealname());

        registerFuQingDto.setContactName(openAccountDto.getRealname());
        registerFuQingDto.setContactTel(openAccountDto.getMobile());

        registerFuQingDto.setContactEmail(openAccountDto.getEmail());
        registerFuQingDto.setContactAddress(openAccountDto.getProvince()+openAccountDto.getCity()+openAccountDto.getArea()+openAccountDto.getAddress());
        registerFuQingDto.setContactName(openAccountDto.getRealname());
        registerFuQingDto.setContactTel(openAccountDto.getMobile());
        registerFuQingDto.setContactEmail(openAccountDto.getEmail());
        registerFuQingDto.setContactAddress(openAccountDto.getProvince()+openAccountDto.getCity()+openAccountDto.getArea()+openAccountDto.getAddress());
        registerFuQingDto.setLegalPerson(openAccountDto.getRealname());
        if(BankCardNoUtils.checkBankCardNoIsCITIC(openAccountDto.getBankCardNo())){
            //如果为本行
            registerFuQingDto.setBankNode(null);
            registerFuQingDto.setBankTotalNode(null);
            registerFuQingDto.setBankCardType("0");
        }else {
            registerFuQingDto.setBankNode(openAccountDto.getBankNode());
            registerFuQingDto.setBankTotalNode(openAccountDto.getBankTotalNode());
            registerFuQingDto.setBankCardType("1");
        }
        String json = JSONObject.toJSONString(registerFuQingDto);
        log.info("请求地址为{}"+registerUrl);
        String response = HttpClientUtils.doPostJson(registerUrl, json);
        JSONObject object=JSON.parseObject(response);
        if(object.getInteger("code")==200){
            //操作成功就需要做一些其他事了
            customer.setSigning("1");
            customer.setModifyTime(new Date());
            String bankClientNo=object.getJSONObject("data").getString("bankClientNo");
            String fundAccountClear=object.getJSONObject("data").getString("fundAccountClear");
            String memCodeClear=object.getJSONObject("data").getString("memCodeClear");
            customer.setBankClientNo(bankClientNo);
            customer.setFundAccountClear(fundAccountClear);
            customer.setMemCodeClear(memCodeClear);
            customer.setSigningTime(new Date());
            //修改用户信息
            customerMapper.updateByPrimaryKeySelective(customer);
            CustomerDetail customerDetail=customerDetailService.findByCustomerNo(customer.getCustomerNo());
            if(customerDetail!=null){
                //存储一些detail信息
                customerDetail.setProvince(openAccountDto.getProvince());
                customerDetail.setCity(openAccountDto.getCity());
                customerDetail.setArea(openAccountDto.getArea());
                customerDetail.setAddress(openAccountDto.getAddress());
                customerDetail.setEmail(openAccountDto.getEmail());
                customerDetail.setBankName(openAccountDto.getBankName());
                customerDetail.setBranchName(openAccountDto.getBranchName());
                customerDetail.setModifyTime(new Date());
                customerDetailMapper.updateByPrimaryKeySelective(customerDetail);
            }
            SettlementCustomerMsg msg=new SettlementCustomerMsg();
            msg.setCustomerNo(customer.getCustomerNo());
            msg.setSignFlag(new Byte("1"));
            msg.setFundAccountClear(fundAccountClear);
            //发送开户消息
            rocketMQUtil.sendMsg(settlementCustomerTopics,JacksonUtil.beanToJson(msg),String.valueOf(UUID.randomUUID()));
            return ApiResult.success();
        }else {
            return ApiResult.error(object.getJSONObject("data").getString("errorInfo"));
        }
    }

    @Override
    public ApiResult getFuQingSingleMem(CustomerNoDto customerNoDto) {
        QuertSingleMemDto quertSingleMemDto=new QuertSingleMemDto();
        quertSingleMemDto.setMemCode(customerNoDto.getCustomerNo());
        String resp = HttpClientUtils.doPostJson(querySingleMemUrl,JSONObject.toJSONString(quertSingleMemDto));
        JSONObject jsonObject=JSON.parseObject(resp);
        if(jsonObject.getInteger("code")==200){
            return ApiResult.success(jsonObject.getJSONObject("data"));
        }else {
            return ApiResult.error(jsonObject.getString("msg"));
        }

    }

    /**
     * 删除用户token信息
     *
     * @param customerNo
     */
    private void deleteToken(String customerNo) {
        String redisKey = MessageFormat.format(RedisConstant.PREFIX_USER_TOKEN, customerNo);
        String userToken = (String) redisUtil.hmget(redisKey, "accessToken");
        String tokenCodeKey = MessageFormat.format(RedisConstant.PREFIX_TOKEN_CODE, userToken);
        redisUtil.delete(tokenCodeKey);
        redisUtil.delete(redisKey);
    }
}
