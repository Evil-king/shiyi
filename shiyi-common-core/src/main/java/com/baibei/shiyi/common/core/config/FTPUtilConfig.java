package com.baibei.shiyi.common.core.config;

import com.baibei.shiyi.common.core.condition.FTPCondition;
import com.baibei.shiyi.common.tool.bean.FtpProperties;
import com.baibei.shiyi.common.tool.utils.FTPUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;


/**
 * 注入FTPUtil配置
 */
@Configuration
public class FTPUtilConfig {


    @Bean
    @Conditional(FTPCondition.class)
    public FTPUtils ftpUtils(FtpProperties ftpProperties) {
        FTPUtils ftpUtils = new FTPUtils(
                ftpProperties.getHost(),
                ftpProperties.getPort(),
                ftpProperties.getUsername(),
                ftpProperties.getPassword(),
                ftpProperties.getControlEncoding(),
                ftpProperties.getRemotePath());
        return ftpUtils;
    }

    @Bean
    @ConfigurationProperties(prefix = "ftp")
    public FtpProperties ftpProperties() {
        return new FtpProperties();
    }
}
