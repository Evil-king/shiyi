package com.baibei.shiyi.common.core.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

@Slf4j
public class FTPCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String host = conditionContext.getEnvironment().getProperty("ftp.host");
        String port = conditionContext.getEnvironment().getProperty("ftp.port");
        String userName = conditionContext.getEnvironment().getProperty("ftp.userName");
        String password = conditionContext.getEnvironment().getProperty("ftp.password");
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port) || StringUtils.isEmpty(userName)
                || StringUtils.isEmpty(password)) {
            log.info("当前ftp工具类有些数据为空 host>[{}],port>[{}],userName>[{}],password>[{}]", host, port, userName, password);
            return false;
        }

        return true;
    }
}
