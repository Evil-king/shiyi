package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.model.CustomerBean;
import com.baibei.shiyi.account.model.RecordEmpowermentBalance;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;


/**
* @author: hyc
* @date: 2019/11/11 10:51:10
* @description: RecordEmpowermentBalance服务接口
*/
public interface IRecordEmpowermentBalanceService extends Service<RecordEmpowermentBalance> {
    ApiResult insertRecord(ChangeCustomerBeanDto frozenAmountDto, CustomerBean customerBean);

    MyPageInfo<RecordVo> recordList(RecordBeanDto recordDto);
}
