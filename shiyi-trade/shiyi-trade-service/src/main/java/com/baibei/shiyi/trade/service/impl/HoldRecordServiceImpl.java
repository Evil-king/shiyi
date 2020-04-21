package com.baibei.shiyi.trade.service.impl;

import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.enumeration.HoldResourceEnum;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.common.tool.utils.NoUtil;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.common.dto.HoldRecordDto;
import com.baibei.shiyi.trade.common.vo.HoldRecordVo;
import com.baibei.shiyi.trade.dao.HoldRecordMapper;
import com.baibei.shiyi.trade.model.HoldRecord;
import com.baibei.shiyi.trade.service.IHoldRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 10:36:53
 * @description: HoldRecord服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HoldRecordServiceImpl extends AbstractService<HoldRecord> implements IHoldRecordService {

    @Autowired
    private HoldRecordMapper holdRecordMapper;

    @Override
    public void saveRecord(ChangeHoldPositionBo bo) {
        HoldRecord record = new HoldRecord();
        record.setId(IdWorker.getId());
        record.setRecordNo(NoUtil.getHoldRecordNo());
        record.setCustomerNo(bo.getCustomerNo());
        record.setProductTradeNo(bo.getProductTradeNo());
        record.setCount(bo.getCount());
        record.setPrice(bo.getPrice());
        record.setResource(bo.getResource());
        record.setResourceNo(bo.getResourceNo());
        record.setRemark(bo.getRemark());
        record.setReType(bo.getReType());
        record.setCreateTime(new Date());
        save(record);
    }

    @Override
    public MyPageInfo<HoldRecordVo> customerHistoryList(HoldRecordDto holdRecordDto) {
        PageHelper.startPage(holdRecordDto.getCurrentPage(), holdRecordDto.getPageSize());

        if (holdRecordDto.getStartTime() != null && !"".equals(holdRecordDto.getStartTime())) {
            String startTime = stampToDate(holdRecordDto.getStartTime()) + " 00:00:00";
            holdRecordDto.setStartTime(startTime);
        }
        if (holdRecordDto.getEndTime() != null && !"".equals(holdRecordDto.getEndTime())) {
            String endTime = stampToDate(holdRecordDto.getEndTime()) + " 23:59:59";
            holdRecordDto.setEndTime(endTime);
        }
        List<HoldRecordVo> holdRecordVoList = holdRecordMapper.customerHistoryList(holdRecordDto);
        holdRecordVoList.stream().forEach(holdRecordVo -> {
            if (HoldResourceEnum.BUY.getCode().equals(holdRecordVo.getResource())) {
                holdRecordVo.setResourceDesc(HoldResourceEnum.BUY.getDesc());
            }
            if (HoldResourceEnum.SELL.getCode().equals(holdRecordVo.getResource())) {
                holdRecordVo.setResourceDesc(HoldResourceEnum.SELL.getDesc());
            }
            if (HoldResourceEnum.EXCHANGE.getCode().equals(holdRecordVo.getResource())) {
                holdRecordVo.setResourceDesc(HoldResourceEnum.EXCHANGE.getDesc());
            }
            if (HoldResourceEnum.BUY_TRANSFER.getCode().equals(holdRecordVo.getResource())) {
                holdRecordVo.setResourceDesc(HoldResourceEnum.BUY_TRANSFER.getDesc());
            }
            if (HoldResourceEnum.SELL_TRANSFER.getCode().equals(holdRecordVo.getResource())) {
                holdRecordVo.setResourceDesc(HoldResourceEnum.SELL_TRANSFER.getDesc());
            }
        });
        MyPageInfo<HoldRecordVo> myPageInfo = new MyPageInfo(holdRecordVoList);
        return myPageInfo;
    }

    protected String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
