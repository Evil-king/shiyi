package com.baibei.shiyi.trade.service;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.feign.bean.dto.TransferDeleteDto;
import com.baibei.shiyi.trade.feign.bean.dto.TransferLogDto;
import com.baibei.shiyi.trade.feign.bean.vo.TransferLogVo;
import com.baibei.shiyi.trade.feign.bean.vo.TransferTemplateVo;
import com.baibei.shiyi.trade.model.TransferLog;

import java.util.List;


/**
* @author: wenqing
* @date: 2019/12/26 14:21:39
* @description: TransferLog服务接口
*/
public interface ITransferLogService extends Service<TransferLog> {

    /**
     * 非交易过户操作列表
     * @param transferLogDto
     * @return
     */
    MyPageInfo<TransferLogVo> pageListLog(TransferLogDto transferLogDto);

    ApiResult importData(List<TransferTemplateVo> transferTemplateListVo);

    void updateStatus(long id);

    ApiResult deleteData(TransferDeleteDto transferDeleteDto);
}
