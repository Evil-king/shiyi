package com.baibei.shiyi.cash.service;

import com.baibei.shiyi.cash.bean.vo.SumBankOrderVo;
import com.baibei.shiyi.cash.feign.base.dto.BankOrderDto;
import com.baibei.shiyi.cash.feign.base.vo.BankOrderVo;
import com.baibei.shiyi.cash.model.BankOrder;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.pingan.feign.base.dto.ViewFileDto;

import java.util.List;


/**
* @author: Longer
* @date: 2019/11/06 13:51:22
* @description: BankOrder服务接口
*/
public interface IBankOrderService extends Service<BankOrder> {

    /**
     * 读取银行出入金流水文件，并插入数据库
     * @param viewFileDto
     * @return 返回批次号
     */
    String bankOrder(ViewFileDto viewFileDto);

    /**
     * 根据批次查询银行出入金流水信息
     * @param batchNo
     * @return
     */
    List<BankOrder> getOrderListByBatchNo(String batchNo);


    BankOrder getOneBankOrder(BankOrder bankOrder);


    MyPageInfo<BankOrderVo> pageList(BankOrderDto bankOrderDto);

    List<BankOrderVo> BankOrderVoList(BankOrderDto bankOrderDto);

    SumBankOrderVo getSumData(BankOrderDto bankOrderDto);
}
