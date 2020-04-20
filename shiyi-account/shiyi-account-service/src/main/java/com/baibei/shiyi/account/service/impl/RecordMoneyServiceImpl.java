package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.dao.RecordMoneyMapper;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.RecordMoney;
import com.baibei.shiyi.account.service.IRecordMoneyService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.enumeration.TradeMoneyTradeTypeEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;


/**
* @author: hyc
* @date: 2019/05/24 10:38:13
* @description: RecordMoney服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordMoneyServiceImpl extends AbstractService<RecordMoney> implements IRecordMoneyService {

    @Autowired
    private RecordMoneyMapper recordMoneyMapper;

    @Override
    public MyPageInfo<RecordVo> recordList(RecordDto recordDto) {
        if(!StringUtils.isEmpty(recordDto.getStartTime())){
            recordDto.setStartTime(DateUtil.changeTimes(Long.valueOf(recordDto.getStartTime())));
        }
        if(!StringUtils.isEmpty(recordDto.getEndTime())){
            recordDto.setEndTime(DateUtil.changeTimes(Long.valueOf(recordDto.getEndTime())));
        }
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<RecordVo> recordVos=recordMoneyMapper.recordList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(TradeMoneyTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<RecordVo> myPageInfo=new MyPageInfo(recordVos);
        return myPageInfo;
    }

    @Override
    public MyPageInfo<AdminRecordVo> AdminRecordList(AdminRecordDto recordDto) {
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<AdminRecordVo> recordVos=recordMoneyMapper.AdminRecordList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(TradeMoneyTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<AdminRecordVo> myPageInfo=new MyPageInfo<>(recordVos);
        return myPageInfo;
    }

    @Override
    public List<AdminRecordVo> list(AdminRecordDto recordDto) {
        List<AdminRecordVo> recordVos=recordMoneyMapper.AdminRecordList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(TradeMoneyTradeTypeEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        return recordVos;
    }
}
