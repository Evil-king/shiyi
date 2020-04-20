package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.dao.RecordBeanMapper;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.CustomerBean;
import com.baibei.shiyi.account.model.RecordBean;
import com.baibei.shiyi.account.service.ICustomerBeanService;
import com.baibei.shiyi.account.service.IRecordBeanService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.common.tool.enumeration.RecordBeanTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/10/29 16:56:57
* @description: RecordBean服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordBeanServiceImpl extends AbstractService<RecordBean> implements IRecordBeanService {

    @Autowired
    private RecordBeanMapper tblRecordBeanMapper;

    @Autowired
    private ICustomerBeanService customerBeanService;

    @Override
    public MyPageInfo<RecordVo> recordList(RecordBeanDto recordDto) {
        if(!StringUtils.isEmpty(recordDto.getStartTime())){
            recordDto.setStartTime(DateUtil.changeTimes(Long.valueOf(recordDto.getStartTime())));
        }
        if(!StringUtils.isEmpty(recordDto.getEndTime())){
            recordDto.setEndTime(DateUtil.changeTimes(Long.valueOf(recordDto.getEndTime())));
        }
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<RecordVo> recordVos=tblRecordBeanMapper.recordList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(RecordBeanTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<RecordVo> myPageInfo=new MyPageInfo<>(recordVos);
        return myPageInfo;
    }

    @Override
    public MyPageInfo<AdminRecordVo> recordPageList(AdminRecordBeanDto recordDto) {
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<AdminRecordVo> recordVos=tblRecordBeanMapper.recordPageList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(RecordBeanTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<AdminRecordVo> myPageInfo=new MyPageInfo<>(recordVos);
        return myPageInfo;
    }

    @Override
    public List<AdminRecordVo> exportRecordList(AdminRecordBeanDto recordDto) {
        List<AdminRecordVo> recordVos=tblRecordBeanMapper.recordPageList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(RecordBeanTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        return recordVos;
    }

    @Override
    public ApiResult<BigDecimal> getDailyIncrement(String customerNo) {
        BigDecimal increment=tblRecordBeanMapper.getDailyIncrement(customerNo);
        ApiResult<CustomerBean> balanceByType = customerBeanService.getBalanceByType(customerNo, Constants.BeanType.EXCHANGE);
        if(balanceByType.hasFail()){
            return ApiResult.error("查询余额失败");
        }
        BigDecimal dayIncrement=increment.compareTo(balanceByType.getData().getBalance())<0?increment:balanceByType.getData().getBalance();
        return ApiResult.success(dayIncrement);
    }
}
