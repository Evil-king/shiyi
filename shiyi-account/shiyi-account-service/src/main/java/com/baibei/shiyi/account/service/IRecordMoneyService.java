package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.RecordMoney;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: hyc
* @date: 2019/05/24 10:38:13
* @description: RecordMoney服务接口
*/
public interface IRecordMoneyService extends Service<RecordMoney> {

    MyPageInfo<RecordVo> recordList(RecordDto recordDto);

    MyPageInfo<AdminRecordVo> AdminRecordList(AdminRecordDto recordDto);

    List<AdminRecordVo> list(AdminRecordDto recordDto);
}
