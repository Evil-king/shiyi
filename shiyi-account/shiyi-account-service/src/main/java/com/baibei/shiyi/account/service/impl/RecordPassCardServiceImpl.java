package com.baibei.shiyi.account.service.impl;

import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.dao.RecordPassCardMapper;
import com.baibei.shiyi.account.model.RecordPassCard;
import com.baibei.shiyi.account.service.IRecordPassCardService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.enumeration.RecordPassCardEnum;
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
* @date: 2019/11/11 10:32:40
* @description: RecordPassCard服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordPassCardServiceImpl extends AbstractService<RecordPassCard> implements IRecordPassCardService {

    @Autowired
    private RecordPassCardMapper tblRecordPassCardMapper;

    @Override
    public MyPageInfo<RecordVo> getList(RecordDto recordDto) {
        if(!StringUtils.isEmpty(recordDto.getStartTime())){
            recordDto.setStartTime(DateUtil.changeTimes(Long.valueOf(recordDto.getStartTime())));
        }
        if(!StringUtils.isEmpty(recordDto.getEndTime())){
            recordDto.setEndTime(DateUtil.changeTimes(Long.valueOf(recordDto.getEndTime())));
        }
        PageHelper.startPage(recordDto.getCurrentPage(),recordDto.getPageSize());
        List<RecordVo> recordVos=tblRecordPassCardMapper.getList(recordDto);
        for (int i = 0; i <recordVos.size() ; i++) {
            recordVos.get(i).setTradeType(RecordPassCardEnum.getMsg(recordVos.get(i).getTradeType()));
        }
        MyPageInfo<RecordVo> myPageInfo=new MyPageInfo(recordVos);
        return myPageInfo;
    }
}
