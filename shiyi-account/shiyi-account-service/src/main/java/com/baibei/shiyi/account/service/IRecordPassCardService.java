package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.RecordDto;
import com.baibei.shiyi.account.common.vo.RecordVo;
import com.baibei.shiyi.account.model.RecordPassCard;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;


/**
* @author: hyc
* @date: 2019/11/11 10:32:40
* @description: RecordPassCard服务接口
*/
public interface IRecordPassCardService extends Service<RecordPassCard> {

    MyPageInfo<RecordVo> getList(RecordDto recordDto);
}
