package com.baibei.shiyi.trade.service;

import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.trade.common.bo.ChangeHoldPositionBo;
import com.baibei.shiyi.trade.common.dto.HoldRecordDto;
import com.baibei.shiyi.trade.common.vo.HoldRecordVo;
import com.baibei.shiyi.trade.model.HoldRecord;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/12/26 10:36:53
 * @description: HoldRecord服务接口
 */
public interface IHoldRecordService extends Service<HoldRecord> {
    /**
     * 保存持仓变动记录
     *
     * @param bo
     */
    void saveRecord(ChangeHoldPositionBo bo);

    MyPageInfo<HoldRecordVo> customerHistoryList(HoldRecordDto holdRecordDto);

}
