package com.baibei.shiyi.publicc.util;

import com.alibaba.fastjson.JSONObject;
import com.baibei.component.redis.util.RedisUtil;
import com.baibei.shiyi.common.tool.constants.RedisConstant;
import com.baibei.shiyi.publicc.sms.PropertiesVal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Slf4j
@Component
public class SmsUtil {

    @Autowired
    public PropertiesVal propertiesVal;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 短信模板组装方法
     *
     * @param smsType
     * @param type
     * @param code
     * @param phone
     * @return
     */
    public String operatorSms(String smsType, String type, String code, String phone) {
        String content = "";
        try {
            JSONObject templateObj = JSONObject
                    .parseObject(propertiesVal.getSmsTemple());
            log.info("短信模板==>" + templateObj.toJSONString());
            if (templateObj != null) {
                //将code+phone为key放入缓存
                String key = MessageFormat.format(RedisConstant.SMS_USER_PHONE, phone + type);
                redisUtil.set(key, code, propertiesVal.getSmsMaxTime());

                JSONObject temp = templateObj.getJSONObject(String.valueOf(smsType));
                if (temp != null) { //这里的判断是针对特殊类型的短信模版 比如'4':{'0':'{0}行情报价已有{1}分钟没有变化，请联系行情业务相关人处理，最后一口报价时间{2}。'}
                    content = temp.getString(type.toString());
                    if ("3".equals(smsType)) {//发送短信模板
                        String[] contentRep = code.split(",");
                        if (content != null && !"".equals(content) && contentRep != null && !"".equals(contentRep)) {
                            try {
                                content = MessageFormat.format(content, contentRep);
                            } catch (Exception e) {
                                log.info("短信模板替换参数格式错误==>" + contentRep);
                            }
                        }
                    } else {
                        String[] contentRep = new String[]{code};
                        content = MessageFormat.format(content, contentRep);
                    }
                }
            } else {
                return "请配置相应的短信模板";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
