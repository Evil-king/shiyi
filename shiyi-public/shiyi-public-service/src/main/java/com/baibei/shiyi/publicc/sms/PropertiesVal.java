package com.baibei.shiyi.publicc.sms;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author hwq
 * @date 2019/05/29
 */
@Data
@Component
public class PropertiesVal {

    //start--短信相关验证
    @Value("${sms.temple}")
    private String smsTemple;

    @Value("${sms.default}")
    private String smsDefaultTmple;

    @Value("${sms.time.out}")
    private long smsTimeOut;

    @Value("${sms.max.count}")
    private int smsMaxCount;

    @Value("${sms.max.time}")
    private int smsMaxTime;
    //end

    //start--微网通联
    @Value("${wwtl.sms.url_submit}")
    private String url_submit;

    @Value("${wwtl.sms.username}")
    private String username;

    @Value("${wwtl.sms.password}")
    private String password;

    @Value("${wwtl.sms.prudoct}")
    private String productNum;

    @Value("${wwtl.sms.sign}")
    private String sign;
    //end

}
