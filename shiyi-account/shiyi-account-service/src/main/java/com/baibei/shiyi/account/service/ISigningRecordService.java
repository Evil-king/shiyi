package com.baibei.shiyi.account.service;

import com.baibei.shiyi.account.feign.bean.dto.PABSigningRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.PABSigningRecordVo;
import com.baibei.shiyi.account.model.SigningRecord;
import com.baibei.shiyi.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: uqing
 * @date: 2019/11/01 17:58:10
 * @description: SigningRecord服务接口
 */
public interface ISigningRecordService extends Service<SigningRecord> {

    /**
     * 根据会员代码和标识查询签约信息
     *
     * @param customerNO
     * @return
     */
    SigningRecord findByThirdCustId(String customerNO);

    /**
     * 根据会员子账号查询用户,由银行分配至
     *
     * @param custAcctId
     * @return
     */
    SigningRecord findByOneCustAcctId(String custAcctId);


    /**
     * 查询系统所有签约的客户列表
     *
     * @return
     */
    List<SigningRecord> allList();


    /**
     * 验证身份证是否唯一性
     *
     * @param
     * @return
     */
    Boolean isOnlyIdCode(String idCode);

    /**
     * 验证除了自己以外,是否添加额外的身份证
     *
     * @param idCode
     * @param customerNo
     * @return
     */
    Boolean isOnlyIdCode(String idCode, String customerNo);


    /**
     * 查询解约的用户并且flag为1的用户
     */
    SigningRecord findByReleaseThirdCustId(String thirdCustId);


    /**
     * 平安签约
     *
     * @return
     */
    PABSigningRecordVo signingRecord(PABSigningRecordDto request);


    /**
     * 查询今天用户是否已经签约
     *
     * @param customerNo 会员代码
     * @return
     */
    Boolean isTodaySigning(String customerNo);

    List<SigningRecord> findByThirdCustIdList(List<String> customerNoS);

    String getBalance(String message);
}
