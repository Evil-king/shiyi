package com.baibei.shiyi.publicc.service;
import com.baibei.shiyi.common.tool.api.ApiResult;
import com.baibei.shiyi.publicc.model.Sms;
import com.baibei.shiyi.common.core.mybatis.Service;


/**
* @author: wenqing
* @date: 2019/05/30 13:41:20
* @description: Sms服务接口
*/
public interface ISmsService extends Service<Sms> {
    /**
     * 发送短信
     *
     * @param phone
     * @param message
     * @return
     */
    ApiResult getSms(String phone, String message);

    /**
     * 验证短信码是否存在
     *
     * @param phone
     * @param code
     * @param type
     * @return
     */
    ApiResult validateCode(String phone, String code, String type);

}
