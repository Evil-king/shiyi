package com.baibei.shiyi.user.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.bean.CustomerBaseDto;
import com.baibei.shiyi.user.feign.bean.dto.RealnameVerificationDto;
import com.baibei.shiyi.user.feign.bean.vo.BankCardVerificationVo;
import com.baibei.shiyi.user.feign.bean.vo.CustomerVo;
import com.baibei.shiyi.user.feign.bean.vo.RealnameInfoVo;
import com.baibei.shiyi.user.model.CustomerDetail;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
* @author: hyc
* @date: 2019/05/30 15:41:30
* @description: CustomerDetail服务接口
*/
public interface ICustomerDetailService extends Service<CustomerDetail> {

    CustomerDetail findByCustomerNo(String customerNo);

    /**
     * 根据身份证号码获取用户信息
     * @param idCard
     * @return
     */
    List<CustomerDetail> findByIdCard(String idCard);

    /**
     * 实名认证
     */
    void realnameVerification(RealnameVerificationDto realnameVerificationDto);

    /**
     * 储蓄卡验证
     * @param cardNo
     * @return
     */
    ApiResult<BankCardVerificationVo> bankCardVerification(String cardNo);

    /**
     * 更新字段
     * @param customerNo
     * @param customerDetail
     */
    int modifySelectiveByCustomerNo(String customerNo,CustomerDetail customerDetail);


    /**
     * 获取用户实名信息
     * @param customerNo
     * @return
     */
    RealnameInfoVo getRealnameInfo(String customerNo);

}
