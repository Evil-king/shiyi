package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.RecordBeanDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.feign.bean.dto.AdminRecordBeanDto;
import com.baibei.shiyi.account.feign.bean.vo.AdminRecordVo;
import com.baibei.shiyi.account.model.RecordBean;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;

import java.math.BigDecimal;
import java.util.List;


/**
* @author: hyc
* @date: 2019/10/29 16:56:57
* @description: RecordBean服务接口
*/
public interface IRecordBeanService extends Service<RecordBean> {

    MyPageInfo<RecordVo> recordList(RecordBeanDto recordDto);

    MyPageInfo<AdminRecordVo> recordPageList(AdminRecordBeanDto recordDto);

    List<AdminRecordVo> exportRecordList(AdminRecordBeanDto recordDto);

    /**
     * 获取某个用户的兑换积分净增量（收入-回退）与余额取小值
     * @return
     */
    ApiResult<BigDecimal> getDailyIncrement(String customerNo);
}
