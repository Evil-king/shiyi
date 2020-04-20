package com.baibei.shiyi.common.core.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;

@Slf4j
public class SFTPCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String host = conditionContext.getEnvironment().getProperty("sftp.host");
        String port = conditionContext.getEnvironment().getProperty("sftp.port");
        String userName = conditionContext.getEnvironment().getProperty("sftp.username");
        String password = conditionContext.getEnvironment().getProperty("sftp.password");
        String filePath = conditionContext.getEnvironment().getProperty("sftp.filePath");
        if (StringUtils.isEmpty(host) || StringUtils.isEmpty(port) || StringUtils.isEmpty(userName)
                || StringUtils.isEmpty(password) || filePath.isEmpty()) {
            log.info("当前sftp工具类有些数据为空 host>[{}],port>[{}],userName>[{}],password>[{}],filePath->[{}]", host, port, userName, password,filePath);
            return false;
        }
        return true;
    }
}
