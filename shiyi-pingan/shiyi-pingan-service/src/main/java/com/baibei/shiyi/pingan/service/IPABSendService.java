package com.baibei.shiyi.pingan.service;

import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.pingan.feign.base.dto.PABSendDto;
import com.baibei.shiyi.pingan.feign.base.vo.PABSendVo;

/**
 * 平安银行发送消息接口类
 */
public interface IPABSendService {

    /**
     * 如果用具体的参数来实现
     * 交易系统的流水号
     */
    ApiResult<PABSendVo> sendMessage(PABSendDto request);

    /**
     * 生成随机的唯一序列号
     *
     * @return
     */
    String generateThiredLogNo();
}
