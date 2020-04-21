package com.baibei.shiyi.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.common.tool.utils.CollectionUtils;
import com.baibei.shiyi.common.tool.utils.IdWorker;
import com.baibei.shiyi.trade.dao.TransferLogRecordMapper;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferLogRecord;
import com.baibei.shiyi.trade.service.ITransferLogRecordService;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
* @author: wenqing
* @date: 2020/01/03 16:47:27
* @description: TransferLogRecord服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TransferLogRecordServiceImpl extends AbstractService<TransferLogRecord> implements ITransferLogRecordService {

    @Autowired
    private TransferLogRecordMapper transferLogRecordMapper;

    @Override
    public void createObj(TransferTemplateVo transferTemplateVo,long transferLogId) {
        log.info("transferTemplateVo={}", JSONObject.toJSONString(transferTemplateVo));
        String tradeTime = transferTemplateVo.getTradeTime() + " 00:00:00";
        TransferLogRecord transferLogRecord = new TransferLogRecord();
        transferLogRecord.setId(IdWorker.getId());
        transferLogRecord.setTransferLogId(transferLogId);
        transferLogRecord.setCostPrice(new BigDecimal(transferTemplateVo.getCostPrice()));
        transferLogRecord.setInCustomerNo(transferTemplateVo.getInCustomerNo());
        transferLogRecord.setOutCustomerNo(transferTemplateVo.getOutCustomerNo());
        transferLogRecord.setPrice(new BigDecimal(transferTemplateVo.getPrice()));
        transferLogRecord.setProductTradeNo(transferTemplateVo.getProductTradeNo());
        transferLogRecord.setRemark(transferTemplateVo.getRemark());
        transferLogRecord.setTransferNum(Integer.valueOf(transferTemplateVo.getNum()));
        transferLogRecord.setTradeTime(tradeTime);
        transferLogRecord.setIsfee(transferTemplateVo.getIsFee());
        transferLogRecord.setFlag((byte)1);
        transferLogRecord.setCreateTime(new Date());
        transferLogRecord.setModifyTime(new Date());
        transferLogRecordMapper.insertSelective(transferLogRecord);
    }

    @Override
    public List<TransferTemplateVo> selectByTransferLogId(long transferLogId) {
        List<TransferTemplateVo> transferTemplateVos = Lists.newArrayList();
        Condition condition = new Condition(TransferLogRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("transferLogId", transferLogId);
        List<TransferLogRecord> transferLogRecords = transferLogRecordMapper.selectByCondition(condition);
        if(!CollectionUtils.isEmpty(transferLogRecords)){
            transferLogRecords.stream().forEach(transferLogRecord -> {
                TransferTemplateVo transferTemplateVo = new TransferTemplateVo();
                transferTemplateVo .setProductTradeNo(transferLogRecord.getProductTradeNo());
                transferTemplateVo .setCostPrice(String.valueOf(transferLogRecord.getCostPrice()));
                transferTemplateVo .setInCustomerNo(transferLogRecord.getInCustomerNo());
                transferTemplateVo .setOutCustomerNo(transferLogRecord.getOutCustomerNo());
                transferTemplateVo .setIsFee(String.valueOf(transferLogRecord.getIsfee()));
                transferTemplateVo .setNum(String.valueOf(transferLogRecord.getTransferNum()));
                transferTemplateVo .setPrice(String.valueOf(transferLogRecord.getPrice()));
                transferTemplateVo .setRemark(transferLogRecord.getRemark());
                transferTemplateVo.setTradeTime(transferLogRecord.getTradeTime());

                transferTemplateVos.add(transferTemplateVo);
            });
        }
        return transferTemplateVos;
    }

    @Override
    public void deleteByTransferId(long transferLogId) {
        Condition condition = new Condition(TransferLogRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("transferLogId", transferLogId);
        List<TransferLogRecord> transferLogRecords = transferLogRecordMapper.selectByCondition(condition);
        if(!CollectionUtils.isEmpty(transferLogRecords)){
            transferLogRecords.stream().forEach(transferLogRecord -> {
                transferLogRecordMapper.deleteByPrimaryKey(transferLogRecord);
            });
        }
    }

    @Override
    public MyPageInfo<TransferPageListVo> listPage(TransferPageListDto transferPageListDto) {
        PageHelper.startPage(transferPageListDto.getCurrentPage(), transferPageListDto.getPageSize());
        List<TransferPageListVo> transferPageListVos = transferLogRecordMapper.listPage(transferPageListDto);
        MyPageInfo<TransferPageListVo> myPageInfo = new MyPageInfo<>(transferPageListVos);
        return myPageInfo;
    }
}
