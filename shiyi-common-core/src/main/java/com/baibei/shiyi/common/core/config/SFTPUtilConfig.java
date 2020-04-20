package com.baibei.shiyi.common.core.config;

import com.baibei.shiyi.common.core.condition.SFTPCondition;
import com.baibei.shiyi.common.tool.bean.SftpProperties;
import com.baibei.shiyi.common.tool.utils.SFTPUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SFTPUtilConfig {

    @Bean
    @Conditional(SFTPCondition.class)
    public SFTPUtils sftpUtils(SftpProperties sftpProperties) {
        SFTPUtils sftpUtils = new SFTPUtils(
                sftpProperties.getUsername()
                , sftpProperties.getPassword(),
                null,
                sftpProperties.getHost(),
                sftpProperties.getPort(),
                sftpProperties.getFilePath());
        return sftpUtils;
    }

    @Bean
    @ConfigurationProperties(prefix = "sftp")
    public SftpProperties sftpProperties() {
        return new SftpProperties();
    }

}

