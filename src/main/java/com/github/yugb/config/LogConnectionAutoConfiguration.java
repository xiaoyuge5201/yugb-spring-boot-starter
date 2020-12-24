package com.github.yugb.config;

import com.github.yugb.util.JdbcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 小余哥
 * @description: \
 * @create: 2020-07-01 09:29
 **/
@Configuration
@EnableConfigurationProperties(LogConnectionProperties.class)
public class LogConnectionAutoConfiguration {

    @Bean
    public JdbcClient jdbcClient(LogConnectionProperties logConnectionProperties) {
        return JdbcClient.create(logConnectionProperties);
    }
}
