package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.dao.RecordEmpowermentBalanceMapper;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.model.CustomerBean;
import com.baibei.shiyi.account.model.RecordBean;
import com.baibei.shiyi.account.model.RecordEmpowermentBalance;
import com.baibei.shiyi.account.service.IRecordEmpowermentBalanceService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.enumeration.RecordEmpowermentEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @author: hyc
* @date: 2019/11/11 10:51:10
* @description: RecordEmpowermentBalance服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordEmpowermentBalanceServiceImpl extends AbstractService<RecordEmpowermentBalance> implements IRecordEmpowermentBalanceService {

    @Autowired
    private RecordEmpowermentBalanceMapper tblRecordEmpowermentBalanceMapper;

    @Override
    public ApiResult insertRecord(ChangeCustomerBeanDto frozenAmountDto, CustomerBean customerBean) {
        //此处存储的余额是待赋能通证余额
        RecordEmpowermentBalance recordBean=new RecordEmpowermentBalance();
        String recordNo=IdWorker.get32UUID();
        recordBean.setId(IdWorker.getId());
        recordBean.setRecordNo(recordNo);
        //交易商编码
        recordBean.setCustomerNo(frozenAmountDto.getCustomerNo());
        //如果
        BigDecimal change=frozenAmountDto.getReType().equals(Constants.Retype.IN)? frozenAmountDto.getChangeAmount():BigDecimal.ZERO.subtract(frozenAmountDto.getChangeAmount());
        //变动数额
        recordBean.setChangeAmount(change.setScale(2,BigDecimal.ROUND_DOWN));
        //可用资金数额
        recordBean.setBalance(customerBean.getEmpowermentBalance());
        //交易类型
        recordBean.setTradeType(frozenAmountDto.getTradeType());
        //订单号
        recordBean.setOrderNo(frozenAmountDto.getOrderNo());
        //冻结类型 1：支出，2：收入
        recordBean.setRetype(frozenAmountDto.getReType());
        recordBean.setBeanType(frozenAmountDto.getCustomerBeanType());
        recordBean.setCreateTime(new Date());
        recordBean.setModifyTime(new Date());
        //是否有效
        recordBean.setFlag(new Byte("1"));
        tblRecordEmpowermentBalanceMapper.insertSelective(recordBean);
        return ApiResult.success();
    }

    @Override
    public MyPageInfo<RecordVo> recordList(RecordBeanDto recordDto) {
        if(!StringUtils.isEmpty(recordDto.getStartTime())){
            recordDto.setStartTime(DateUtil.changeTimes(Long.valueOf(recordDto.getStartTime())));
        }
        if(!StringUtils.isEmpty(recordDto.getEndTime())){
            recordDto.setEndTime(DateUtil.changeTimes(Long.valueOf(recordDto.getEndTime())));
        }
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<RecordVo> recordVos=tblRecordEmpowermentBalanceMapper.recordList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(RecordEmpowermentEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<RecordVo> myPageInfo=new MyPageInfo<>(recordVos);
        return myPageInfo;
    }
}
