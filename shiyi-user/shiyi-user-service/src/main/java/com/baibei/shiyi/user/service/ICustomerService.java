package com.baibei.shiyi.user.service;

import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.user.common.dto.CustomerPageListDto;
import com.baibei.shiyi.user.common.dto.MobileLoginDto;
import com.baibei.shiyi.user.common.dto.OpenAccountDto;
import com.baibei.shiyi.user.common.vo.CustomerPageListVo;
import com.baibei.shiyi.user.common.vo.CustomerTokenVo;
import com.baibei.shiyi.user.common.vo.QrcodeVo;
import com.baibei.shiyi.user.feign.bean.dto.*;
import com.baibei.shiyi.user.feign.bean.vo.AdminCustomerBalanceVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerListVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.PABCustomerVo;
import com.baibei.shiyi.user.model.Customer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author: hyc
 * @date: 2019/05/23 19:39:38
 * @description: Customer服务接口
 */
public interface ICustomerService extends Service<Customer> {
    /**
     * 注册
     *
     * @param registerDto
     * @param customer
     * @return
     */
    ApiResult<CustomerTokenVo> register(RegisterDto registerDto, Customer customer);

    /**
     * 登录
     *
     * @param customer
     * @param loginDto
     * @param request
     * @return
     */
    ApiResult<CustomerTokenVo> login(Customer customer, LoginDto loginDto, HttpServletRequest request);

    /**
     * 判断redis中密码错误次数是否达到了指定次数
     *
     * @param customerNo
     * @return
     */
    Boolean checkLoginFailCount(String customerNo);

    /**
     * 校验手机验证码是否正确
     *
     * @param mobile
     * @param mobileVerificationCode
     * @return
     */
    boolean checkMobileVerificationCode(String mobile, String mobileVerificationCode, String type);

    /**
     * 通过手机号或者用户编号查询用户
     *
     * @param username
     * @return
     */
    Customer checkUsername(String username);

    /**
     * 判断手机号是否注册过了
     *
     * @param mobile
     * @return
     */
    boolean checkMobileRegistered(String mobile);

    /**
     * 请求短信验证码
     *
     * @param mobileDto
     * @return
     */
    ApiResult<String> requestMobileVerificationCode(MobileDto mobileDto);

    /**
     * 根据用户编码获取用户信息
     *
     * @param customerNo
     * @return
     */
    Customer findByCustomerNo(String customerNo);

    /**
     * 充值密码
     *
     * @param customer
     * @param password
     * @return
     */
    ApiResult<String> resetPassword(Customer customer, String password);

    /**
     * 修改密码
     *
     * @param updatePasswordDto
     * @return
     */
    ApiResult<String> updatePassword(UpdatePasswordDto updatePasswordDto);

    /**
     * 通过手机号获取用户编号
     *
     * @param mobile
     * @return
     */
    ApiResult<String> findCustomerNoByMobile(String mobile);

    /**
     * 通过手机号获取用户
     *
     * @param mobile
     * @return
     */
    Customer findByMobile(String mobile);

    /**
     * 手机快捷登陆
     *
     * @param mobileLoginDto
     * @return
     */
    ApiResult<CustomerTokenVo> mobileLogin(MobileLoginDto mobileLoginDto);

    MyPageInfo<CustomerPageListVo> adminPageList(CustomerPageListDto customerPageListDto);

    /**
     * 返回安卓和IOS二维码给前端
     *
     * @param
     * @return
     */
    ApiResult<QrcodeVo> createQrCode(String customerNo);

    /**
     * 退出登录
     *
     * @param customerNo
     * @return
     */
    ApiResult<String> exitLogin(String customerNo);

    /**
     * 后台用户余额列表
     *
     * @param adminCustomerAccountDto
     * @return
     */
    MyPageInfo<AdminCustomerBalanceVo> getCustomerBalanceList(AdminCustomerAccountDto adminCustomerAccountDto);

    List<AdminCustomerBalanceVo> getCustomerList(AdminCustomerAccountDto adminCustomerAccountDto);

    /**
     * 后台用户管理列表
     *
     * @param customerListDto
     * @return
     */
    MyPageInfo<CustomerListVo> getAllCustomerList(CustomerListDto customerListDto);

    /**
     * 修改状态
     *
     * @param changeStatusDto
     * @return
     */
    ApiResult changeStatus(ChangeStatusDto changeStatusDto);

    /**
     * 修改归属
     *
     * @param changeRecommendDto
     * @return
     */
    ApiResult changeRecommend(ChangeRecommendDto changeRecommendDto);

    /**
     * 导出用的list数据
     *
     * @param customerListDto
     * @return
     */
    List<CustomerListVo> export(CustomerListDto customerListDto);

    /**
     * 修改签约用户详细信息
     *
     * @param pabCustomerVo
     */
    void updateCustomerDetail(PABCustomerVo pabCustomerVo);

    /**
     * 后台注册代理，其中2个参数由于产品需求不进行数据处理，
     * @param mobile
     * @param recommenderNo
     * @param cityAgentOrgCode 市代编码
     * @param superOrgCode     上级编码
     * @return
     */
    ApiResult<String> adminRegister(String mobile, String recommenderNo, Long superOrgCode, Long myOrgCode, Long cityAgentOrgCode);

    /**
     * 更新用户购销协议状态
     *
     * @param customerNoDto
     * @return
     */
    ApiResult confirmSign(CustomerNoDto customerNoDto);

    /**
     * 更新
     *
     * @param customerNo
     * @param customer
     * @return
     */
    int modifySelectiveByCustomerNo(String customerNo, Customer customer);

    ApiResult changeUser(List<ChangeUserDto> changeUserDto);

    /**
     * 业务系统开户
     *
     * @param openAccountDto
     * @return
     */
    ApiResult openAccount(OpenAccountDto openAccountDto);

    ApiResult getFuQingSingleMem(CustomerNoDto customerNoDto);


}
