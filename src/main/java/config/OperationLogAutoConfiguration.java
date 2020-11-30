package config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 小余哥
 * @description: \
 * @create: 2020-07-01 09:29
 **/
@Configuration
@EnableConfigurationProperties(OperationLogProperties.class)
public class OperationLogAutoConfiguration {

    /*@Bean
    public RedissonClient redissonClient(OperationLogProperties operationLogProperties){

    }*/
}
