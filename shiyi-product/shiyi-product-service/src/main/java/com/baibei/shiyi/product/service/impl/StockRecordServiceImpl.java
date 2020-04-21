package com.baibei.shiyi.product.service.impl;

import com.baibei.shiyi.product.dao.StockRecordMapper;
import com.baibei.shiyi.product.model.StockRecord;
import com.baibei.shiyi.product.service.IStockRecordService;
import com.baibei.shiyi.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/09/17 16:18:36
* @description: StockRecord服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class StockRecordServiceImpl extends AbstractService<StockRecord> implements IStockRecordService {

    @Autowired
    private StockRecordMapper tblProStockRecordMapper;

}
