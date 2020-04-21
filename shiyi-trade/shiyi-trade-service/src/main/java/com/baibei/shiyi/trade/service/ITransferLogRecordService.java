package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferPageListDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferPageListVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferLogRecord;

import java.util.List;


/**
* @author: wenqing
* @date: 2020/01/03 16:47:27
* @description: TransferLogRecord服务接口
*/
public interface ITransferLogRecordService extends Service<TransferLogRecord> {

    void createObj(TransferTemplateVo transferTemplateVo,long transferLogId);

    List<TransferTemplateVo> selectByTransferLogId(long transferLogId);

    void deleteByTransferId(long transferLogId  );

    MyPageInfo<TransferPageListVo> listPage(TransferPageListDto transferPageListDto);
}
