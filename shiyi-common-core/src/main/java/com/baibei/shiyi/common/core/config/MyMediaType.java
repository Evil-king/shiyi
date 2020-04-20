package com.baibei.shiyi.common.core.config;

import org.springframework.http.MediaType;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/3 17:02
 * @description: SpringBoot Actuator健康检查端点MediaType
 */
public class MyMediaType extends MediaType implements Serializable {
    public static final MediaType APPLICATION_ACTUATOR_JSON = valueOf("application/vnd.spring-boot.actuator.v2+json;charset=UTF-8");

    public MyMediaType(String type) {
        super(type);
    }

    public MyMediaType(String type, String subtype) {
        super(type, subtype);
    }

    public MyMediaType(String type, String subtype, Charset charset) {
        super(type, subtype, charset);
    }

    public MyMediaType(String type, String subtype, double qualityValue) {
        super(type, subtype, qualityValue);
    }

    public MyMediaType(MediaType other, Charset charset) {
        super(other, charset);
    }

    public MyMediaType(MediaType other, Map<String, String> parameters) {
        super(other, parameters);
    }

    public MyMediaType(String type, String subtype, Map<String, String> parameters) {
        super(type, subtype, parameters);
    }
}